package com.example.servicesyncservice.dto.car;

import com.example.servicesyncservice.dto.client.ClientResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarResponse {

    private ClientResponse client;

    private String make;

    private String model;

    private Integer year;

    private String vin;

    private Integer mileage;

}
