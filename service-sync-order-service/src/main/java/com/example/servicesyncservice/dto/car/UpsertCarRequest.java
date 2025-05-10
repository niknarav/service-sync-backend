package com.example.servicesyncservice.dto.car;

import com.example.servicesyncservice.dto.client.UpsertClientRequest;
import com.example.servicesyncservice.model.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertCarRequest {

    private String make;

    private String model;

    private Integer year;

    private String vin;

    private Integer mileage;

}
