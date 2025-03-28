package com.example.servicesyncuserservice.kafka.producer;

import com.example.servicesyncuserservice.entity.User;
import com.example.servicesyncuserservice.kafka.event.UserEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class UserEventProducer {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public UserEventProducer(KafkaTemplate<String, UserEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishUserCreated(User user) {
        UserEvent event = UserEvent.builder()
                .eventType("USER_CREATED")
                .userId(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();

        CompletableFuture<SendResult<String, UserEvent>> future = kafkaTemplate.send("user-events", user.getId().toString(), event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to send message: {}", ex.getMessage());
            } else {
                log.info("Message sent successfully: topic={}, partition={}, offset={}, event={}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset(),
                        event);
            }
        });
    }

    public void publishUserUpdated(User user) {
        UserEvent event = UserEvent.builder()
                .eventType("USER_UPDATED")
                .userId(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
        kafkaTemplate.send("user-events", user.getId().toString(), event);
    }

    public void publishUserDeleted(Long userId) {
        UserEvent event = UserEvent.builder()
                .eventType("USER_DELETED")
                .userId(userId)
                .build();
        kafkaTemplate.send("user-events", userId.toString(), event);
    }
}
