package com.example.servicesyncuserservice.controller;

import com.example.servicesyncuserservice.dto.refreshToken.RefreshTokenRequest;
import com.example.servicesyncuserservice.dto.refreshToken.RefreshTokenResponse;
import com.example.servicesyncuserservice.dto.simple.SimpleResponse;
import com.example.servicesyncuserservice.dto.user.*;
import com.example.servicesyncuserservice.exception.AlreadyExistsException;
import com.example.servicesyncuserservice.repository.UserRepository;
import com.example.servicesyncuserservice.security.SecurityService;
import com.example.servicesyncuserservice.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service-sync/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    private final SecurityService securityService;

    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> authUser(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(securityService.authenticateUser(request));
    }

    @PostMapping("/register")
    public ResponseEntity<SimpleResponse> registerUser(@RequestBody UpsertUserRequest upsertUserRequest) {
        if(userRepository.existsByUsername(upsertUserRequest.getUsername())) {
            throw new AlreadyExistsException("Пользователь с данным именем уже существует");
        }

        if(userRepository.existsByEmail(upsertUserRequest.getEmail())) {
            throw new AlreadyExistsException("Пользователь с данной почтой уже существует");
        }

        securityService.register(upsertUserRequest);

        return ResponseEntity.ok(new SimpleResponse("User created!"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(securityService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<SimpleResponse> logout(@AuthenticationPrincipal UserDetails userDetails) {
        securityService.logout();

        return ResponseEntity.ok(new SimpleResponse("User logged out!"));
    }

    @GetMapping("/get/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<UserResponsesList> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponse> update(@RequestBody UpsertUserRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(userService.updateUser(request, id));
    }

    @DeleteMapping("/delete/id")
    public ResponseEntity<SimpleResponse> delete(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok(new SimpleResponse("Пользователь с id" + id + "удален!"));
    }

}
