package com.cardealer.service;

import com.cardealer.exception.NotFoundException;
import com.cardealer.model.common.ECarStatus;
import com.cardealer.model.dto.request.CarRequest;
import com.cardealer.model.dto.response.CarResponse;
import com.cardealer.model.entity.Brand;
import com.cardealer.model.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ICarService {
    boolean existsByName(String name);

    void addNewCar(CarRequest carRequest) throws NotFoundException;
    Page<Car> findAll(Pageable pageable);
    Car findById(String id) throws NotFoundException;
    Car editCarById(String id, CarRequest carRequest) throws NotFoundException;

    void deleteById(String id) throws NotFoundException;


    List<CarResponse> findAllByNameContainingOrDescriptionContaining(String name, String description);// search sp
    Page<CarResponse> findAllByStatus(ECarStatus status, Pageable pageable); // sp dc ban
    List<CarResponse> findAllByBrandId(String brandId) throws NotFoundException; // sản phẩm nổi bật, sản phẩm theo danh mục
    List<CarResponse> findAllByCreatedAtBetween(Date start, Date end);
    CarResponse showCarDetailById(String id) throws NotFoundException;
}
