package com.example.servicesyncservice.dto.user;

import com.example.servicesyncservice.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse implements Serializable {

    private Long id;

    private String username;

    private String name;

    private String surname;

    private Set<RoleType> roles;

}
