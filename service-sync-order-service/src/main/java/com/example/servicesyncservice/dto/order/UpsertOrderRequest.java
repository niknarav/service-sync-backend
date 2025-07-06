package com.example.servicesyncservice.dto.order;

import com.example.servicesyncservice.dto.car.UpsertCarRequest;
import com.example.servicesyncservice.dto.client.UpsertClientRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertOrderRequest {

    private UpsertClientRequest client;

    private UpsertCarRequest car;

    private String description;

}
