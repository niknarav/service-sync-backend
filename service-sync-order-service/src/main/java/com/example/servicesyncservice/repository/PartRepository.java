package com.example.servicesyncservice.repository;

import com.example.servicesyncservice.model.Part;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, Long> {
    Part findByName(String name);
}
