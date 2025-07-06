package com.example.servicesyncservice.service.part.impl;

import com.example.servicesyncservice.dto.PartResponse;
import com.example.servicesyncservice.dto.UpsertPartRequest;
import com.example.servicesyncservice.mapper.PartMapper;
import com.example.servicesyncservice.model.Part;
import com.example.servicesyncservice.repository.PartRepository;
import com.example.servicesyncservice.service.part.PartService;
import com.example.servicesyncservice.service.replenishmentService.ReplenishmentRequestService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartServiceImpl implements PartService {

    private final PartRepository partRepository;

    @Autowired
    public void setReplenishmentService(ReplenishmentRequestService replenishmentService) {
        this.replenishmentService = replenishmentService;
    }

    private ReplenishmentRequestService replenishmentService;

    private final PartMapper partMapper;

    @Override
    public Part createPart(UpsertPartRequest request) {
        Part part = new Part();
        updatePartFromRequest(part, request);
        return partRepository.save(part);
    }

    @Override
    public Part updatePart(Long id, UpsertPartRequest request) {
        Part part = findPartById(id);
        updatePartFromRequest(part, request);
        return partRepository.save(part);
    }

    @Override
    public Part findPartById(Long id) {
        return partRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Часть не найдена"));
    }

    @Override
    public void checkStockAndGenerateRequests() {
        List<Part> parts = partRepository.findAll();
        for (Part part : parts) {
            if (part.getQuantity() < part.getMinStock()) {
                replenishmentService.createRequest(part.getId(), Math.max(0, part.getMinStock() - part.getQuantity()));
            }
        }
    }

    @Override
    public void updatePartFromRequest(Part part, UpsertPartRequest request) {
        part.setName(request.getName());
        part.setDescription(request.getDescription());
        part.setPartNumber(request.getPartNumber());
        part.setCategory(request.getCategory());
        part.setQuantity(request.getQuantity());
        part.setMinStock(request.getMinStock());
        part.setPrice(request.getPrice());
    }

    @Override
    public void updatePartFromEntity(Part part) {
        partRepository.save(part);
    }

    @Override
    public void deletePart(Long id) {
        partRepository.deleteById(id);
    }

    @Override
    public List<PartResponse> getAllParts() {
        return partRepository.findAll()
                .stream()
                .map(partMapper::toPartResponse)
                .toList();
    }

    @Override
    public List<PartResponse> getLowStockParts() {
        return partRepository.findByQuantityLessThanMinStock()
                .stream()
                .map(partMapper::toPartResponse)
                .toList();
    }

    @Override
    public Part findPartByName(String name) {
        return partRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("<UNK> <UNK> <UNK>"));
    }

}
