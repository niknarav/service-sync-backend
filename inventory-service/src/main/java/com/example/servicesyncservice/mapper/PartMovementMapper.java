package com.example.servicesyncservice.mapper;

import com.example.servicesyncservice.dto.PartMovementResponse;
import com.example.servicesyncservice.model.PartMovement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PartMovementMapper {

    public PartMovementResponse entityToResponse(PartMovement partMovement) {
        return PartMovementResponse.builder()
                .id(partMovement.getId())
                .partName(partMovement.getPart().getName())
                .partNumber(partMovement.getPart().getPartNumber())
                .movementType(partMovement.getType())
                .quantity(partMovement.getQuantity())
                .timestamp(partMovement.getTimestamp())
                .reason(partMovement.getReason())
                .build();
    }

}
