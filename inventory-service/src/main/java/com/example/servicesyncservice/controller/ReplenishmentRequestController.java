package com.example.servicesyncservice.controller;

import com.example.servicesyncservice.dto.CreateReplenishmentRequest;
import com.example.servicesyncservice.dto.ReplenishmentRequestResponse;
import com.example.servicesyncservice.mapper.ReplenishmentMapper;
import com.example.servicesyncservice.service.replenishmentService.ReplenishmentRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service-sync/inventory/replenish")
@RequiredArgsConstructor
public class ReplenishmentRequestController {

    private final ReplenishmentRequestService replenishmentService;

    private final ReplenishmentMapper replenishmentMapper;

    @PostMapping("/create")
    public ResponseEntity<ReplenishmentRequestResponse> createRequest(
            @RequestBody CreateReplenishmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(replenishmentMapper.entityToResponse(replenishmentService.createRequest(request.getPartId(),
                        request.getRequestedQuantity(), request.getComment())));
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<ReplenishmentRequestResponse> approveRequest(@PathVariable Long id) {
        return ResponseEntity.ok(replenishmentMapper.entityToResponse(replenishmentService.approveRequest(id)));
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<ReplenishmentRequestResponse> rejectRequest(@PathVariable Long id) {
        return ResponseEntity.ok(replenishmentMapper.entityToResponse(replenishmentService.rejectRequest(id)));
    }

    @GetMapping("/get/pending")
    public ResponseEntity<List<ReplenishmentRequestResponse>> getPendingRequests() {
        return ResponseEntity.ok(replenishmentService.getPendingRequests().stream().map(replenishmentMapper::entityToResponse).toList());
    }
}
