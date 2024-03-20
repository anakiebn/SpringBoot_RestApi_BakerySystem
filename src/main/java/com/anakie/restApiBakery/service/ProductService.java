package com.anakie.restApiBakery.service;


import com.anakie.restApiBakery.entity.Product;
import com.anakie.restApiBakery.entity.ProductDTO;
import com.anakie.restApiBakery.exception.CategoryNotFoundException;
import com.anakie.restApiBakery.exception.ProductNotFoundException;
import com.anakie.restApiBakery.exception.RecipeNotFoundException;
import org.hibernate.PropertyValueException;

import java.util.List;

public interface ProductService {

    Product save(ProductDTO productDTO) throws RecipeNotFoundException, CategoryNotFoundException, PropertyValueException;

    List<Product> save(List<ProductDTO> productDTOS)

            throws RecipeNotFoundException, CategoryNotFoundException, PropertyValueException;
    Product findById(Long id) throws ProductNotFoundException;
    List<Product> findAll();
    void deleteById(Long id) throws ProductNotFoundException;
    Product update(ProductDTO productDTO) throws ProductNotFoundException, CategoryNotFoundException, RecipeNotFoundException;
    boolean existsById(Long id);
    double totalAmount(Long productId,int productQty) throws ProductNotFoundException;
}
