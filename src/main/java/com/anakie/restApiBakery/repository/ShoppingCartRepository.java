package com.anakie.restApiBakery.repository;

import com.anakie.restApiBakery.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
    int deleteShoppingCartById(Long id); //delete

}
