package com.example.servicesyncservice.kafka.dto;

import com.example.servicesyncservice.model.MovementType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordMovementRequest {
    private Long partId;
    private Integer quantity;
    private MovementType type;
    private String reason;
}