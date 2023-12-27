package com.cardealer.service.impl;

import com.cardealer.exception.NotFoundException;
import com.cardealer.model.common.ECarStatus;
import com.cardealer.model.common.EColor;
import com.cardealer.model.common.EType;
import com.cardealer.model.dto.request.CarRequest;
import com.cardealer.model.dto.response.CarResponse;
import com.cardealer.model.entity.Brand;
import com.cardealer.model.entity.Car;
import com.cardealer.model.entity.Color;
import com.cardealer.repository.ICarRepository;
import com.cardealer.repository.IColorRepository;
import com.cardealer.service.IBrandService;
import com.cardealer.service.ICarService;
import com.cardealer.util.SKUGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarService implements ICarService {
    private final ICarRepository carRepository;
    private final IColorRepository colorRepository;
    private final UploadService uploadService;
    private final IBrandService brandService;
    @Override
    public boolean existsByName(String name) {
        return carRepository.existsByName(name);
    }
    @Override
    public void addNewCar(CarRequest carRequest) throws NotFoundException {
        String imageUrl = "https://png.pngtree.com/png-vector/20190507/ourmid/pngtree-vector-car-icon-png-image_1024782.jpg";
        if (!carRequest.getFile().isEmpty() || carRequest.getFile() != null) {
            imageUrl = uploadService.uploadFile(carRequest.getFile());
        }
        Set<Color> colorSet = new HashSet<>();
        if (carRequest.getListColors() == null || carRequest.getListColors().isEmpty()) {
            colorSet.add(colorRepository.findByColorName(EColor.BLACK).orElseThrow(() -> new NoSuchElementException("Resource not found.")));
        } else {
            carRequest.getListColors().forEach(c -> {
                switch (c) {
                    case "red" ->
                            colorSet.add(colorRepository.findByColorName(EColor.RED).orElseThrow(() -> new NoSuchElementException("Resource not found.")));
                    case "yellow" ->
                            colorSet.add(colorRepository.findByColorName(EColor.YELLOW).orElseThrow(() -> new NoSuchElementException("Resource not found.")));
                    case "blue" ->
                            colorSet.add(colorRepository.findByColorName(EColor.BLUE).orElseThrow(() -> new NoSuchElementException("Resource not found.")));
                    case "pink" ->
                            colorSet.add(colorRepository.findByColorName(EColor.PINK).orElseThrow(() -> new NoSuchElementException("Resource not found.")));
                    case "green" ->
                            colorSet.add(colorRepository.findByColorName(EColor.GREEN).orElseThrow(() -> new NoSuchElementException("Resource not found.")));
                    default ->
                            colorSet.add(colorRepository.findByColorName(EColor.BLACK).orElseThrow(() -> new NoSuchElementException("Resource not found.")));
                }
            });
        }

        Car car = Car.builder()
                .name(carRequest.getName())
                .colors(colorSet)
                .type(
                        switch (carRequest.getType()) {
                            case "sedan" -> EType.SEDAN;
                            case "hatchback" -> EType.HATCHBACK;
                            case "suv" -> EType.SUV;
                            case "cuv" -> EType.CUV;
                            case "minivan" -> EType.MINIVAN;
                            case "coupe" -> EType.COUPE;
                            case "convertible" -> EType.CONVERTIBLE;
                            case "pickup" -> EType.PICKUP;
                            case "limousine" -> EType.LIMOUSINE;
                            default -> throw new IllegalStateException("Không có hãng: " + carRequest.getType());
                        }
                )
                .description(carRequest.getDescription())
                .unitPrice(carRequest.getUnitPrice())
                .stockQuantity(carRequest.getStockQuantity())
                .image(imageUrl)
                .brand(brandService.findById(carRequest.getBrandId()))
                .createdAt(new Date(System.currentTimeMillis()))
                .updatedAt(null)
                .status(
                        switch (carRequest.getStatus()) {
                            case "instock" -> ECarStatus.IN_STOCK;
                            case "outofstock" -> ECarStatus.OUT_OF_STOCK;
                            case "ontheway" -> ECarStatus.ON_THE_WAY;
                            default -> throw new IllegalStateException("Không có trạng thái: " + carRequest.getStatus());
                        }
                )
                .sku(SKUGenerator.generateSKU(carRequest.getName(), carRequest.getType()))
                .build();
        carRepository.save(car);
    }

    @Override
    public Page<Car> findAll(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return carRepository.findAll(pageable);
    }

    @Override
    public Car findById(String id) throws NotFoundException {
        return carRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy xe có ID: " + id));
    }

    @Override
    public Car editCarById(String id, CarRequest carRequest) throws NotFoundException {
        Car car = findById(id);
        String imageUrl = car.getImage();
        if (!carRequest.getFile().isEmpty() || carRequest.getFile() != null) {
            imageUrl = uploadService.uploadFile(carRequest.getFile());
        }
        Set<Color> colorSet = new HashSet<>();
        if (carRequest.getListColors() == null || carRequest.getListColors().isEmpty()) {
            colorSet.add(colorRepository.findByColorName(EColor.BLACK).orElseThrow(() -> new NoSuchElementException("Resource not found.")));
        } else {
            carRequest.getListColors().forEach(c -> {
                switch (c) {
                    case "red" ->
                            colorSet.add(colorRepository.findByColorName(EColor.RED).orElseThrow(() -> new NoSuchElementException("Resource not found.")));
                    case "yellow" ->
                            colorSet.add(colorRepository.findByColorName(EColor.YELLOW).orElseThrow(() -> new NoSuchElementException("Resource not found.")));
                    case "blue" ->
                            colorSet.add(colorRepository.findByColorName(EColor.BLUE).orElseThrow(() -> new NoSuchElementException("Resource not found.")));
                    case "pink" ->
                            colorSet.add(colorRepository.findByColorName(EColor.PINK).orElseThrow(() -> new NoSuchElementException("Resource not found.")));
                    case "green" ->
                            colorSet.add(colorRepository.findByColorName(EColor.GREEN).orElseThrow(() -> new NoSuchElementException("Resource not found.")));
                    default ->
                            colorSet.add(colorRepository.findByColorName(EColor.BLACK).orElseThrow(() -> new NoSuchElementException("Resource not found.")));
                }
            });
        }
        String carName = car.getName();



        Car carIn = Car.builder()
                .id(car.getId())
                .name(carRequest.getName() == null?carName:carRequest.getName())
                .colors(carRequest.getListColors()==null?car.getColors() : colorSet)
                .type(carRequest.getType().isEmpty()?car.getType():
                        switch (carRequest.getType()) {
                            case "sedan" -> EType.SEDAN;
                            case "hatchback" -> EType.HATCHBACK;
                            case "suv" -> EType.SUV;
                            case "cuv" -> EType.CUV;
                            case "minivan" -> EType.MINIVAN;
                            case "coupe" -> EType.COUPE;
                            case "convertible" -> EType.CONVERTIBLE;
                            case "pickup" -> EType.PICKUP;
                            case "limousine" -> EType.LIMOUSINE;
                            default -> throw new IllegalStateException("Không có hãng: " + carRequest.getType());
                        }
                )
                .description(carRequest.getDescription()==null?car.getDescription():carRequest.getDescription())
                .unitPrice(carRequest.getUnitPrice()==null? car.getUnitPrice() : carRequest.getUnitPrice())
                .stockQuantity(carRequest.getStockQuantity()==null? car.getStockQuantity() : carRequest.getStockQuantity())
                .image(imageUrl)
                .brand(carRequest.getBrandId()==null?car.getBrand(): brandService.findById(carRequest.getBrandId()))
                .createdAt(car.getCreatedAt())
                .updatedAt(new Date(System.currentTimeMillis()))
                .status(carRequest.getStatus()==null?car.getStatus():
                        switch (carRequest.getStatus()) {
                            case "instock" -> ECarStatus.IN_STOCK;
                            case "outofstock" -> ECarStatus.OUT_OF_STOCK;
                            case "ontheway" -> ECarStatus.ON_THE_WAY;
                            default -> throw new IllegalStateException("Không có trạng thái: " + carRequest.getStatus());
                        }
                )
                .sku(SKUGenerator.generateSKU(carRequest.getName(), carRequest.getType()))
                .build();
        carRepository.save(carIn);
        return carIn;
    }

    @Override
    public void deleteById(String id) throws NotFoundException {
        findById(id);
        carRepository.deleteById(id);
    }

    @Override
    public Page<CarResponse> findAllByStatus(ECarStatus status, Pageable pageable) {
        Page<Car> carPage = carRepository.findAllByStatus(status, pageable);
        List<CarResponse> carResponseList = carRepository.findAllByStatus(status, pageable).getContent().stream().map(car -> {
            return CarResponse.builder()
                    .id(car.getId())
                    .name(car.getName())
                    .colors(car.getColors())
                    .type(car.getType())
                    .description(car.getDescription())
                    .unitPrice(car.getUnitPrice())
                    .stockQuantity(car.getStockQuantity())
                    .image(car.getImage())
                    .brand(car.getBrand())
                    .status(car.getStatus())
                    .sku(car.getSku())
                    .build();
        }).toList();
        return new PageImpl<>(carResponseList, pageable, carPage.getTotalElements());
    }

    @Override
    public List<CarResponse> findAllByBrandId(String brandId) throws NotFoundException {
        Brand brand = brandService.findById(brandId);
        if (brand.getStatus()) {
            List<CarResponse> carResponseList = carRepository.findAllByBrand(brand).stream().map(car -> {
                return CarResponse.builder()
                        .id(car.getId())
                        .name(car.getName())
                        .colors(car.getColors())
                        .type(car.getType())
                        .description(car.getDescription())
                        .unitPrice(car.getUnitPrice())
                        .stockQuantity(car.getStockQuantity())
                        .image(car.getImage())
                        .brand(car.getBrand())
                        .status(car.getStatus())
                        .sku(car.getSku())
                        .build();
            }).toList();
            return carResponseList;
        }
        return null;
    }

    @Override
    public List<CarResponse> findAllByNameContainingOrDescriptionContaining(String name, String description) {
        List<CarResponse> carResponseList = carRepository.findAllByNameContainingOrDescriptionContaining(name, description).stream()
                .filter(car -> car.getBrand().getStatus())
                .map(car -> {
                    return CarResponse.builder()
                            .id(car.getId())
                            .name(car.getName())
                            .colors(car.getColors())
                            .type(car.getType())
                            .description(car.getDescription())
                            .unitPrice(car.getUnitPrice())
                            .stockQuantity(car.getStockQuantity())
                            .image(car.getImage())
                            .brand(car.getBrand())
                            .status(car.getStatus())
                            .sku(car.getSku())
                            .build();
                }).toList();
        return carResponseList;
    }

    @Override
    public List<CarResponse> findAllByCreatedAtBetween(Date start, Date end) {
        List<CarResponse> carResponseList = carRepository.findAllByCreatedAtBetween(start, end).stream()
                .filter(car -> car.getBrand().getStatus())
                .map(car -> {
                    return CarResponse.builder()
                            .id(car.getId())
                            .name(car.getName())
                            .colors(car.getColors())
                            .type(car.getType())
                            .description(car.getDescription())
                            .unitPrice(car.getUnitPrice())
                            .stockQuantity(car.getStockQuantity())
                            .image(car.getImage())
                            .brand(car.getBrand())
                            .status(car.getStatus())
                            .sku(car.getSku())
                            .build();
                }).toList();
        return carResponseList;
    }

    @Override
    public CarResponse showCarDetailById(String id) throws NotFoundException {
        Car car = findById(id);
        return CarResponse.builder()
                .id(car.getId())
                .name(car.getName())
                .colors(car.getColors())
                .type(car.getType())
                .description(car.getDescription())
                .unitPrice(car.getUnitPrice())
                .stockQuantity(car.getStockQuantity())
                .image(car.getImage())
                .brand(car.getBrand())
                .status(car.getStatus())
                .sku(car.getSku())
                .build();
    }
}
