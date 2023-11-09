package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Product;
import com.anakie.restApiBakery.entity.ShoppingCart;
import com.anakie.restApiBakery.exception.*;

import java.util.List;

public interface ShoppingCartService {
    boolean addProduct(Long productId,int productQty) throws OutOfStockException, ShoppingCartNotFound, ProductNotFoundException;
    boolean removeProduct(Long productId,int qty) throws ProductNotFoundException, InvalidProductQuantity;

    ShoppingCart save(ShoppingCart shoppingCart) throws Exception;
    List<ShoppingCart> findAll();
    void deleteById(Long id) throws ShoppingCartNotFound;

    ShoppingCart update(ShoppingCart shoppingCart) throws ShoppingCartNotFound;

    ShoppingCart findById(Long id) throws ShoppingCartNotFound;
    boolean existsById(Long id);

    boolean cartValid(ShoppingCart cart) throws ShoppingCartNotFound, CartEmptyException, ProductNotFoundException;
}
