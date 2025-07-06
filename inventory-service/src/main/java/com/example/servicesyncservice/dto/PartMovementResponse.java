package com.example.servicesyncservice.dto;

import com.example.servicesyncservice.type.MovementType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartMovementResponse {

    private Long id;

    private String partName;

    private String partNumber;

    private MovementType movementType;

    private Integer quantity;

    private LocalDateTime timestamp;

    private String reason;

}
