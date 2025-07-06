package com.example.servicesyncservice.service.car;

import com.example.servicesyncservice.dto.car.UpsertCarRequest;
import com.example.servicesyncservice.exception.EntityNotFoundException;
import com.example.servicesyncservice.mapper.car.CarMapper;
import com.example.servicesyncservice.model.Car;
import com.example.servicesyncservice.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    private final CarMapper carMapper;


    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    public boolean existsByVin(String vin) {
        return carRepository.existsByVin(vin);
    }

    public Car findByVin(String vin) {
        return carRepository.findByVin(vin)
                .orElseThrow(() -> new RuntimeException("Car not found with VIN: " + vin));
    }

    public Car updateCar(Car car) {
        Car existedCar = carRepository.findById(car.getId()).orElseThrow(
                () -> new EntityNotFoundException("Машина по id не была найдена id:" + car.getId()));
        BeanUtils.copyProperties(car, existedCar);
        return carRepository.save(existedCar);
    }

    public Car updateCar(UpsertCarRequest request, Long id) {
        Car existedCar = carRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Машина по id не была найдена id:" + id));
        BeanUtils.copyProperties(request, existedCar);
        return carRepository.save(existedCar);
    }

}
