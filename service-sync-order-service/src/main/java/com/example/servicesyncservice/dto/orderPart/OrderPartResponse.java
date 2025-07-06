package com.example.servicesyncservice.dto.orderPart;

import com.example.servicesyncservice.dto.order.OrderResponse;
import com.example.servicesyncservice.dto.part.PartResponse;
import com.example.servicesyncservice.model.Part;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderPartResponse {

    private Long orderId;

    private PartResponse part;

    private Integer quantityUsed;

    private BigDecimal totalCost;

}
