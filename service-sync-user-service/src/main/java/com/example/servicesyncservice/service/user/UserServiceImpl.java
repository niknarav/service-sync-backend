package com.example.servicesyncservice.service.user;

import com.example.servicesyncservice.dto.simple.SimpleResponse;
import com.example.servicesyncservice.dto.user.UpsertUserRequest;
import com.example.servicesyncservice.dto.user.UserResponse;
import com.example.servicesyncservice.dto.user.UserResponsesList;
import com.example.servicesyncservice.entity.RoleType;
import com.example.servicesyncservice.entity.User;
import com.example.servicesyncservice.exception.EntityNotFoundException;
import com.example.servicesyncservice.kafka.producer.UserEventProducer;
import com.example.servicesyncservice.mapper.UserMapper;
import com.example.servicesyncservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserEventProducer userEventProducer;

    @Override
    public SimpleResponse createAdmin() {
        if(userRepository.findByUsername("admin").isEmpty()) {
            User user = User.builder()
                    .username("admin")
                    .name("admin")
                    .surname("admin")
                    .email("admin@admin.admin")
                    .roles(Collections.singleton(RoleType.ROLE_ADMIN))
                    .password(passwordEncoder.encode("admin"))
                    .build();

            User sevedUser = userRepository.save(user);
            userEventProducer.publishUserCreated(sevedUser);
            return new SimpleResponse("Админ создан");
        } else {
            return new SimpleResponse("Админ уже существует");
        }
    }

    @Override
    public UserResponse findUserById(Long id) {
        return userMapper.entityToResponse(userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id: " + id + " не был найден!")));
    }

    @Override
    public UserResponse findUserByUsername(String username) {
        return userMapper.entityToResponse(userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с username: " + username + " не был найден!")));
    }

    @Override
    public UserResponse updateUser(UpsertUserRequest upsertUserRequest, Long id) {
        User existedUser = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Пользователь с id: " + id + " не был найден!"));

        User savedUser = User.builder()
                .id(existedUser.getId())
                .username(upsertUserRequest.getUsername() != null ? upsertUserRequest.getUsername() : existedUser.getUsername())
                .name(upsertUserRequest.getName() != null ? upsertUserRequest.getName() : existedUser.getName())
                .surname(upsertUserRequest.getSurname() != null ? upsertUserRequest.getSurname() : existedUser.getSurname())
                .email(upsertUserRequest.getEmail() != null ? upsertUserRequest.getEmail() : existedUser.getEmail())
                .password(upsertUserRequest.getPassword() != null ? passwordEncoder.encode(upsertUserRequest.getPassword()) : existedUser.getPassword())
                .roles(existedUser.getRoles())
                .build();

        userEventProducer.publishUserUpdated(savedUser);
        return userMapper.entityToResponse(userRepository.save(savedUser));
    }

    @Override
    public void deleteUserById(Long id) {
        userEventProducer.publishUserDeleted(id);
        userRepository.deleteById(id);
    }

    @Override
    public UserResponsesList getAllUsers() {
        return userMapper.entityListToResponsesList(userRepository.findAll());
    }

    @Override
    public UserResponsesList getAllUsersByRole(RoleType role) {
        return userMapper.entityListToResponsesList(userRepository.findAllByRole(role));
    }

    @Override
    public UserResponse updateRole(Long id, RoleType roleType) {
        User currentUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id: " + id + " не был найден!"));

        Set<RoleType> roles = currentUser.getRoles();

        if (roles.contains(RoleType.ROLE_GUEST)) {
            roles.clear();
            roles.add(roleType);
        } else {
            if (!roles.contains(roleType)) {
                roles.add(roleType);
            }
        }

        currentUser.setRoles(roles);
        User updatedUser = userRepository.save(currentUser);
        userEventProducer.publishUserUpdated(updatedUser);

        return userMapper.entityToResponse(updatedUser);
    }
}