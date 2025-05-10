package com.example.servicesyncservice.service.user;

import com.example.servicesyncservice.dto.user.UpsertUserRequest;
import com.example.servicesyncservice.dto.user.UserResponse;
import com.example.servicesyncservice.dto.user.UserResponsesList;
import com.example.servicesyncservice.entity.RoleType;
import com.example.servicesyncservice.entity.User;
import com.example.servicesyncservice.exception.EntityNotFoundException;
import com.example.servicesyncservice.mapper.UserMapper;
import com.example.servicesyncservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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

        return userMapper.entityToResponse(updatedUser);
    }
}
