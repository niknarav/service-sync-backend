package com.example.servicesyncservice.kafka.producer;

import com.example.servicesyncservice.kafka.dto.RecordMovementRequest;
import com.example.servicesyncservice.model.MovementType;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaMovementClient {

    private final KafkaTemplate<String, RecordMovementRequest> kafkaTemplate;

    public void sendMovement(Long partId, Integer quantity, MovementType type, String reason) {
        RecordMovementRequest movementRequest = new RecordMovementRequest();
        movementRequest.setPartId(partId);
        movementRequest.setQuantity(quantity);
        movementRequest.setType(type);
        movementRequest.setReason(reason);

        kafkaTemplate.send("movement-topic", movementRequest);
    }
}
