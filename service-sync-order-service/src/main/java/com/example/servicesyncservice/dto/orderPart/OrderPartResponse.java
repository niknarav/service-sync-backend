package com.example.servicesyncservice.dto.orderPart;

import com.example.servicesyncservice.dto.order.OrderResponse;
import com.example.servicesyncservice.dto.part.PartResponse;
import com.example.servicesyncservice.model.Part;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderPartResponse {

    private OrderResponse order;

    private PartResponse part;

    private Integer quantityUsed;

    private Double totalCost;

}
