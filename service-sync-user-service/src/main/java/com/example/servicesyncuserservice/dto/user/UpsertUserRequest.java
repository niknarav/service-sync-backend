package com.example.servicesyncuserservice.dto.user;

import com.example.servicesyncuserservice.entity.RoleType;
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

    private Set<RoleType> roles;

    private String password;

}
