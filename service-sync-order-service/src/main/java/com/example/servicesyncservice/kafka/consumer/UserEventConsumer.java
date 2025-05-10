package com.example.servicesyncservice.kafka.consumer;

import com.example.servicesyncservice.model.Mechanic;
import com.example.servicesyncservice.model.RoleType;
import com.example.servicesyncservice.repository.MechanicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.servicesyncservice.kafka.event.UserEvent;
import com.example.servicesyncservice.model.User;
import com.example.servicesyncservice.repository.UserRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserEventConsumer {

    private final UserRepository userRepository;

    private final MechanicRepository mechanicRepository;

    @KafkaListener(topics = "user-events", groupId = "order-service-group")
    public void consumeUserEvent(UserEvent event) {
        if (event == null) {
            log.error("Получено null событие");
            return;
        }
        try {
            log.info("Получено событие: {}", event);
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
                    throw new IllegalArgumentException("Неизвестный тип события: " + event.getEventType());
            }
        } catch (Exception e) {
            log.error("Ошибка при обработке события: {}", event, e);
        }
    }

    private void createUser(UserEvent event) {
        if (event.getRoles().stream()
                .anyMatch(role -> role.getValue().equals("ROLE_MECHANIC"))) {
            Mechanic mechanic = Mechanic.builder()
                    .email(event.getEmail())
                    .name(event.getName())
                    .surname(event.getSurname())
                    .username(event.getUsername())
                    .build();
            mechanicRepository.save(mechanic);
        }
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
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
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