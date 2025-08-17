package com.E_Commerce.service;

import com.E_Commerce.entity.Cart;
import com.E_Commerce.entity.Order;
import com.E_Commerce.entity.User;
import com.E_Commerce.repo.CartRepository;
import com.E_Commerce.repo.OrderRepository;
import com.E_Commerce.repo.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    public void saveOrder(String paymentId, String razorpayOrderId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Get logged-in user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get user's cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Calculate total amount
        double totalAmount = cart.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        // Create order
        Order order = new Order();
        order.setPaymentId(paymentId);
        order.setRazorpayOrderId(razorpayOrderId);
        order.setAmount(totalAmount);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PAID");
        order.setUser(user);
        order.setCart(cart);

        orderRepository.save(order);

        // Clear cart
        cart.getItems().clear();
        cartRepository.save(cart);
    }

}
