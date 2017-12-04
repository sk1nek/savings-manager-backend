package me.mjaroszewicz.annotations;


import me.mjaroszewicz.validators.FirstNameValidator;

import javax.validation.Constraint;
import java.lang.annotation.Target;

@Constraint(validatedBy = FirstNameValidator.class)

public @interface ValidFirstName {
}
