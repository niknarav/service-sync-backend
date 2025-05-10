package com.example.servicesyncservice.dto.user;

import com.example.servicesyncservice.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertUserRequest {

    private String email;

    private String username;

    private String name;

    private String surname;

    private String password;

}
