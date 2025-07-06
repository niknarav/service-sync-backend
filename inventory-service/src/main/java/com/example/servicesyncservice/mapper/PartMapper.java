package com.example.servicesyncservice.mapper;

import com.example.servicesyncservice.dto.PartResponse;
import com.example.servicesyncservice.model.Part;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PartMapper {

    private final PartMovementMapper partMovementMapper;

    public PartResponse toPartResponse(Part part) {
        return PartResponse.builder()
                .id(part.getId())
                .name(part.getName())
                .price(part.getPrice())
                .description(part.getDescription())
                .partNumber(part.getPartNumber())
                .category(part.getCategory())
                .quantity(part.getQuantity())
                .minStock(part.getMinStock())
                .movements(part.getMovements().stream().map(partMovementMapper::entityToResponse).collect(Collectors.toList()))
                .build();
    }
}
