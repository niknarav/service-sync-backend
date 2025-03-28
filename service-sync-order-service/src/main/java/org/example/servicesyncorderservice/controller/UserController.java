package org.example.servicesyncorderservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.servicesyncorderservice.model.User;
import org.example.servicesyncorderservice.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order-service/user/")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/get/all")
    public List<User> users() {
        return userRepository.findAll();
    }

}
