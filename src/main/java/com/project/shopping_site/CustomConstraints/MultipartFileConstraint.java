package com.project.shopping_site.CustomConstraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MultipartFileValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MultipartFileConstraint {
    String message() default "Please select at least 1 image or images only";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
