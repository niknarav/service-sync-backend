package org.example.servicesyncorderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "parts")
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partId;

    private String name;
    private Integer quantity;
    private BigDecimal price;
    private String supplierInfo;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
