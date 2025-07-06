package com.example.servicesyncservice.controller;

import com.example.servicesyncservice.dto.refreshToken.RefreshTokenRequest;
import com.example.servicesyncservice.dto.refreshToken.RefreshTokenResponse;
import com.example.servicesyncservice.dto.role.RoleRequest;
import com.example.servicesyncservice.dto.simple.SimpleResponse;
import com.example.servicesyncservice.dto.user.*;
import com.example.servicesyncservice.entity.RoleType;
import com.example.servicesyncservice.entity.User;
import com.example.servicesyncservice.exception.AlreadyExistsException;
import com.example.servicesyncservice.exception.EntityNotFoundException;
import com.example.servicesyncservice.repository.UserRepository;
import com.example.servicesyncservice.security.AppUserDetails;
import com.example.servicesyncservice.security.SecurityService;
import com.example.servicesyncservice.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/create/admin")
    public ResponseEntity<SimpleResponse> createAdmin() {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createAdmin());
    }

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

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(@AuthenticationPrincipal AppUserDetails userDetails) {
        return ResponseEntity.ok(userService.findUserById(userDetails.getId()));
    }

    @GetMapping("/get/all")
    public ResponseEntity<UserResponsesList> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/get/role")
    public ResponseEntity<UserResponsesList> getAllUsersByRole(@RequestParam(required = false) String role) {
        System.out.println("Fetching users by role: " + role);
        try {
            if (role == null || role.isEmpty()) {
                return ResponseEntity.ok(userService.getAllUsers());
            }

            RoleType roleType = RoleType.valueOf(role.toUpperCase());
            UserResponsesList userResponsesList = userService.getAllUsersByRole(roleType);
            return ResponseEntity.ok(userResponsesList);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid role: " + role);
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponse> update(@RequestBody UpsertUserRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(userService.updateUser(request, id));
    }

    @PutMapping("/role/update/{id}")
    public ResponseEntity<UserResponse> updateRole(@PathVariable Long id, @RequestBody RoleRequest request) {
        try {
            RoleType roleType = RoleType.valueOf(request.getRole().toUpperCase());
            UserResponse updatedUser = userService.updateRole(id, roleType);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SimpleResponse> delete(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok(new SimpleResponse("Пользователь с id" + id + "удален!"));
    }

}
