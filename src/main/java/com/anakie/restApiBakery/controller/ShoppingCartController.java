package com.anakie.restApiBakery.controller;

import com.anakie.restApiBakery.entity.ShoppingCart;
import com.anakie.restApiBakery.exception.ShoppingCartNotFound;
import com.anakie.restApiBakery.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shoppingCarts")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/{id}")
    public ResponseEntity<ShoppingCart> findById(@PathVariable Long id){
        try {
            return new ResponseEntity<>(shoppingCartService.findById(id), HttpStatus.OK);
        } catch (ShoppingCartNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping
    public ResponseEntity<ShoppingCart> save(@RequestBody ShoppingCart shoppingCart){
        try {
            return new ResponseEntity<>(shoppingCartService.save(shoppingCart), HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping
    public ResponseEntity<List<ShoppingCart>> getShoppingCart(){
        return new ResponseEntity<>(shoppingCartService.findAll(), HttpStatus.OK);
    }

//    @PostMapping("/{productId}/{cartId}")
//    public ResponseEntity<ShoppingCartDTO> addProduct(@PathVariable Long productId,@PathVariable Long cartId){
//        try {
//            ShoppingCartDTO shoppingCartDTO=shoppingCartService.addProduct(productId,cartId).toDTO();
//            return new ResponseEntity<>(shoppingCartDTO, HttpStatus.OK);
//        } catch (OutOfStockException | ShoppingCartNotFound | ProductNotFoundException e) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//    }


}
