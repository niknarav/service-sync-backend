package com.example.servicesyncservice.service.part;

import com.example.servicesyncservice.dto.PartResponse;
import com.example.servicesyncservice.dto.UpsertPartRequest;
import com.example.servicesyncservice.model.Part;

import java.util.List;

public interface PartService {
    Part createPart(UpsertPartRequest request);
    Part updatePart(Long id, UpsertPartRequest request);
    Part findPartById(Long id);
    void checkStockAndGenerateRequests();
    void updatePartFromRequest(Part part, UpsertPartRequest request);
    void updatePartFromEntity(Part part);
    void deletePart(Long id);
    List<PartResponse> getAllParts();
    List<PartResponse> getLowStockParts();
    Part findPartByName(String name);
}
