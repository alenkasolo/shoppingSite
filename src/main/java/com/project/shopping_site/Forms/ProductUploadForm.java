package com.project.shopping_site.Forms;

import com.project.shopping_site.CustomConstraints.MultipartFileConstraint;
import com.project.shopping_site.Validation.ProductRegisterValidation;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class ProductUploadForm {
    @NotBlank(message = "please fill in product name", groups = {ProductRegisterValidation.class})
    @Length(max = 255, message = "too long", groups = {ProductRegisterValidation.class})
    private String name;

    @Positive(message = "the price must be non-negative", groups = {ProductRegisterValidation.class})
    @NotNull(message = "please fill in price", groups = {ProductRegisterValidation.class})
    private Integer price;

    @NotNull(message = "please select category name", groups = {ProductRegisterValidation.class})
    private Integer categoryId;

    @NotBlank(message = "please fill in description", groups = {ProductRegisterValidation.class})
    @Length(max = 10000, message = "too long", groups = {ProductRegisterValidation.class})
    private String description;
    @MultipartFileConstraint(groups = {ProductRegisterValidation.class})
    MultipartFile[] files;
}
