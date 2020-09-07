package com.project.shopping_site.Controllers;

import com.project.shopping_site.Entities.Order;
import com.project.shopping_site.Entities.Product;
import com.project.shopping_site.Entities.User;
import com.project.shopping_site.Repositories.OrderRepository;
import com.project.shopping_site.Repositories.ProductRepository;
import com.project.shopping_site.Utilities.GetUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController implements GetUser {
    final ProductRepository productRepository;
    final OrderRepository orderRepository;
    @Autowired
    public SearchController(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam("search") String searchQuery) {
        if (searchQuery.trim().length() > 0) {
            User user = getUser();
            if (user != null) {
                List<Order> orders = orderRepository.findUnpaidOrdersByUserInDesc(user);
                Integer existedOrders = orders.stream().mapToInt(Order::getQuantity).sum();
                model.addAttribute("existedOrders", existedOrders);
            }
            List<Product> products = productRepository.searchedProducts(searchQuery);
            model.addAttribute("products", products);
            model.addAttribute("searchQuery", searchQuery);
            return "search";
        }
        return "error/404";
    }
}
