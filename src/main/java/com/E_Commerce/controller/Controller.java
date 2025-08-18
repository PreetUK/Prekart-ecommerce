package com.E_Commerce.controller;

import com.E_Commerce.entity.Product;
import com.E_Commerce.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Controller
public class Controller {
    @Autowired
    ProductRepository repository ;
    @Autowired
    private ProductRepository productRepository;
    @GetMapping("/admin/add-product")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "index";   // this will show your product form
    }

    @PostMapping("/save")
    public String postData(@Validated Product product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "index"; // stay on form with validation errors
        }

        repository.save(product); // ✅ save new or update existing

        model.addAttribute("msg", "Product Saved Successfully");
        model.addAttribute("product", new Product()); // ✅ reset form to empty

        return "index"; // go back to form page with empty product
    }

    @GetMapping("/products")
    public String getAllProducts(Model model){
        model.addAttribute("products",repository.findAll());
        return "products";

    }
    @GetMapping("/edit")
    public String edit(@RequestParam("productId") Long id , Model model) {
        Optional<Product> p = repository.findById(id);
        if (p.isPresent()) {
            Product product = p.get();
            repository.save(product);
            model.addAttribute("product", product);
        }
        return "index";

    }
    @GetMapping("/delete")
    public String delete(@RequestParam ("productId") Long id, Model model){
        Optional<Product> p = repository.findById(id);
        if (p.isPresent()){
            repository.deleteById(id);
            model.addAttribute("msg", "Delete Success");
            model.addAttribute("products",repository.findAll());
        }
        return "products";

    }

    @GetMapping("/product/{id}")
    public String viewProductDetails(@PathVariable("id") Long id, Model model) {
        Product product = repository.findById(id).orElse(null);

        if (product == null) {
            model.addAttribute("error", "Product not found");
            return "redirect:/products";
        }

        model.addAttribute("product", product);
        return "product_detail"; // must match the file name in templates folder
    }
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        List<Product> products = productRepository.findAll(); // ✅ Now it's instance method

        model.addAttribute("products", products);
        model.addAttribute("totalProducts", products.size());
        model.addAttribute("totalCategories", products.stream().map(Product::getCategory).distinct().count());
        model.addAttribute("totalQuantity", products.stream().mapToInt(Product::getQuantity).sum());

        return "dashboard";
    }





}
