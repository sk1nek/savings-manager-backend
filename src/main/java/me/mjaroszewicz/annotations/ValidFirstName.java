package me.mjaroszewicz.annotations;


import me.mjaroszewicz.validators.FirstNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = FirstNameValidator.class)

public @interface ValidFirstName {

    String message() default "Invalid First Name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default{};
}
