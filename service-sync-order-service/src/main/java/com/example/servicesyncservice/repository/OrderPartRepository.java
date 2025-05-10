package com.example.servicesyncservice.repository;

import com.example.servicesyncservice.model.OrderPart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderPartRepository extends JpaRepository<OrderPart, Long> {
}
