package com.example.servicesyncservice.service.order;

import com.example.servicesyncservice.dto.order.OrderListResponse;
import com.example.servicesyncservice.dto.order.OrderResponse;
import com.example.servicesyncservice.dto.order.UpsertOrderRequest;
import com.example.servicesyncservice.exception.EntityNotFoundException;
import com.example.servicesyncservice.mapper.car.CarMapper;
import com.example.servicesyncservice.mapper.client.ClientMapper;
import com.example.servicesyncservice.mapper.order.OrderMapper;
import com.example.servicesyncservice.model.*;
import com.example.servicesyncservice.repository.OrderRepository;
import com.example.servicesyncservice.service.car.CarService;
import com.example.servicesyncservice.service.client.ClientService;
import com.example.servicesyncservice.service.task.TaskService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final ClientService clientService;

    private final CarService carService;

    private final CarMapper carMapper;

    @Lazy
    private final OrderMapper orderMapper;


    public OrderResponse findOrderByCarVin(String vin) {
        Order order = orderRepository.findByCar(carService.findByVin(vin))
                .orElseThrow(() -> new RuntimeException("Car not found with VIN: " + vin));
        return orderMapper.entityToResponse(order);
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    public OrderListResponse getAll() {
        return orderMapper.entityListToOrderResponseList(orderRepository.findAll());
    }

    @Transactional
    public Order createOrder(UpsertOrderRequest request) {
        Order order = new Order();

        Client client;
        if (!clientService.existsByPhone(request.getClientRequest().getPhone())) {
            client = clientService.createClient(request.getClientRequest());
        } else {
            client = clientService.findByPhone(request.getClientRequest().getPhone());
        }
        order.setClient(client);

        Car car;
        if (!carService.existsByVin(request.getCarRequest().getVin())) {
            Car newCar = carMapper.requestToEntity(request.getCarRequest());
            newCar.setClient(client);
            car = carService.createCar(newCar);
        } else {
            car = carService.findByVin(request.getCarRequest().getVin());
            car.setClient(client);
            carService.updateCar(car);
        }
        order.setCar(car);

        order.setDescription(request.getDescription());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.NEW);

        return orderRepository.save(order);
    }

    public Order updateOrder(UpsertOrderRequest request, Long id) {
        Order order = findOrderById(id);
        BeanUtils.copyProperties(request, order);
        return orderRepository.save(order);
    }

    public Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Заказ по id не найден, id: " + id));
    }

    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }
}
