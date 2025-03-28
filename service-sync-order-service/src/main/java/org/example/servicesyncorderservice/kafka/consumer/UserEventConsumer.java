package org.example.servicesyncorderservice.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.servicesyncorderservice.kafka.event.UserEvent;
import org.example.servicesyncorderservice.model.User;
import org.example.servicesyncorderservice.repository.UserRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserEventConsumer {

    private final UserRepository userRepository;

    @KafkaListener(topics = "user-events", groupId = "order-service-group")
    public void consumeUserEvent(UserEvent event) {
        if (event == null) {
            log.error("Received null event");
            return;
        }
        log.info("Received event: {}", event);;
        switch (event.getEventType()) {
            case "USER_CREATED":
                createUser(event);
                break;
            case "USER_UPDATED":
                updateUser(event);
                break;
            case "USER_DELETED":
                deleteUser(event.getUserId());
                break;
            default:
                throw new IllegalArgumentException("Unknown event type: " + event.getEventType());
        }
    }

    private void createUser(UserEvent event) {
        User user = User.builder()
                .userId(event.getUserId())
                .username(event.getUsername())
                .name(event.getName())
                .surname(event.getSurname())
                .email(event.getEmail())
                .roles(event.getRoles())
                .build();
        userRepository.save(user);
    }

    private void updateUser(UserEvent event) {
        User user = userRepository.findById(event.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(event.getUsername());
        user.setName(event.getName());
        user.setSurname(event.getSurname());
        user.setEmail(event.getEmail());
        user.setRoles(event.getRoles());
        userRepository.save(user);
    }

    private void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
