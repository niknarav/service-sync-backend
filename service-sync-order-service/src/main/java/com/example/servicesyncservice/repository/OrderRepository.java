package com.example.servicesyncservice.repository;

import com.example.servicesyncservice.model.Car;
import com.example.servicesyncservice.model.Order;
import com.example.servicesyncservice.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByCar(Car car);

    List<Order> getAllByStatus(OrderStatus status);

}
