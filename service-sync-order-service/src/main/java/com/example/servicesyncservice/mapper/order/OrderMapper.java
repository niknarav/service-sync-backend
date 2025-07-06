package com.example.servicesyncservice.mapper.order;

import com.example.servicesyncservice.dto.order.OrderListResponse;
import com.example.servicesyncservice.dto.order.OrderResponse;
import com.example.servicesyncservice.mapper.car.CarMapper;
import com.example.servicesyncservice.mapper.orderPart.OrderPartMapper;
import com.example.servicesyncservice.mapper.task.TaskMapper;
import com.example.servicesyncservice.model.Client;
import com.example.servicesyncservice.model.Order;
import com.example.servicesyncservice.service.client.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final ClientService clientService;
    private final CarMapper carMapper;
    private TaskMapper taskMapper;
    private OrderPartMapper orderPartMapper;

    @Autowired
    public void setTaskMapper(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Autowired
    public void setOrderPartMapper(OrderPartMapper orderPartMapper) {
        this.orderPartMapper = orderPartMapper;
    }

    public OrderResponse entityToResponse(Order order) {
        Client client = clientService.findById(order.getClient().getId());

        return OrderResponse.builder()
                .id(order.getId())
                .clientId(order.getClient().getId())
                .clientName(client.getName())
                .clientSurname(client.getSurname())
                .tasks(taskMapper.entityListToResponseList(order.getTasks()))
                .parts(orderPartMapper.entityListToResponseList(order.getParts()))
                .car(carMapper.entityToResponse(order.getCar()))
                .createdAt(order.getCreatedAt())
                .description(order.getDescription())
                .status(order.getStatus())
                .totalCost(order.getTotalCost())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    public OrderListResponse entityListToOrderResponseList(List<Order> list) {
        return OrderListResponse.builder()
                .list(list.stream()
                        .map(this::entityToResponse)
                        .toList())
                .build();
    }
}