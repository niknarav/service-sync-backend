package com.example.servicesyncservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateReplenishmentRequest {

    private Long partId;
    private Integer requestedQuantity;
    private String comment;

}
