package com.example.servicesyncservice.dto.orderPart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertOrderPartRequest {

    private Long orderId;

    private String partName;

    private Integer quantityUsed;

    private Double totalCost;
}
