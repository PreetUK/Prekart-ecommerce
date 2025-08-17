package com.E_Commerce.controller;

import com.E_Commerce.entity.Cart;
import com.E_Commerce.entity.CartItem;
import com.E_Commerce.entity.Product;
import com.E_Commerce.entity.User;
import com.E_Commerce.repo.CartItemRepository;
import com.E_Commerce.repo.CartRepository;
import com.E_Commerce.repo.ProductRepository;
import com.E_Commerce.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ Add to cart
    @GetMapping("/cart/add/{productId}")
    public String addToCart(@PathVariable Long productId,
                            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        CartItem existingItem = cart.getItems().stream()
                .filter(i -> i.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(1);
            newItem.setCart(cart);
            cartItemRepository.save(newItem);
            cart.getItems().add(newItem);
        }

        cartRepository.save(cart);
        return "redirect:/cart";
    }

    // 🛒 View cart
    @GetMapping("/cart")
    public String viewCart(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user).orElse(null);

        if (cart == null || cart.getItems().isEmpty()) {
            model.addAttribute("cartItems", new ArrayList<>());
            model.addAttribute("totalPrice", 0.0);
            return "cart";
        }

        List<CartItem> items = cart.getItems();
        double totalPrice = items.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        model.addAttribute("cartItems", items);
        model.addAttribute("totalPrice", totalPrice);

        return "cart";
    }

    // 🔄 Update quantity
    @PostMapping("/cart/update/{itemId}")
    public String updateCartItem(@PathVariable Long itemId,
                                 @RequestParam int quantity,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        cartItemRepository.findById(itemId).ifPresent(item -> {
            if (quantity > 0) {
                item.setQuantity(quantity);
                cartItemRepository.save(item);
            }
        });

        return "redirect:/cart";
    }

    // ❌ Remove item
    @GetMapping("/cart/remove/{itemId}")
    public String removeCartItem(@PathVariable Long itemId,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        cartItemRepository.findById(itemId).ifPresent(cartItemRepository::delete);
        return "redirect:/cart";
    }
}
