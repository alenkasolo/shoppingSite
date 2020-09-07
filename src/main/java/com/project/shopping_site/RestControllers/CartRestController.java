package com.project.shopping_site.RestControllers;

import com.project.shopping_site.Entities.Order;
import com.project.shopping_site.Entities.Product;
import com.project.shopping_site.Entities.User;
import com.project.shopping_site.Forms.OrderForm;
import com.project.shopping_site.Repositories.OrderRepository;
import com.project.shopping_site.Utilities.GetUser;
import com.project.shopping_site.Validation.AddOrderValidation;
import com.project.shopping_site.Validation.ChangeOrderQuantityValidation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.NumberFormat;
import java.util.*;

@RestController
public class CartRestController implements GetUser {

    final OrderRepository orderRepository;

    public CartRestController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostMapping("/addToCart")
    public ResponseEntity<String> addToCartHandler(@Validated({AddOrderValidation.class}) OrderForm submittedOrder, BindingResult result) {
        if (result.hasErrors()) {
            result.getAllErrors().forEach(System.out::println);
            return ResponseEntity.badRequest().body("oops! Something wrong here");
        }
        User user = getUser();
        Order existedOrder = orderRepository.findUnpaidOrderByProductAndUser(submittedOrder.getProduct(), user);
        if (existedOrder != null) {
            existedOrder.setQuantity(existedOrder.getQuantity() + submittedOrder.getQuantity());
            orderRepository.save(existedOrder);
        } else {
            try {
                Product product = new Product();
                product.setId(submittedOrder.getProduct());
                Order order = new Order();
                order.setUser(user);
                order.setProduct(product);
                order.setQuantity(submittedOrder.getQuantity());
                order.setCreatedAt(Calendar.getInstance());
                order.setDelivered(false);
                order.setPaid(false);
                orderRepository.save(order);
            } catch (Exception exception) {
                exception.printStackTrace();
                return ResponseEntity.badRequest().body("oops! Something wrong here");
            }
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/changeQuantity")
    public ResponseEntity<Map<String, String>> changeQuantity(@Validated({ChangeOrderQuantityValidation.class}) OrderForm submittedOrder, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(System.out::println);
            return ResponseEntity.badRequest().build();
        }
        Order existedOrder = orderRepository.findByIdAndUser(submittedOrder.getId(), getUser());
        if (existedOrder != null) {
            existedOrder.setQuantity(submittedOrder.getQuantity());
            orderRepository.save(existedOrder);
            List<Order> orders = orderRepository.findUnpaidOrdersByUserInDesc(getUser());
            int totalAmount = 0;
            for (Order order : orders) {
                totalAmount += (order.getQuantity() * order.getProduct().getPrice());
            }
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("ja", "JP"));
            String totalAmountString = format.format(totalAmount);
            Map<String, String> model = new HashMap<>();
            model.put("totalAmount", totalAmountString);
            model.put("quantity", Integer.toString(orders.stream().mapToInt(Order::getQuantity).sum()));
            return ResponseEntity.ok().body(model);
        }
        return ResponseEntity.badRequest().build();
    }
}
