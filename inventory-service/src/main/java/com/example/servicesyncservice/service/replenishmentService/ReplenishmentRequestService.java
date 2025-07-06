package com.example.servicesyncservice.service.replenishmentService;

import com.example.servicesyncservice.model.ReplenishmentRequest;

import java.util.List;

public interface ReplenishmentRequestService {
    ReplenishmentRequest createRequest(Long partId, Integer quantity, String comment);
    ReplenishmentRequest createRequest(Long partId, Integer quantity);
    ReplenishmentRequest approveRequest(Long requestId);
    ReplenishmentRequest rejectRequest(Long requestId);
    ReplenishmentRequest findById(Long requestId);
    List<ReplenishmentRequest> getPendingRequests();
}
