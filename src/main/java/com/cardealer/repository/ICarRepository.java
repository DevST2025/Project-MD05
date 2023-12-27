package com.cardealer.repository;

import com.cardealer.model.common.ECarStatus;
import com.cardealer.model.entity.Brand;
import com.cardealer.model.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ICarRepository extends JpaRepository<Car, String> {
    boolean existsByName(String name);
    Page<Car> findAll(Pageable pageable);
    Optional<Car> findById(String id); // xem sp detail
    List<Car> findAllByNameContainingOrDescriptionContaining(String name, String description);// search sp
    Page<Car> findAllByStatus(ECarStatus status, Pageable pageable); // sp dc ban
    List<Car> findAllByBrand(Brand brand); // sản phẩm nổi bật, sản phẩm theo danh mục
    List<Car> findAllByCreatedAtBetween(Date start, Date end);

}
