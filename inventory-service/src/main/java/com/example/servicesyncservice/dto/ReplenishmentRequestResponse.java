package com.example.servicesyncservice.dto;

import com.example.servicesyncservice.type.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplenishmentRequestResponse {

    private Long id;

    private String partName;

    private Integer requestedQuantity;

    private RequestStatus status;

    private LocalDateTime requestDate;
}
