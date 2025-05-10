package com.example.servicesyncservice.mapper.client;

import com.example.servicesyncservice.dto.client.ClientResponse;
import com.example.servicesyncservice.dto.client.UpsertClientRequest;
import com.example.servicesyncservice.model.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public Client requestToEntity(UpsertClientRequest request) {
        return Client.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .phone(request.getSurname())
                .build();
    }

    public ClientResponse entityToResponse(Client client) {
        return ClientResponse.builder()
                .name(client.getName())
                .surname(client.getSurname())
                .phone(client.getPhone())
                .build();
    }

}
