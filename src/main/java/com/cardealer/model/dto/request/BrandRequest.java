package com.cardealer.model.dto.request;

import com.cardealer.model.entity.Car;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandRequest {
    @NotBlank(message = "Brand's name cannot be left blank.")
    private String name;
    private String description;
    private Boolean status = true;
    private Set<Car> cars = new HashSet<>();
}
