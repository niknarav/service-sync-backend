package com.example.servicesyncuserservice.kafka.event;

import com.example.servicesyncuserservice.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {

    private String eventType;

    private Long userId;

    private String username;

    private String name;

    private String surname;

    private String email;

    private Set<RoleType> roles;

}
