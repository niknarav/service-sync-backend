package com.example.servicesyncservice.service.replenishmentService.impl;

import com.example.servicesyncservice.model.Part;
import com.example.servicesyncservice.model.ReplenishmentRequest;
import com.example.servicesyncservice.repository.ReplenishmentRequestRepository;
import com.example.servicesyncservice.service.part.PartService;
import com.example.servicesyncservice.service.replenishmentService.ReplenishmentRequestService;
import com.example.servicesyncservice.type.RequestStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplenishmentRequestServiceImpl implements ReplenishmentRequestService {

    private final ReplenishmentRequestRepository replenishmentRepository;

    private PartService partService;

    @Autowired
    public void setPartService(PartService partService) {
        this.partService = partService;
    }

    public ReplenishmentRequest createRequest(Long partId, Integer quantity, String comment) {
        Part part = partService.findPartById(partId);
        return replenishmentRepository.save(ReplenishmentRequest.builder()
                .part(part)
                .requestedQuantity(quantity)
                .status(RequestStatus.PENDING)
                .requestDate(LocalDateTime.now())
                .build());
    }

    public ReplenishmentRequest createRequest(Long partId, Integer requestedQuantity) {
        return createRequest(partId, requestedQuantity, "Автоматическая заявка");
    }

    public ReplenishmentRequest approveRequest(Long requestId) {
        ReplenishmentRequest request = findById(requestId);
        request.setStatus(RequestStatus.APPROVED);
        Part part = request.getPart();
        part.setQuantity(part.getQuantity() + request.getRequestedQuantity());
        partService.updatePartFromEntity(part);
        return replenishmentRepository.save(request);
    }

    public ReplenishmentRequest rejectRequest(Long requestId) {
        ReplenishmentRequest request = findById(requestId);
        request.setStatus(RequestStatus.REJECTED);
        return replenishmentRepository.save(request);
    }

    public ReplenishmentRequest findById(Long requestId) {
        return replenishmentRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Заявка не найдена"));
    }

    @Override
    public List<ReplenishmentRequest> getPendingRequests() {
        return replenishmentRepository.findByStatus(RequestStatus.PENDING);
    }
}
