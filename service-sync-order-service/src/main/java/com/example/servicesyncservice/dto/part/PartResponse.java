package com.example.servicesyncservice.dto.part;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartResponse {

    private String name;

    private String partNumber;

    private Integer quantity;

    private Double price;

}
