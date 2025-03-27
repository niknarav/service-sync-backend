package org.example.servicesyncorderservice.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.servicesyncorderservice.model.RoleType;

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
