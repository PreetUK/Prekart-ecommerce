package com.E_Commerce.repo;

import com.E_Commerce.entity.Cart;
import com.E_Commerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
