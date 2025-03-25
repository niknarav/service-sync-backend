package com.example.servicesyncuserservice.service.user;

import com.example.servicesyncuserservice.dto.user.UpsertUserRequest;
import com.example.servicesyncuserservice.dto.user.UserResponse;
import com.example.servicesyncuserservice.dto.user.UserResponsesList;

public interface UserService {
    UserResponsesList getAllUsers();
    UserResponse findUserById(Long id);
    UserResponse findUserByUsername(String username);
    UserResponse updateUser(UpsertUserRequest upsertUserRequest, Long id);
    void deleteUserById(Long id);
}
