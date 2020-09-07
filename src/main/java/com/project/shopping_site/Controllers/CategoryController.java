package com.project.shopping_site.Controllers;

import com.project.shopping_site.Entities.Category;
import com.project.shopping_site.Entities.Order;
import com.project.shopping_site.Entities.Product;
import com.project.shopping_site.Entities.User;
import com.project.shopping_site.Repositories.CategoryRepository;
import com.project.shopping_site.Repositories.OrderRepository;
import com.project.shopping_site.Repositories.ProductRepository;
import com.project.shopping_site.Utilities.GetUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CategoryController implements GetUser {
    final ProductRepository productRepository;
    final CategoryRepository categoryRepository;
    final OrderRepository orderRepository;
    @Autowired
    public CategoryController(ProductRepository productRepository, CategoryRepository categoryRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/category/{name}")
    public String getSmartPhone(Model model, @PathVariable String name, @RequestParam("page") Integer page) {
        User user = getUser();
        if (user != null) {
            List<Order> orders = orderRepository.findUnpaidOrdersByUserInDesc(user);
            int existedOrders = orders.stream().mapToInt(Order::getQuantity).sum();
            model.addAttribute("existedOrders", existedOrders);
        }

        Category category = categoryRepository.findByName(name);
        if (category != null) {
            int size = 12;
            Double productQuantity = productRepository.countProducts(category.getId());
            int pages = (int) Math.ceil(productQuantity / size);
            if (page < 0) {
                page = 0;
            } else if (page > pages - 1) {
                page = pages - 1;
            }
            List<Product> products = productRepository.findByCategoryIdInDesc(category, PageRequest.of(page, size));
            model.addAttribute("products", products);
            model.addAttribute("category", category);
            model.addAttribute("user", user);
            model.addAttribute("pages", pages);
            return "category";
        }
        return "error/404";
    }

}
