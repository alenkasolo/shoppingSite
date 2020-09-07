package com.project.shopping_site.Controllers;

import com.project.shopping_site.Entities.Order;
import com.project.shopping_site.Entities.User;
import com.project.shopping_site.Repositories.OrderRepository;
import com.project.shopping_site.Utilities.GetUser;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Controller
public class CartController implements GetUser {
    @Value("${stripe}")
    private String apiKey;
    final OrderRepository orderRepository;

    public CartController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/cart")
    public String cart(Model model) {
        User user = getUser();
        List<Order> orders = orderRepository.findUnpaidOrdersByUserInDesc(user);
        Integer existedOrders = orders.stream().mapToInt(Order::getQuantity).sum();
        int totalAmount = 0;
        for (Order order : orders) {
            totalAmount += (order.getQuantity() * order.getProduct().getPrice());
        }
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("ja", "JP"));
        String totalAmountString = format.format(totalAmount);
        model.addAttribute("totalAmount", totalAmountString);
        model.addAttribute("existedOrders", existedOrders);
        model.addAttribute("orders", orders);
        model.addAttribute("user", user);
        return "cart";
    }
    @PostMapping("/charge")
    public String charge(@RequestParam("stripeToken") String token, RedirectAttributes redirectAttributes) throws StripeException {
        Stripe.apiKey = apiKey;
        List<Order> orders = orderRepository.findUnpaidOrdersByUserInDesc(getUser());
        long amount = 0;
        for (Order order : orders) {
            amount += (order.getQuantity() * order.getProduct().getPrice());
        }
        ChargeCreateParams params = ChargeCreateParams.builder().setAmount(amount).setCurrency("jpy").setDescription("Test").setSource(token).build();
        try {
            Charge charge = Charge.create(params);
            System.out.println(charge.getStatus());
        } catch (StripeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart";
        }
        for (Order order : orders) {
            order.setPaid(true);
        }
        orderRepository.saveAll(orders);
        return "redirect:/";
    }
}
