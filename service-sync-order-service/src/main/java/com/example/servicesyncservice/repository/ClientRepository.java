package com.example.servicesyncservice.repository;

import com.example.servicesyncservice.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByPhone(String phone);

    Optional<Client> findByPhone(String phone);
}
