package com.cardealer.service;

import com.cardealer.exception.NotFoundException;
import com.cardealer.model.dto.request.BrandRequest;
import com.cardealer.model.dto.request.CarRequest;
import com.cardealer.model.dto.response.BrandResponse;
import com.cardealer.model.entity.Brand;
import com.cardealer.model.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBrandService {
    Brand findById(String id) throws NotFoundException;

    boolean existsByName(String name);

    void addNewBrand(BrandRequest brandRequest) throws NotFoundException;
    Page<Brand> findAll(Pageable pageable);
    List<BrandResponse>  findAllByStatus(Boolean status);
    Brand editBrandById(String id, BrandRequest brandRequest) throws NotFoundException;

    void deleteById(String id) throws NotFoundException;
}
