package com.example.servicesyncservice.dto.part;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertPartRequest {

    private String name;

    private String partNumber;

    private Integer quantity;

    private BigDecimal price;
}
