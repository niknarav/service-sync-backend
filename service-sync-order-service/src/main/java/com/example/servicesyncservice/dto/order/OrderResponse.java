package com.example.servicesyncservice.dto.order;

import com.example.servicesyncservice.dto.car.CarResponse;
import com.example.servicesyncservice.dto.client.ClientResponse;
import com.example.servicesyncservice.dto.orderPart.OrderPartListResponse;
import com.example.servicesyncservice.dto.task.TaskResponsesList;
import com.example.servicesyncservice.model.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {

    private Long clientId;

    private String clientName;

    private String clientSurname;

    private CarResponse car;

    private OrderStatus status;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    private Double totalCost;

    private TaskResponsesList tasks;

    private OrderPartListResponse parts;

}
