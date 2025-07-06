package com.example.servicesyncservice.kafka.listener;

import com.example.servicesyncservice.dto.RecordMovementRequest;
import com.example.servicesyncservice.service.partMovement.PartMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovementConsumer {

    private final PartMovementService movementService;

    @KafkaListener(topics = "movement-topic", groupId = "group1")
    public void consume(RecordMovementRequest request) {
        movementService.recordMovement(
                request.getPartId(),
                request.getQuantity(),
                request.getType(),
                request.getReason()
        );
    }
}
