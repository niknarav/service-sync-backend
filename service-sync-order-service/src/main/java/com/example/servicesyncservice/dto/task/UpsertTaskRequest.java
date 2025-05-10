package com.example.servicesyncservice.dto.task;

import com.example.servicesyncservice.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertTaskRequest {

    private Long mechanicId;

    private String description;

    private TaskStatus status;

    private Double cost;

}
