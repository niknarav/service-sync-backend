package com.example.servicesyncservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String partNumber;

    private String category;

    private Integer quantity;

    private Integer minStock;

    private BigDecimal price;

    @Builder.Default
    @OneToMany(mappedBy = "part", cascade = CascadeType.ALL)
    private List<PartMovement> movements = new ArrayList<>();
}
