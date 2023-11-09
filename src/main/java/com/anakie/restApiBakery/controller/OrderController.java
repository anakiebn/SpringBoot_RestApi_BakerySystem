package com.anakie.restApiBakery.controller;

import com.anakie.restApiBakery.entity.Order;
import com.anakie.restApiBakery.entity.OrderDTO;
import com.anakie.restApiBakery.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/orders")
@Slf4j
public class OrderController {


    @Autowired
    private OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<Order> findById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(orderService.findById(id), HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception occurred: " + ex.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Order>> findAll() {
        try {
            return new ResponseEntity<>(orderService.findAll(), HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception occurred: " + ex.getMessage());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping
    public ResponseEntity<Order> save(@RequestBody OrderDTO orderDTO) {
        try {
            return new ResponseEntity<>(orderService.save(orderDTO), HttpStatus.CREATED);
        } catch (Exception ex) {
            log.error("Exception occurred: " + ex.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        try {
            orderService.deleteById(id);
            return new ResponseEntity<>("Order id: " + id + " successfully removed", HttpStatus.GONE);
        } catch (Exception ex) {
            log.error("Exception occurred: " + ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
