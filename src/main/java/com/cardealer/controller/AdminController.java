package com.cardealer.controller;

import com.cardealer.exception.NotFoundException;
import com.cardealer.model.dto.request.BrandRequest;
import com.cardealer.model.dto.request.CarRequest;
import com.cardealer.model.entity.Brand;
import com.cardealer.model.entity.Car;
import com.cardealer.model.entity.Role;
import com.cardealer.model.entity.User;
import com.cardealer.service.IBrandService;
import com.cardealer.service.ICarService;
import com.cardealer.service.IRoleService;
import com.cardealer.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api.cardealer.com/v1/admin")
public class AdminController {
    private final IUserService userService;
    private final IRoleService roleService;
    private final ICarService carService;
    private final IBrandService brandService;
    @GetMapping("/users")
    public ResponseEntity<Page<User>> findAllUsers(Pageable pageable) {
        return new ResponseEntity<>(userService.findAll(pageable), HttpStatus.OK);
    }
    @PostMapping("/users/{userId}/role")
    public ResponseEntity<User> upgradeAccount(@PathVariable String userId) throws NotFoundException {
        return new ResponseEntity<>(userService.upgradeAccount(userId), HttpStatus.OK);
    }
    @DeleteMapping("/users/{userId}/role")
    public ResponseEntity<User> downgradeAccount(@PathVariable String userId) throws NotFoundException {
        return new ResponseEntity<>(userService.downgradeAccount(userId), HttpStatus.OK);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<User> receptorAccount(@PathVariable String userId) throws NotFoundException {
        return new ResponseEntity<>(userService.receptorAccount(userId), HttpStatus.OK);
    }

    @GetMapping("/roles") // chua test
    public ResponseEntity<List<Role>> findAllRoles() {
        return new ResponseEntity<>(roleService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/users/search")
    public ResponseEntity<Page<User>> findUserByName(@RequestParam String name, Pageable pageable) {
        return new ResponseEntity<>(userService.findUserByName(name, pageable), HttpStatus.OK);
    }

    @GetMapping("/products")
    public ResponseEntity<Page<Car>> findAllProducts(Pageable pageable) {
        return new ResponseEntity<>(carService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Car> findCarById(@PathVariable String productId) throws NotFoundException {
        return new ResponseEntity<>(carService.findById(productId), HttpStatus.OK);
    }

    @PostMapping("/products")
    public ResponseEntity<String> addNewProduct(@Valid @ModelAttribute CarRequest carRequest) throws NotFoundException {
        carService.addNewCar(carRequest);
        return new ResponseEntity<>("Thêm mới thành công", HttpStatus.OK);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<Car> editCarById(@PathVariable String productId, @ModelAttribute CarRequest carRequest) throws NotFoundException {
        return new ResponseEntity<>(carService.editCarById(productId, carRequest), HttpStatus.OK);
    }

    @DeleteMapping("/products/{productId}") // chua test
    public ResponseEntity<String> deleteCarById(@PathVariable String productId) throws NotFoundException {
        carService.deleteById(productId);
        return new ResponseEntity<>("Xoá sản phẩm thành công", HttpStatus.OK);
    }


    @GetMapping("/categories")
    public ResponseEntity<Page<Brand>> findAllBrands(Pageable pageable) {
        return new ResponseEntity<>(brandService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<Brand> findBrandById(@PathVariable String categoryId) throws NotFoundException {
        return new ResponseEntity<>(brandService.findById(categoryId), HttpStatus.OK);
    }

    @PostMapping("/categories")
    public ResponseEntity<String> addNewCategory(@Valid @RequestBody BrandRequest brandRequest) throws NotFoundException {
        brandService.addNewBrand(brandRequest);
        return new ResponseEntity<>("Thêm mới thành công", HttpStatus.OK);
    }

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<Brand> editBrandById(@PathVariable String categoryId, @RequestBody BrandRequest brandRequest) throws NotFoundException {
        return new ResponseEntity<>(brandService.editBrandById(categoryId, brandRequest), HttpStatus.OK);
    }

    @DeleteMapping("/categories/{categoryId}") // chua test
    public ResponseEntity<String> deleteBrandById(@PathVariable String categoryId) throws NotFoundException {
        brandService.deleteById(categoryId);
        return new ResponseEntity<>("Xoá danh mục thành công", HttpStatus.OK);
    }


}
