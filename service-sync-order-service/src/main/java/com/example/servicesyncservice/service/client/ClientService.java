package com.example.servicesyncservice.service.client;

import com.example.servicesyncservice.dto.client.UpsertClientRequest;
import com.example.servicesyncservice.exception.EntityNotFoundException;
import com.example.servicesyncservice.mapper.client.ClientMapper;
import com.example.servicesyncservice.model.Client;
import com.example.servicesyncservice.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    public Client createClient(UpsertClientRequest request) {
        return clientRepository.save(clientMapper.requestToEntity(request));
    }

    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь по id не найден. Id: " + id));
    }

    public boolean existsByPhone(String phone) {
        return clientRepository.existsByPhone(phone);
    }

    public Client findByPhone(String phone) {
        return clientRepository.findByPhone(phone).
                orElseThrow(() -> new EntityNotFoundException("Пользователь по номеру телефона не найден. Id: " + phone));
    }

}
