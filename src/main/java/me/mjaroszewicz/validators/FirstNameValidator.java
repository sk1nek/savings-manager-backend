package me.mjaroszewicz.validators;

import me.mjaroszewicz.annotations.ValidFirstName;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FirstNameValidator implements ConstraintValidator<ValidFirstName, String> {

    @Override
    public void initialize(ValidFirstName constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.matches("[A-Z]([a-z])*");
    }

}
