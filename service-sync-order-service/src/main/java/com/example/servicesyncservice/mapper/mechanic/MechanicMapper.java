package com.example.servicesyncservice.mapper.mechanic;

import com.example.servicesyncservice.dto.mechanic.MechanicResponse;
import com.example.servicesyncservice.model.Mechanic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MechanicMapper {

    public MechanicResponse entityToResponse(Mechanic mechanic) {
        return MechanicResponse.builder()
                .name(mechanic.getName())
                .surname(mechanic.getSurname())
                .username(mechanic.getUsername())
                .specialization(mechanic.getSpecialization())
                .email(mechanic.getEmail())
                .isAvailable(mechanic.getIsAvailable())
                .tasksId(mechanic.getTasks().stream()
                        .map(t -> t.getId())
                        .toList())
                .build();
    }

}
