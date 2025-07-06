package com.example.servicesyncservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mechanics")
public class Mechanic {

    @Id
    private Long id;

    @Version
    private Integer version;

    private String name;

    private String surname;

    private String username;

    private String email;

    @OneToMany(mappedBy = "mechanic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

}
