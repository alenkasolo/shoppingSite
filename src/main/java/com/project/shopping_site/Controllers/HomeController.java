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
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController implements GetUser {
    final OrderRepository orderRepository;
    final CategoryRepository categoryRepository;
    final ProductRepository productRepository;
    @Autowired
    public HomeController(OrderRepository orderRepository, CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/")
    public String home(Model model, HttpServletRequest request) {
        System.out.println(((CsrfToken)request.getAttribute("_csrf")).getToken());
        User user = getUser();
        if (user != null) {
            List<Order> orders = orderRepository.findUnpaidOrdersByUserInDesc(user);
            Integer existedOrders = orders.stream().mapToInt(Order::getQuantity).sum();
            model.addAttribute("existedOrders", existedOrders);
        }
        List<Category> categories = categoryRepository.findAll();
        List<List<Product>> productsOfAllCategories = new ArrayList<>();
        for (Category category : categories) {
            List<Product> products = productRepository.findByCategoryIdInDesc(category, PageRequest.of(0, 4));
            productsOfAllCategories.add(products);
        }
        model.addAttribute("productsOfAllCategories", productsOfAllCategories);
        model.addAttribute("user", user);
        return "home";
    }
}