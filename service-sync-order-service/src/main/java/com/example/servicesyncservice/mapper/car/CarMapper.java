package com.example.servicesyncservice.mapper.car;

import com.example.servicesyncservice.dto.car.CarResponse;
import com.example.servicesyncservice.dto.car.UpsertCarRequest;
import com.example.servicesyncservice.mapper.client.ClientMapper;
import com.example.servicesyncservice.model.Car;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarMapper {

    private final ClientMapper clientMapper;

    public Car requestToEntity(UpsertCarRequest request) {
        return Car.builder()
                .make(request.getMake())
                .model(request.getModel())
                .year(request.getYear())
                .vin(request.getVin())
                .mileage(request.getMileage())
                .build();
    }

    public CarResponse entityToResponse(Car car) {
        return CarResponse.builder()
                .client(clientMapper.entityToResponse(car.getClient()))
                .make(car.getMake())
                .model(car.getModel())
                .year(car.getYear())
                .vin(car.getVin())
                .mileage(car.getMileage())
                .build();
    }

}
