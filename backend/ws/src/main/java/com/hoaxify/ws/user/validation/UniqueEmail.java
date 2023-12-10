package com.hoaxify.ws.user.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(
        validatedBy = UniqueEmailValidator.class
)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    String message() default "{hoaxify.constraint.email.notunique}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
