package com.example.servicesyncservice.mapper;

import com.example.servicesyncservice.dto.ReplenishmentRequestResponse;
import com.example.servicesyncservice.model.ReplenishmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReplenishmentMapper {

    public ReplenishmentRequestResponse entityToResponse(ReplenishmentRequest replenishmentRequest) {
        return ReplenishmentRequestResponse.builder()
                .id(replenishmentRequest.getId())
                .partName(replenishmentRequest.getPart().getName())
                .requestedQuantity(replenishmentRequest.getRequestedQuantity())
                .status(replenishmentRequest.getStatus())
                .requestDate(replenishmentRequest.getRequestDate())
                .build();
    }

}
