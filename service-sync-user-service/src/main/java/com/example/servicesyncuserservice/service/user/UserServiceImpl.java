package com.example.servicesyncuserservice.service.user;

import com.example.servicesyncuserservice.dto.user.UpsertUserRequest;
import com.example.servicesyncuserservice.dto.user.UserResponse;
import com.example.servicesyncuserservice.dto.user.UserResponsesList;
import com.example.servicesyncuserservice.entity.User;
import com.example.servicesyncuserservice.exception.EntityNotFoundException;
import com.example.servicesyncuserservice.kafka.producer.UserEventProducer;
import com.example.servicesyncuserservice.mapper.UserMapper;
import com.example.servicesyncuserservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponsesList getAllUsers() {
        return userMapper.entityListToResponsesList(userRepository.findAll());
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
        User existedUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Пользователь с id: " + id + " не был найден!"));
        BeanUtils.copyProperties(upsertUserRequest, existedUser);
        existedUser.setPassword(passwordEncoder.encode(upsertUserRequest.getPassword()));
        return userMapper.entityToResponse(userRepository.save(existedUser));
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
