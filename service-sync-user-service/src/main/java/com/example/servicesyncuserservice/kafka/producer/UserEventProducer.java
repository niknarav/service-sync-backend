package com.example.servicesyncuserservice.kafka.producer;

import com.example.servicesyncuserservice.entity.User;
import com.example.servicesyncuserservice.kafka.event.UserEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
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
        kafkaTemplate.send("user-events", user.getId().toString(), event);
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
