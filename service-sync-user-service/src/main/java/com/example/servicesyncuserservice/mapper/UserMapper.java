package com.example.servicesyncuserservice.mapper;

import com.example.servicesyncuserservice.dto.user.UpsertUserRequest;
import com.example.servicesyncuserservice.dto.user.UserResponse;
import com.example.servicesyncuserservice.dto.user.UserResponsesList;
import com.example.servicesyncuserservice.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User requestToEntity(UpsertUserRequest request) {
        return User.builder()
                .username(request.getUsername())
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .roles(request.getRoles())
                .password(request.getPassword())
                .build();
    }

    public UserResponse entityToResponse(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .roles(user.getRoles())
                .build();
    }

    public UserResponsesList entityListToResponsesList(List<User> users) {
        return UserResponsesList.builder()
                .userResponses(users.stream().map(this::entityToResponse).collect(Collectors.toList()))
                .build();
    }

}
