package com.example.servicesyncservice.service.part;

import com.example.servicesyncservice.model.Part;
import com.example.servicesyncservice.repository.PartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartService {

    private final PartRepository partRepository;

    public Part savePart(Part part) {
        return partRepository.save(part);
    }

    public Part findPartByName(String name) {
        return partRepository.findByName(name);
    }

}
