package com.example.servicesyncservice.controller;

import com.example.servicesyncservice.dto.order.*;
import com.example.servicesyncservice.dto.orderStatus.OrderStatusRequest;
import com.example.servicesyncservice.dto.simple.SimpleRequest;
import com.example.servicesyncservice.dto.vin.VinRequest;
import com.example.servicesyncservice.mapper.order.OrderMapper;
import com.example.servicesyncservice.model.Order;
import com.example.servicesyncservice.model.OrderStatus;
import com.example.servicesyncservice.repository.OrderRepository;
import com.example.servicesyncservice.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/service-sync/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final OrderRepository orderRepository;

    @Lazy
    private final OrderMapper orderMapper;

    @GetMapping("/get/all")
    public ResponseEntity<OrderListResponse> getAllOrders() {
        return ResponseEntity.ok(orderService.getAll());
    }


    @GetMapping("/get/by-id/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderMapper.entityToResponse(orderService.findOrderById(id)));
    }

    @GetMapping("/get/by/status")
    public ResponseEntity<OrderListResponse> getByStatus(@RequestParam OrderStatus status) {
        OrderStatusRequest request = new OrderStatusRequest(status);
        return ResponseEntity.ok(orderService.getAllByStatus(request));
    }

    @PutMapping("/set/status/{id}")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long id, @RequestBody OrderStatusRequest orderStatusRequest) {
        OrderStatus orderStatus = orderStatusRequest.getStatus();
        Order existedOrder = orderService.findOrderById(id);
        existedOrder.setStatus(orderStatus);
        Order order;
        if(orderStatus == OrderStatus.COMPLETED) {
            existedOrder.setUpdatedAt(LocalDateTime.now());
            order = orderRepository.save(existedOrder);
            System.out.println("hui");
        } else {
            order = orderRepository.save(existedOrder);
        }
        return ResponseEntity.ok(orderMapper.entityToResponse(order));
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable Long id, @RequestBody UpsertOrderRequest request) {
        return ResponseEntity.ok(orderMapper.entityToResponse(orderService.updateOrder(request, id)));
    }

    @GetMapping("/get/by-car-vin")
    public ResponseEntity<OrderResponse> getOrderByCarVin(@RequestBody VinRequest vin) {
        return ResponseEntity.ok(orderService.findOrderByCarVin(vin.getVin()));
    }

    @PostMapping("/create-order")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody UpsertOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderMapper.entityToResponse(orderService.createOrder(request)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SimpleRequest> delete(@PathVariable Long id) {
        return ResponseEntity.ok(new SimpleRequest(MessageFormat.format("Заказ с id: {0} был успешно удален", id)));
    }

}
