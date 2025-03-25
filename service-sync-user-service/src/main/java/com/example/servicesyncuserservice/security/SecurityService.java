package com.example.servicesyncuserservice.security;

import com.example.servicesyncuserservice.dto.refreshToken.RefreshTokenRequest;
import com.example.servicesyncuserservice.dto.refreshToken.RefreshTokenResponse;
import com.example.servicesyncuserservice.dto.user.AuthResponse;
import com.example.servicesyncuserservice.dto.user.LoginRequest;
import com.example.servicesyncuserservice.dto.user.UpsertUserRequest;
import com.example.servicesyncuserservice.entity.RefreshToken;
import com.example.servicesyncuserservice.entity.User;
import com.example.servicesyncuserservice.exception.RefreshTokenException;
import com.example.servicesyncuserservice.repository.UserRepository;
import com.example.servicesyncuserservice.security.jwt.JwtUtils;
import com.example.servicesyncuserservice.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return AuthResponse.builder()
                .id(userDetails.getId())
                .email(userDetails.getEmail())
                .username(userDetails.getUsername())
                .roles(roles)
                .token(jwtUtils.generateJwtToken(userDetails))
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public void register(UpsertUserRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .roles(request.getRoles())
                .name(request.getName())
                .surname(request.getSurname())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String requestRefreshToken = refreshTokenRequest.getRefreshToken();

        return refreshTokenService.findByRefreshToken(requestRefreshToken)
                .map(refreshTokenService::checkRefreshToken)
                .map(RefreshToken::getUserId)
                .map(userId -> {
                    User tokenOwner = userRepository.findById(userId)
                            .orElseThrow(() ->
                                    new RefreshTokenException("Ошибка при получении токена по id пользователя: " + userId));
                    String token = jwtUtils.generateTokenFromUsername(tokenOwner.getUsername());

                    return new RefreshTokenResponse(token, refreshTokenService.createRefreshToken(userId).getToken());
                }).orElseThrow(() -> new RefreshTokenException(requestRefreshToken, "Refresh token не найден"));
    }

    public void logout() {
        var currentPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(currentPrincipal instanceof AppUserDetails userDetails) {
            Long userId = userDetails.getId();

            refreshTokenService.deleteByUserId(userId);
        }
    }

}
