package com.cardealer.util.validator;

import com.cardealer.service.IUserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PhoneUniqueValidator implements ConstraintValidator<PhoneUnique, String> {
    private final IUserService userService;
    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        return !userService.existsByPhone(phone);
    }
}
