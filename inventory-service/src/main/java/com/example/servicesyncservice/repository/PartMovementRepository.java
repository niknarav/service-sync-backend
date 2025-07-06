package com.example.servicesyncservice.repository;

import com.example.servicesyncservice.model.PartMovement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PartMovementRepository extends JpaRepository<PartMovement, Long> {
    List<PartMovement> findByPartId(Long partId);
    List<PartMovement> findByTimestampBetween(LocalDateTime from, LocalDateTime to);
}
