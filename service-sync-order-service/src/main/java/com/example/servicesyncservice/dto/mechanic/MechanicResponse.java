package com.example.servicesyncservice.dto.mechanic;

import com.example.servicesyncservice.dto.task.TaskResponsesList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MechanicResponse {

    private Long id;

    private String name;

    private String surname;

    private String username;

    private String email;

    private List<Long> tasksId;

}
