package com.E_Commerce.controller;

import com.E_Commerce.entity.Product;
import com.E_Commerce.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductRepository productRepository;



    public String viewProducts(@RequestParam (value = "category", required = false)String category, Model model){
        List<Product> products;
            if (category == null || category.equalsIgnoreCase("All Categories")) {
                products = productRepository.findAll();
            } else {
                products = productRepository.findByCategoryIgnoreCase(category);
            }

            model.addAttribute("products", products);
            model.addAttribute("selectedCategory", category != null ? category : "All Categories");
            return "products";
        }
    }



