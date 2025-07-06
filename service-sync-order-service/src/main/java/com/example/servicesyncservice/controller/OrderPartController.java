package com.example.servicesyncservice.controller;

import com.example.servicesyncservice.dto.orderPart.OrderPartListResponse;
import com.example.servicesyncservice.dto.orderPart.UpsertOrderPartRequest;
import com.example.servicesyncservice.kafka.dto.PartResponseDto;
import com.example.servicesyncservice.kafka.producer.KafkaPartClient;
import com.example.servicesyncservice.mapper.orderPart.OrderPartMapper;
import com.example.servicesyncservice.model.OrderPart;
import com.example.servicesyncservice.service.orderPart.OrderPartService;
import com.example.servicesyncservice.service.part.PartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service-sync/order/order-part")
@RequiredArgsConstructor
public class OrderPartController {

    private final OrderPartService orderPartService;
    private final OrderPartMapper orderPartMapper;
    private final KafkaPartClient kafkaPartClient;
    private final PartService partService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody UpsertOrderPartRequest request) {
        try {
            PartResponseDto partDto;
            if(partService.findPartByName(request.getPartName()) == null) {
                partDto = kafkaPartClient.getPartByName(request.getPartName()).get();
                if (partDto.isError()) {
                    return ResponseEntity.badRequest().body(partDto.getErrorMessage());
                }
            } else {
                partDto = null;
            }
            OrderPart response = orderPartService.createOrderPart(request, partDto);
            return ResponseEntity.ok(orderPartMapper.entityToResponse(response));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @GetMapping("/get/by-order-id")
    public ResponseEntity<OrderPartListResponse> getByOrderId(@RequestParam("orderId") Long orderId) {
        return ResponseEntity.ok(orderPartService.getAllByOrderId(orderId));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        orderPartService.deleteOrderPart(id);
        return ResponseEntity.noContent().build();
    }

}
