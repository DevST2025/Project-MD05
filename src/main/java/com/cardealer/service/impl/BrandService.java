package com.cardealer.service.impl;

import com.cardealer.exception.NotFoundException;
import com.cardealer.model.dto.request.BrandRequest;
import com.cardealer.model.dto.response.BrandResponse;
import com.cardealer.model.entity.Brand;
import com.cardealer.repository.IBrandRepository;
import com.cardealer.service.IBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BrandService implements IBrandService {
    private final IBrandRepository brandRepository;
    @Override
    public Brand findById(String id) throws NotFoundException {
        return brandRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy hãng nào có ID: " + id));
    }

    @Override
    public boolean existsByName(String name) {
        return false;
    }

    @Override
    public void addNewBrand(BrandRequest brandRequest) throws NotFoundException {
        Brand brand = Brand.builder()
                .name(brandRequest.getName())
                .description(brandRequest.getDescription())
                .status(true)
//                .cars(new HashSet<>())
                .build();
        brandRepository.save(brand);
    }

    @Override
    public Page<Brand> findAll(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return brandRepository.findAll(pageable);
    }

    @Override
    public Brand editBrandById(String id, BrandRequest brandRequest) throws NotFoundException {
        Brand brand = findById(id);
        Brand brandIn = Brand.builder()
                .id(brand.getId())
                .name(brandRequest.getName().isBlank()?brand.getName():brandRequest.getName())
                .description(brandRequest.getDescription().isBlank()?brand.getDescription():brandRequest.getDescription())
                .status(brandRequest.getStatus())
                .build();
        brandRepository.save(brandIn);
        return brandIn;
    }

    @Override
    public void deleteById(String id) throws NotFoundException {
        findById(id);
        brandRepository.deleteById(id);
    }

    @Override
    public List<BrandResponse> findAllByStatus(Boolean status) {
        return brandRepository.findAllByStatus(status).stream().map(brand -> {
            return BrandResponse.builder()
                    .name(brand.getName())
                    .description(brand.getDescription())
                    .build();
        }).collect(Collectors.toList());
    }

}
