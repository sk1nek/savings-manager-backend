package me.mjaroszewicz.annotations;


import me.mjaroszewicz.validators.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = PasswordValidator.class)

public @interface ValidPassword {

    String message() default "Invalid password";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default {};
}
