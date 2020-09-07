package com.project.shopping_site.CustomConstraints;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class MultipartFileValidator implements ConstraintValidator<MultipartFileConstraint, MultipartFile[]> {
    @Override
    public void initialize(MultipartFileConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(MultipartFile[] files, ConstraintValidatorContext context) {
        boolean isEmpty = files[0].isEmpty();
        if (isEmpty) {
            return false;
        }
        for (MultipartFile file : files) {
            String contentType = file.getContentType();
            if (!Objects.requireNonNull(contentType).startsWith("image/")) {
                return false;
            }
        }
        return true;
    }
}
