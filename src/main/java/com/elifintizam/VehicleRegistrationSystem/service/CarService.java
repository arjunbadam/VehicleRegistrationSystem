package com.elifintizam.VehicleRegistrationSystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elifintizam.VehicleRegistrationSystem.model.Car;
import com.elifintizam.VehicleRegistrationSystem.repository.CarRepository;

import jakarta.transaction.Transactional;

@Service
public class CarService {

    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getCars() {
        return carRepository.findAll();
    }

    public void addCar(Car car) {
        carRepository.save(car);
    }

    public void deleteCar(Long carId) {
        boolean exists = carRepository.existsById(carId);
        if (!exists) {
            throw new IllegalStateException("Car with id " + carId + " does not exists.");
        }
        carRepository.deleteById(carId);
    }

    @Transactional
    public void updateCar(Long carId, String carName, String brand, String model, Integer year, String numberPlate) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalStateException("Car with id " + carId + " does not exists."));

        if (carName != null && carName != car.getCarName()) {
            car.setCarName(carName);
        }

        if (brand != null && brand != car.getBrand()) {
            car.setBrand(brand);
        }

        if (model != null && model != car.getModel()) {
            car.setModel(model);
        }

        if (year != null && year >= 1750 && year <= 2023 && year != car.getYear()) {
            car.setYear(year);
        }

        if (numberPlate != null && numberPlate != car.getNumberPlate()) {
            Optional<Car> carOptional = carRepository.findCarByNumberPlate(numberPlate);
            if (carOptional.isPresent()) {
                throw new IllegalStateException("This number plate is already registered.");
            } else {
                car.setNumberPlate(numberPlate);
            }

        }

    }
}
