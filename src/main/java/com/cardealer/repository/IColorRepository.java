package com.cardealer.repository;

import com.cardealer.model.common.EColor;
import com.cardealer.model.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IColorRepository extends JpaRepository<Color, Long> {
    Optional<Color> findByColorName(EColor eColor);
}
