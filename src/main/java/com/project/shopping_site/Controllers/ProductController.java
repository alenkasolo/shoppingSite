package com.project.shopping_site.Controllers;

import com.project.shopping_site.Entities.Comment;
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
import org.springframework.web.bind.annotation.PathVariable;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Controller
public class ProductController implements GetUser {
    final ProductRepository productRepository;
    final OrderRepository orderRepository;

    @Autowired
    public ProductController(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/product/{name}")
    public String product(Model model, @PathVariable String name) {
        Product result = productRepository.findByName(name);
        if (result == null) {
            return "error/404";
        }
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("ja", "JP"));
        String priceInString = format.format(result.getPrice());
        User user = getUser();
        if (user != null) {
            List<Order> orders = orderRepository.findUnpaidOrdersByUserInDesc(user);
            Integer existedOrders = orders.stream().mapToInt(Order::getQuantity).sum();
            model.addAttribute("existedOrders", existedOrders);
        }
        Collections.sort(result.getComments());
        Order order = new Order();
        order.setProduct(result);
        model.addAttribute("product", result);
        model.addAttribute("comment", new Comment());
        model.addAttribute("order", order);
        model.addAttribute("user", user);
        model.addAttribute("price", priceInString);
        return "product";
    }
}
