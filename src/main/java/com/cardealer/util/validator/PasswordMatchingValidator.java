package com.cardealer.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class PasswordMatchingValidator implements ConstraintValidator<PasswordMatching, Object> {
    private String password;
    private String confirmPassword;
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object passwordValue = new BeanWrapperImpl(value).getPropertyValue(password);
        Object confirmPasswordValue = new BeanWrapperImpl(value).getPropertyValue(confirmPassword);
        if (passwordValue == null || !passwordValue.equals(confirmPasswordValue)) {
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(confirmPassword)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    @Override
    public void initialize(PasswordMatching passwordMatching) {
        this.password = passwordMatching.password();
        this.confirmPassword = passwordMatching.confirmPassword();
    }
}
