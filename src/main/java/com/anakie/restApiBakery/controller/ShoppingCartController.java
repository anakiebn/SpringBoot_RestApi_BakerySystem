package com.anakie.restApiBakery.controller;

import com.anakie.restApiBakery.entity.ShoppingCart;
import com.anakie.restApiBakery.exception.ShoppingCartNotFoundException;
import com.anakie.restApiBakery.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shoppingCarts")
@RequiredArgsConstructor
public class ShoppingCartController {

    @Autowired
    private final ShoppingCartService shoppingCartService;

    @GetMapping("/find/{id}")
    public ResponseEntity<ShoppingCart> findById(@PathVariable Long id) throws ShoppingCartNotFoundException {

            return new ResponseEntity<>(shoppingCartService.findById(id), HttpStatus.OK);

    }
    @PostMapping("/save")
    public ResponseEntity<ShoppingCart> save(@RequestBody ShoppingCart shoppingCart) throws Exception {

            return new ResponseEntity<>(shoppingCartService.save(shoppingCart), HttpStatus.OK);

    }
    @PostMapping("/update")
    public ResponseEntity<ShoppingCart> update(@RequestBody ShoppingCart shoppingCart) throws Exception {

        return new ResponseEntity<>(shoppingCartService.update(shoppingCart), HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<ShoppingCart>> findAll(){
        return new ResponseEntity<>(shoppingCartService.findAll(), HttpStatus.OK);
    }



}
