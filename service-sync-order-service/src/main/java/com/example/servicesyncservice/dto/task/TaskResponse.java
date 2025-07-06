package com.example.servicesyncservice.dto.task;

import com.example.servicesyncservice.dto.mechanic.MechanicResponse;
import com.example.servicesyncservice.model.Mechanic;
import com.example.servicesyncservice.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskResponse {

    private Long id;

    private Long orderId;

    private MechanicResponse mechanic;

    private String description;

    private TaskStatus status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Double cost;

    
}
