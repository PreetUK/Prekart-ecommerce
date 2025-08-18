package com.E_Commerce.repo;

import com.E_Commerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
        List<Product> findByCategoryIgnoreCase(String category);
    }



