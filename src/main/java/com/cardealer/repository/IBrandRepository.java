package com.cardealer.repository;

import com.cardealer.model.entity.Brand;
import com.cardealer.model.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBrandRepository extends JpaRepository<Brand, String> {
    Optional<Brand> findByStatus(Boolean status);
    Optional<Brand> findById(String id);
    Page<Brand> findAll(Pageable pageable);
    List<Brand> findAllByStatus(Boolean status);
}
