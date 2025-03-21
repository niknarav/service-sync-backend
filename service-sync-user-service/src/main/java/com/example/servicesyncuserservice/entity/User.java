package com.example.servicesyncuserservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String name;

    private String surname;

    private String patronymic;

    @Enumerated(EnumType.STRING)
    private PostType post;

    private String password;

    public enum PostType {
        ROLE_MANAGER,
        ROLE_MECHANIC,
        ROLE_ADMIN
    }

}