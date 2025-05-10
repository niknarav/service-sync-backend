package com.example.servicesyncservice.mapper.part;

import com.example.servicesyncservice.dto.part.PartResponse;
import com.example.servicesyncservice.dto.part.UpsertPartRequest;
import com.example.servicesyncservice.model.Part;
import org.springframework.stereotype.Component;

@Component
public class PartMapper {

    public Part requestToEntity(UpsertPartRequest request) {
        return Part.builder()
                .name(request.getName())
                .partNumber(request.getPartNumber())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .build();
    }

    public PartResponse entityToResponse(Part part) {
        return PartResponse.builder()
                .name(part.getName())
                .partNumber(part.getPartNumber())
                .quantity(part.getQuantity())
                .price(part.getPrice())
                .build();
    }
}
