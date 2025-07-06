package com.example.servicesyncservice.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartResponseDto {
    private String requestId;

    private Long id;
    private String name;
    private Integer quantity;
    private BigDecimal price;

    private boolean error;
    private String errorMessage;
}
