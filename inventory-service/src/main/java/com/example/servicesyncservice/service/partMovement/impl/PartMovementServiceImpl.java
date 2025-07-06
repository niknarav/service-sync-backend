package com.example.servicesyncservice.service.partMovement.impl;

import com.example.servicesyncservice.model.Part;
import com.example.servicesyncservice.model.PartMovement;
import com.example.servicesyncservice.repository.PartMovementRepository;
import com.example.servicesyncservice.service.part.PartService;
import com.example.servicesyncservice.service.partMovement.PartMovementService;
import com.example.servicesyncservice.type.MovementType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PartMovementServiceImpl implements PartMovementService {

    private final PartMovementRepository movementRepository;
    private final PartService partService;

    public PartMovement recordMovement(Long partId, Integer quantity, MovementType type, String reason) {
        Part part = partService.findPartById(partId);

        switch (type) {
            case INCOME:
                part.setQuantity(part.getQuantity() + quantity);
                break;
            case OUTCOME:
                if (part.getQuantity() < quantity) throw new IllegalArgumentException("Недостаточно товара");
                part.setQuantity(part.getQuantity() - quantity);
                break;
            case WRITE_OFF:
                part.setQuantity(part.getQuantity() - quantity);
                break;
        }

        PartMovement movement = PartMovement.builder()
                .part(part)
                .type(type)
                .quantity(quantity)
                .reason(reason)
                .timestamp(LocalDateTime.now())
                .build();

        partService.updatePartFromEntity(part);
        return movementRepository.save(movement);
    }

    @Override
    public List<PartMovement> getMovementByPartId(Long partId) {
        return movementRepository.findByPartId(partId);
    }
}
