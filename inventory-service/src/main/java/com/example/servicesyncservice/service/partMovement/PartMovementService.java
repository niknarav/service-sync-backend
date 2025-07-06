package com.example.servicesyncservice.service.partMovement;

import com.example.servicesyncservice.model.PartMovement;
import com.example.servicesyncservice.type.MovementType;

import java.util.List;

public interface PartMovementService {
    PartMovement recordMovement(Long partId, Integer quantity, MovementType type, String reason);
    List<PartMovement> getMovementByPartId(Long partId);
}
