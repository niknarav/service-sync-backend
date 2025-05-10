package com.example.servicesyncservice.repository;

import com.example.servicesyncservice.model.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MechanicRepository extends JpaRepository<Mechanic, Long> {
}
