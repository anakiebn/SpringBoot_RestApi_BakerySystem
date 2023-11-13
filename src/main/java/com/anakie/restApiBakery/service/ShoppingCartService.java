package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.ShoppingCart;
import com.anakie.restApiBakery.exception.*;

import java.util.List;

public interface ShoppingCartService {
    boolean addProduct(Long productId,int productQty) throws OutOfStockException, ShoppingCartNotFoundException, ProductNotFoundException;
    boolean removeProduct(Long productId,int qty) throws ProductNotFoundException, InvalidProductQuantity;

    ShoppingCart save(ShoppingCart shoppingCart) throws Exception;
    List<ShoppingCart> findAll();
    void deleteById(Long id) throws ShoppingCartNotFoundException;

    ShoppingCart update(ShoppingCart shoppingCart) throws ShoppingCartNotFoundException;

    ShoppingCart findById(Long id) throws ShoppingCartNotFoundException;
    boolean existsById(Long id);

    boolean cartValid(ShoppingCart cart) throws ShoppingCartNotFoundException, CartEmptyException, ProductNotFoundException;
}
