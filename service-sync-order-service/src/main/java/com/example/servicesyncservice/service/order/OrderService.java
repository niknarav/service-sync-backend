package com.example.servicesyncservice.service.order;

import com.example.servicesyncservice.dto.car.UpsertCarRequest;
import com.example.servicesyncservice.dto.client.UpsertClientRequest;
import com.example.servicesyncservice.dto.order.*;
import com.example.servicesyncservice.dto.orderStatus.OrderStatusRequest;
import com.example.servicesyncservice.dto.status.StatusRequest;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

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

    public OrderListResponse getAll() {
        return orderMapper.entityListToOrderResponseList(orderRepository.findAll());
    }

    public OrderListResponse getAllByStatus(OrderStatusRequest status) {
        return orderMapper.entityListToOrderResponseList(orderRepository.getAllByStatus(status.getStatus()));
    }

    @Transactional
    public Order createOrder(UpsertOrderRequest request) {
        Order order = new Order();

        Client client;
        if (!clientService.existsByPhone(request.getClient().getPhone())) {
            client = clientService.createClient(request.getClient());
        } else {
            client = clientService.findByPhone(request.getClient().getPhone());
        }
        order.setClient(client);

        Car car;
        if (!carService.existsByVin(request.getCar().getVin())) {
            Car newCar = carMapper.requestToEntity(request.getCar());
            newCar.setClient(client);
            car = carService.createCar(newCar);
        } else {
            car = carService.findByVin(request.getCar().getVin());
            car.setClient(client);
            carService.updateCar(car);
        }
        order.setCar(car);

        order.setDescription(request.getDescription());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.NEW);

        Long randomStationId = (long) ThreadLocalRandom.current().nextInt(1, 5); // [1..4]
        order.setStationId(randomStationId);

        BigDecimal totalCost = order.getParts().stream()
                .map(OrderPart::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalCost(totalCost);

        return orderRepository.save(order);
    }

    public Order updateOrder(UpsertOrderRequest request, Long id) {
        Order existingOrder = findOrderById(id);

        UpsertClientRequest requestClient = request.getClient();
        Client client = existingOrder.getClient();


        if (requestClient != null && client != null) {
            boolean clientHasChanges = !Objects.equals(requestClient.getName(), client.getName()) ||
                    !Objects.equals(requestClient.getSurname(), client.getSurname()) ||
                    !Objects.equals(requestClient.getPhone(), client.getPhone());

            if (clientHasChanges) {
                Client updatedClient = clientService.updateClient(client.getId(), requestClient);
                existingOrder.setClient(updatedClient);
            }
        }

        UpsertCarRequest requestCar = request.getCar();
        Car car = existingOrder.getCar();

        if (requestCar != null && car != null) {
            boolean carHasChanges = !Objects.equals(requestCar.getMake(), car.getMake()) ||
                    !Objects.equals(requestCar.getModel(), car.getModel()) ||
                    !Objects.equals(requestCar.getYear(), car.getYear()) ||
                    !Objects.equals(requestCar.getVin(), car.getVin()) ||
                    !Objects.equals(requestCar.getMileage(), car.getMileage());

            if (carHasChanges) {
                Car updatedCar = carService.updateCar(requestCar, car.getId());
                existingOrder.setCar(updatedCar);
            }
        }

        if (request.getDescription() != null) {
            existingOrder.setDescription(request.getDescription());
        }

        return orderRepository.save(existingOrder);
    }



    public Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Заказ по id не найден, id: " + id));
    }

    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

}
