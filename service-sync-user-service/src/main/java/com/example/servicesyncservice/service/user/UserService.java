package com.example.servicesyncservice.service.user;

import com.example.servicesyncservice.dto.user.UpsertUserRequest;
import com.example.servicesyncservice.dto.user.UserResponse;
import com.example.servicesyncservice.dto.user.UserResponsesList;
import com.example.servicesyncservice.entity.RoleType;

public interface UserService {
    UserResponsesList getAllUsers();
    UserResponse findUserById(Long id);
    UserResponse findUserByUsername(String username);
    UserResponse updateUser(UpsertUserRequest upsertUserRequest, Long id);
    void deleteUserById(Long id);
    UserResponsesList getAllUsersByRole(RoleType role);
    UserResponse updateRole(Long id, RoleType roleType);

}
