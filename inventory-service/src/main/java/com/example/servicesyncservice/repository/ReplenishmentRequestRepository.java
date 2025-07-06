package com.example.servicesyncservice.repository;

import com.example.servicesyncservice.model.ReplenishmentRequest;
import com.example.servicesyncservice.type.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplenishmentRequestRepository extends JpaRepository<ReplenishmentRequest, Long> {
    List<ReplenishmentRequest> findByStatus(RequestStatus status);
}
