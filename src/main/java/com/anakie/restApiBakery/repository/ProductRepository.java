package com.anakie.restApiBakery.repository;

import com.anakie.restApiBakery.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {

    int deleteProductById(Long id);
}
