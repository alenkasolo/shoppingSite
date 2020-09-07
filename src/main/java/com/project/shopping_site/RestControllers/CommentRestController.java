package com.project.shopping_site.RestControllers;

import com.project.shopping_site.Entities.Comment;
import com.project.shopping_site.Entities.Product;
import com.project.shopping_site.Entities.User;
import com.project.shopping_site.Forms.CommentForm;
import com.project.shopping_site.Repositories.CommentRepository;
import com.project.shopping_site.Repositories.ProductRepository;
import com.project.shopping_site.Utilities.GetUser;
import com.project.shopping_site.Validation.DeleteCommentValidation;
import com.project.shopping_site.Validation.UpdateCommentValidation;
import com.project.shopping_site.Validation.UploadCommentValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CommentRestController implements GetUser {
    final CommentRepository commentRepository;
    final ProductRepository productRepository;

    @Autowired
    public CommentRestController(CommentRepository commentRepository, ProductRepository productRepository) {
        this.commentRepository = commentRepository;
        this.productRepository = productRepository;
    }


    @PostMapping("/comment")
    public ResponseEntity<Map<String, String>> commentResponseEntity(@Validated({UploadCommentValidation.class}) CommentForm submittedComment,
                                                                     BindingResult result) {
        if (result.hasErrors()) {
            result.getAllErrors().forEach(System.out::println);
            return ResponseEntity.noContent().build();
        }
        User user = getUser();
        Comment comment = new Comment();
        Product product = new Product();
        product.setId(submittedComment.getProductId());
        comment.setUserId(user);
        comment.setMessage(submittedComment.getMessage());
        comment.setProductId(product);
        commentRepository.saveAndFlush(comment);
        Map<String, String> response = new HashMap<>();
        response.put("comment", comment.getMessage());
        response.put("user", user.getName());
        response.put("commentId", comment.getId().toString());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/deleteComment")
    public ResponseEntity<String> deleteComment(@Validated({DeleteCommentValidation.class}) CommentForm comment, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(System.out::println);
            return ResponseEntity.badRequest().build();
        }
        User user = getUser();
        int result = commentRepository.deleteByIdAndUserId(comment.getId(), user);
        return ResponseEntity.ok().body(Integer.toString(result));
    }

    @PostMapping("/updateComment")
    public ResponseEntity<String> updateComment(@Validated({UpdateCommentValidation.class}) CommentForm comment, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(System.out::println);
            return ResponseEntity.badRequest().build();
        }
        User user = getUser();
        int result = commentRepository.updateByIdAndUserId(comment.getMessage(), Calendar.getInstance(), comment.getId(), user);
        return ResponseEntity.ok().body(Integer.toString(result));
    }
}
