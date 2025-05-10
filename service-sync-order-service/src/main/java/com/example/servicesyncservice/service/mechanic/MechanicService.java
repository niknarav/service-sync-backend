package com.example.servicesyncservice.service.mechanic;

import com.example.servicesyncservice.exception.EntityNotFoundException;
import com.example.servicesyncservice.model.Mechanic;
import com.example.servicesyncservice.repository.MechanicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MechanicService {

    private final MechanicRepository mechanicRepository;

    public Mechanic findMechanicById(Long id) {
        return mechanicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Механик по id не найден, id: " + id));
    }

}
