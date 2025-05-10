package com.example.servicesyncservice.mapper;

import com.example.servicesyncservice.dto.user.UpsertUserRequest;
import com.example.servicesyncservice.dto.user.UserResponse;
import com.example.servicesyncservice.dto.user.UserResponsesList;
import com.example.servicesyncservice.entity.User;
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
                .password(request.getPassword())
                .build();
    }

    public UserResponse entityToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId() )
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
