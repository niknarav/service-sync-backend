package com.example.servicesyncservice.controller;

import com.example.servicesyncservice.dto.PartResponse;
import com.example.servicesyncservice.dto.UpsertPartRequest;
import com.example.servicesyncservice.mapper.PartMapper;
import com.example.servicesyncservice.repository.PartRepository;
import com.example.servicesyncservice.service.part.PartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service-sync/inventory/part")
@RequiredArgsConstructor
public class PartController {

    private final PartService partService;

    private final PartMapper partMapper;

    private final PartRepository partRepository;

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<PartResponse> getPart(@PathVariable Long id) {
        return ResponseEntity.ok(partMapper.toPartResponse(partService.findPartById(id)));
    }

    @GetMapping("/all")
    public ResponseEntity<List<PartResponse>> getAllParts() {
        return ResponseEntity.ok(partService.getAllParts());
    }

    @PostMapping("/create")
    public ResponseEntity<PartResponse> createPart(@RequestBody UpsertPartRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(partMapper.toPartResponse(partService.createPart(request)));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PartResponse> updatePart(@PathVariable Long id, @RequestBody UpsertPartRequest request) {
        return ResponseEntity.ok(partMapper.toPartResponse(partService.updatePart(id, request)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePart(@PathVariable Long id) {
        partService.deletePart(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<PartResponse>> getLowStockParts() {
        return ResponseEntity.ok(partService.getLowStockParts());
    }
}
