package com.anakie.restApiBakery.controller;

import com.anakie.restApiBakery.entity.Order;
import com.anakie.restApiBakery.entity.OrderDTO;
import com.anakie.restApiBakery.exception.OrderNotFoundException;
import com.anakie.restApiBakery.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/orders")
@Slf4j
@RequiredArgsConstructor
public class OrderController {


    @Autowired
    private final OrderService orderService;

    @GetMapping("/find/{id}")
    public ResponseEntity<Order> findById(@PathVariable Long id) throws OrderNotFoundException {
            return new ResponseEntity<>(orderService.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Order>> findAll() {
            return new ResponseEntity<>(orderService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Order> save(@RequestBody OrderDTO orderDTO) throws Exception {
            return new ResponseEntity<>(orderService.save(orderDTO), HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<Order> update(@RequestBody OrderDTO orderDTO) throws Exception {
        return new ResponseEntity<>(orderService.save(orderDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) throws OrderNotFoundException {

            orderService.deleteById(id);
            return new ResponseEntity<>("Order id: " + id + " successfully removed", HttpStatus.GONE);

    }

}
