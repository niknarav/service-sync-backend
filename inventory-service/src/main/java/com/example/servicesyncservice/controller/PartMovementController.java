package com.example.servicesyncservice.controller;

import com.example.servicesyncservice.dto.PartMovementResponse;
import com.example.servicesyncservice.dto.RecordMovementRequest;
import com.example.servicesyncservice.mapper.PartMovementMapper;
import com.example.servicesyncservice.model.PartMovement;
import com.example.servicesyncservice.service.partMovement.PartMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service-sync/inventory/movement")
@RequiredArgsConstructor
public class PartMovementController {

    private final PartMovementService movementService;

    private final PartMovementMapper partMovementMapper;

    @PostMapping("/record")
    public ResponseEntity<PartMovementResponse> recordMovement(@RequestBody RecordMovementRequest request) {
        PartMovement movement = movementService.recordMovement(
                request.getPartId(),
                request.getQuantity(),
                request.getType(),
                request.getReason());

        return ResponseEntity.status(HttpStatus.CREATED).body(partMovementMapper.entityToResponse(movement));
    }

    @GetMapping("/history/{partId}")
    public ResponseEntity<List<PartMovementResponse>> getMovementHistory(@PathVariable Long partId) {
        return ResponseEntity.ok(movementService.getMovementByPartId(partId).stream().map(partMovementMapper::entityToResponse).toList());
    }
}
