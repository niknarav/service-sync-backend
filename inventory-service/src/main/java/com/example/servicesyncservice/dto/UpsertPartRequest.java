package com.example.servicesyncservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpsertPartRequest {

    private String name;
    private String description;
    private String partNumber;
    private String category;
    private Integer quantity;
    private Integer minStock;
    private BigDecimal price;

}
