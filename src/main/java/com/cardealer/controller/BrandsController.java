package com.cardealer.controller;

import com.cardealer.model.dto.response.BrandResponse;
import com.cardealer.model.entity.Brand;
import com.cardealer.service.IBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api.cardealer.com/v1/categories")
public class BrandsController {
    private final IBrandService brandService;
    @GetMapping
    public ResponseEntity<List<BrandResponse>> findAllBrands() {
        return new ResponseEntity<>(brandService.findAllByStatus(true), HttpStatus.OK);
    }
}
