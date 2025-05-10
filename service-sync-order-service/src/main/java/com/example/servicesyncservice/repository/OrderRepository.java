package com.example.servicesyncservice.repository;

import com.example.servicesyncservice.model.Car;
import com.example.servicesyncservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByCar(Car car);

}
