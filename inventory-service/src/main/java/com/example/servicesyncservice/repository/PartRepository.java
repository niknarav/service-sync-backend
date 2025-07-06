package com.example.servicesyncservice.repository;

import com.example.servicesyncservice.model.Part;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartRepository extends JpaRepository<Part, Long> {
    Optional<Part> findByPartNumber(String partNumber);
    Optional<Part> findByName(String name);
    default List<Part> findByQuantityLessThanMinStock() {
        return findAll().stream()
                .filter(p -> p.getQuantity() < p.getMinStock())
                .toList();
    }
}
