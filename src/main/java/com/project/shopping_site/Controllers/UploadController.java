package com.project.shopping_site.Controllers;

import com.project.shopping_site.AWSServices.AWSS3Service;
import com.project.shopping_site.Entities.*;
import com.project.shopping_site.Forms.ProductUploadForm;
import com.project.shopping_site.Repositories.CategoryRepository;
import com.project.shopping_site.Repositories.OrderRepository;
import com.project.shopping_site.Repositories.ProductRepository;
import com.project.shopping_site.Utilities.GetUser;
import com.project.shopping_site.Validation.ProductRegisterValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class UploadController implements GetUser {
    final AWSS3Service awss3Service;
    final ProductRepository productRepository;
    final CategoryRepository categoryRepository;
    final OrderRepository orderRepository;

    @Autowired
    public UploadController(AWSS3Service awss3Service, ProductRepository productRepository, CategoryRepository categoryRepository, OrderRepository orderRepository) {
        this.awss3Service = awss3Service;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/upload")
    public String uploadPage(Model model) {
        User user = getUser();
        List<Order> orders = orderRepository.findUnpaidOrdersByUserInDesc(user);
        Integer existedOrders = orders.stream().mapToInt(Order::getQuantity).sum();
        model.addAttribute("existedOrders", existedOrders);
        model.addAttribute("product", new ProductUploadForm());
        model.addAttribute("categories", getCategories());
        model.addAttribute("user", user);

        return "upload";
    }
    @PostMapping("/upload")
    public String uploadFile(
            @Validated({ProductRegisterValidation.class}) @ModelAttribute("product") ProductUploadForm form,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(System.out::println);
            model.addAttribute("categories", getCategories());
            return "upload";
        }
        //check that product name doesn't exist yet
        Product existedProduct = productRepository.findByName(form.getName().trim());
        if (existedProduct != null) {
            model.addAttribute("categories", getCategories());
            model.addAttribute("duplicateName", "product name already exists");
            return "upload";
        }
        //check that selected category matches existed one in the database
        Optional<Category> category = categoryRepository.findById(form.getCategoryId());
        if (!category.isPresent()) {
            model.addAttribute("categories", getCategories());
            model.addAttribute("categoryNotFound", "selected category doesn't exist");
            return "upload";
        }

        Product product = new Product();
        product.setName(form.getName().trim());
        product.setPrice(form.getPrice());
        product.setDescription(form.getDescription());
        product.setCategoryId(category.get());

        ArrayList<ProductImage> productImages = new ArrayList<>();
        for (MultipartFile multipartFile : form.getFiles()) {
            String contentType = multipartFile.getContentType();
            String fileName = form.getName() + "." + Objects.requireNonNull(contentType).substring(6);
            String uniqueFileName = awss3Service.uploadFile(multipartFile, fileName);
            if (uniqueFileName != null) {
                ProductImage productImage = new ProductImage();
                productImage.setUrl(uniqueFileName);
                productImage.setProduct(product);
                productImages.add(productImage);
            }
        }
        Thumbnail thumbnail = new Thumbnail();
        thumbnail.setProduct(product);
        thumbnail.setImage(productImages.get(0));
        product.setProductImages(productImages);
        product.setThumbnail(thumbnail);
        try {
            productRepository.save(product);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "redirect:/product/" + form.getName();
    }

    List<Category> getCategories() {
        return categoryRepository.findAll();
    }
}
