package com.E_Commerce.controller;

import com.E_Commerce.entity.User;
import com.E_Commerce.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 🔐 Show login form
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // refers to login.html
    }

    // 📝 Show registration form
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // refers to register.html
    }

    // 💾 Handle registration form submission
    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("user") User user, Model model) {
        // Check if username is already taken
        if (userRepository.findByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "Username already exists!");
            return "register";
        }

        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Default role: CUSTOMER
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            user.setRole("CUSTOMER");
        }

        userRepository.save(user);

        // Show success message and clear form
        model.addAttribute("success", "Registered successfully! Please login.");
        model.addAttribute("user", new User()); // reset form
        return "register";

        // Alternative redirect after registration:
        // return "redirect:/login";
    }
}
