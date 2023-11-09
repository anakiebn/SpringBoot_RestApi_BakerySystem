package com.anakie.restApiBakery.repository;

import com.anakie.restApiBakery.entity.CartItem;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
