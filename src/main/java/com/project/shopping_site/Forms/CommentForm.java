package com.project.shopping_site.Forms;

import com.project.shopping_site.Validation.DeleteCommentValidation;
import com.project.shopping_site.Validation.UpdateCommentValidation;
import com.project.shopping_site.Validation.UploadCommentValidation;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
@Data
public class CommentForm {
    @NotNull(groups = {UpdateCommentValidation.class, DeleteCommentValidation.class})
    Integer id;

    @NotNull(groups = {UploadCommentValidation.class})
    Integer productId;

    @NotBlank(groups = {UploadCommentValidation.class, UpdateCommentValidation.class})
    String message;
}
