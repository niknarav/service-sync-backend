package com.example.servicesyncservice.dto;

import com.example.servicesyncservice.type.MovementType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordMovementRequest {

    private Long partId;

    private Integer quantity;

    private MovementType type;

    private String reason;
}
