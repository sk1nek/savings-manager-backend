package me.mjaroszewicz.validators;

import me.mjaroszewicz.annotations.ValidUserDto;
import me.mjaroszewicz.dtos.UserRegistrationDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserDtoValidator implements ConstraintValidator<ValidUserDto, UserRegistrationDto> {

    @Override
    public void initialize(ValidUserDto constraintAnnotation) {
    }

    @Override
    public boolean isValid(UserRegistrationDto value, ConstraintValidatorContext context) {

        boolean validUsername = value.getUsername().matches("/^[A-Za-z0-9]+(?:[ _-][A-Za-z0-9]+)*$/");

        //at least one character, one digit, one character that does not belong in a-zA-Z0-9 and at least 6 characters
        boolean validPassword = value.getPassword().matches("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9\\s]).{6,}");

        //Official RFC 5322 e-mail address standard
        boolean validEmail = value.getEmail().matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");

        return (validUsername && validPassword && validEmail);
    }
}
