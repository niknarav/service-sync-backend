package com.example.servicesyncservice.repository;

import com.example.servicesyncservice.model.Order;
import com.example.servicesyncservice.model.OrderPart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderPartRepository extends JpaRepository<OrderPart, Long> {
    List<OrderPart> findAllByOrder(Order order);
}
