package com.project.shopping_site.Forms;

import com.project.shopping_site.Validation.AddOrderValidation;
import com.project.shopping_site.Validation.ChangeOrderQuantityValidation;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
@Data
public class OrderForm {

    @NotNull(groups = {ChangeOrderQuantityValidation.class}, message = "bad request")
    private Integer id;

    @NotNull(groups = {AddOrderValidation.class}, message = "bad request")
    private Integer product;

    @Positive(groups = {AddOrderValidation.class, ChangeOrderQuantityValidation.class}, message = "bad request")
    @NotNull(groups = {AddOrderValidation.class, ChangeOrderQuantityValidation.class}, message = "bad request")
    private Integer quantity;
}
