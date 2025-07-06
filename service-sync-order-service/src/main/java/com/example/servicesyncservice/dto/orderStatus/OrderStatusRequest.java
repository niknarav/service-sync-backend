package com.example.servicesyncservice.dto.orderStatus;

import com.example.servicesyncservice.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderStatusRequest {
    private OrderStatus status;
}
