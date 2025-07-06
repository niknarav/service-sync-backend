package com.example.servicesyncservice.kafka.producer;

import com.example.servicesyncservice.kafka.dto.PartRequestDto;
import com.example.servicesyncservice.kafka.dto.PartResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class KafkaPartClient {

    private final KafkaTemplate<String, PartRequestDto> kafkaTemplate;

    private final Map<String, CompletableFuture<PartResponseDto>> requests = new ConcurrentHashMap<>();

    public CompletableFuture<PartResponseDto> getPartByName(String partName) {
        String requestId = UUID.randomUUID().toString();
        PartRequestDto request = new PartRequestDto(partName, requestId);

        requests.put(requestId, new CompletableFuture<>());
        kafkaTemplate.send("request.part.by-name", request);
        return requests.get(requestId);
    }

    @KafkaListener(
            topics = "response.part.by-name",
            groupId = "order-group",
            containerFactory = "partResponseKafkaListenerContainerFactory"
    )
    public void handlePartResponse(PartResponseDto response) {
        CompletableFuture<PartResponseDto> future = requests.remove(response.getRequestId());
        if (future != null) {
            if (response.isError()) {
                future.completeExceptionally(new RuntimeException(response.getErrorMessage()));
            } else {
                future.complete(response);
            }
        }
    }
}
