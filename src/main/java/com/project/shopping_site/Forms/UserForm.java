package com.project.shopping_site.Forms;

import com.project.shopping_site.CustomConstraints.PhoneNumberConstraint;
import com.project.shopping_site.Validation.SignUpValidation;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
@Data
public class UserForm {
    @NotBlank(groups = {SignUpValidation.class}, message = "please fill in user name")
    private String name;
    @NotBlank(groups = {SignUpValidation.class}, message = "please fill in address")
    private String address;
    @NotBlank(groups = {SignUpValidation.class}, message = "please fill in telephone number")
    @PhoneNumberConstraint(groups = {SignUpValidation.class})
    private String tel;
    @NotBlank(groups = {SignUpValidation.class}, message = "please fill in password")
    @Length( groups = {SignUpValidation.class}, min = 10, message = "password must be more than 10 characters")
    private String password;
}
