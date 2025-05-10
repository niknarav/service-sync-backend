package com.example.servicesyncservice.mapper.orderPart;

import com.example.servicesyncservice.dto.orderPart.OrderPartListResponse;
import com.example.servicesyncservice.dto.orderPart.OrderPartResponse;
import com.example.servicesyncservice.dto.orderPart.UpsertOrderPartRequest;
import com.example.servicesyncservice.exception.EntityNotFoundException;
import com.example.servicesyncservice.mapper.order.OrderMapper;
import com.example.servicesyncservice.mapper.part.PartMapper;
import com.example.servicesyncservice.model.OrderPart;
import com.example.servicesyncservice.repository.OrderRepository;
import com.example.servicesyncservice.service.order.OrderService;
import com.example.servicesyncservice.service.part.PartService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderPartMapper {

    private final PartService partService;

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final PartMapper partMapper;

    public OrderPart requestToEntity(UpsertOrderPartRequest request) {
        return OrderPart.builder()
                .order(orderRepository.findById(request.getOrderId()).orElseThrow(() ->
                                new EntityNotFoundException("Заказ по id не найден, id: " + request.getOrderId())))
                .part(partService.findPartByName(request.getPartName()))
                .quantityUsed(request.getQuantityUsed())
                .totalCost(request.getTotalCost())
                .build();
    }

    public OrderPartResponse entityToResponse(OrderPart orderPart) {
        return OrderPartResponse.builder()
                .order(orderMapper.entityToResponse(orderPart.getOrder()))
                .part(partMapper.entityToResponse(orderPart.getPart()))
                .quantityUsed(orderPart.getQuantityUsed())
                .totalCost(orderPart.getTotalCost())
                .build();
    }

    public OrderPartListResponse entityListToResponseList(List<OrderPart> ordersParts) {
        return OrderPartListResponse.builder()
                .list(ordersParts.stream().map(this::entityToResponse).toList())
                .build();
    }

}
