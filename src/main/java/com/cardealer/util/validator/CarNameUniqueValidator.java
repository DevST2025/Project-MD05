package com.cardealer.util.validator;

import com.cardealer.service.ICarService;
import com.cardealer.service.IUserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CarNameUniqueValidator implements ConstraintValidator<CarNameUnique, String> {
    private final ICarService carService;
    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return !carService.existsByName(name);
    }
}
