package com.cardealer.controller;

import com.cardealer.exception.NotFoundException;
import com.cardealer.model.common.ECarStatus;
import com.cardealer.model.dto.response.CarResponse;
import com.cardealer.service.ICarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api.cardealer.com/v1/products")
public class CarsController {
    private final ICarService carService;
    //Danh sách sản phẩm được bán(có phân trang và sắp xếp)
    @GetMapping
    public ResponseEntity<Page<CarResponse>> findCarInStock(Pageable pageable) {
        return new ResponseEntity<>(carService.findAllByStatus(ECarStatus.IN_STOCK, pageable), HttpStatus.OK);
    }


    //Tìm kiếm sản phẩm theo tên hoặc mô tả
    @GetMapping("/search")
    public ResponseEntity<List<CarResponse>> findCarByNameOrDesc(@RequestParam String search) {
        return new ResponseEntity<>(carService.findAllByNameContainingOrDescriptionContaining(search, search), HttpStatus.OK);
    }
    //Danh sách sản phẩm nổi bật
    @GetMapping("/featured-products")
    public ResponseEntity<List<CarResponse>> findPopularCar() throws NotFoundException {
        return new ResponseEntity<>(carService.findAllByBrandId("5dc60c7e-6bb2-452f-a143-3cc80a644817"), HttpStatus.OK);
    }
    //Danh sách sản phẩm mới
    @GetMapping("/new-products")
    public ResponseEntity<List<CarResponse>> findNewCar() throws NotFoundException {
        return new ResponseEntity<>(carService.findAllByCreatedAtBetween(Date.from(Instant.now().minus(5, ChronoUnit.DAYS)), Date.from(Instant.now())), HttpStatus.OK);
    }
    //Danh sách sản phẩm bán chạy
    //Danh sách sản phẩm theo danh mục
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<List<CarResponse>> findCarByBrand(@PathVariable String categoryId) throws NotFoundException {
        return new ResponseEntity<>(carService.findAllByBrandId(categoryId), HttpStatus.OK);
    }
    //Chi tiết thông tin sản phẩm theo id
    @GetMapping("/{productId}")
    public ResponseEntity<CarResponse> showCarDetailById(@PathVariable String productId) throws NotFoundException {
        return new ResponseEntity<>(carService.showCarDetailById(productId), HttpStatus.OK);
    }
}
