package com.anakie.restApiBakery.controller;

import com.anakie.restApiBakery.entity.Product;
import com.anakie.restApiBakery.entity.ProductDTO;
import com.anakie.restApiBakery.exception.CategoryNotFoundException;
import com.anakie.restApiBakery.exception.ProductNotFoundException;
import com.anakie.restApiBakery.exception.RecipeNotFoundException;
import com.anakie.restApiBakery.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id){
        try {
            return new ResponseEntity<>(productService.findById(id), HttpStatus.OK);
        }catch (ProductNotFoundException ex){
            log.error("Exception occurred: "+ex.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping
    public ResponseEntity<List<Product>> getAllProduct(){
            return new ResponseEntity<>(productService.findAll(), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody ProductDTO productDTO){
        try {
            return new ResponseEntity<>(productService.save(productDTO), HttpStatus.OK);
        }catch(RecipeNotFoundException | CategoryNotFoundException | PropertyValueException ex){
            log.error("Exception occurred: "+ex.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        try {
            productService.deleteById(id);
            return new ResponseEntity<>("Product id: "+id+" Successfully removed", HttpStatus.GONE);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
