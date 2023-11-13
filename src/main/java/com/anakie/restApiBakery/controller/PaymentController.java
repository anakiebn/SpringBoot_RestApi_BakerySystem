package com.anakie.restApiBakery.controller;

import com.anakie.restApiBakery.entity.Payment;
import com.anakie.restApiBakery.entity.PaymentDTO;
import com.anakie.restApiBakery.exception.*;
import com.anakie.restApiBakery.service.OrderService;
import com.anakie.restApiBakery.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/payments")
@Slf4j
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @GetMapping("/{id}")
    public ResponseEntity<Payment> findById(@PathVariable Long id) throws PaymentNotFoundException {

            return new ResponseEntity<>(paymentService.findById(id), HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<List<Payment>> findAll() {

            return new ResponseEntity<>(paymentService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Payment> save(@RequestBody PaymentDTO paymentDTO) throws OrderNotFoundException, UserNotFoundException, InsufficientFundsException, ProductNotFoundException, AccountNotFoundException {

            return new ResponseEntity<>(paymentService.save(paymentDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) throws PaymentNotFoundException {

            paymentService.deleteById(id);
            return new ResponseEntity<>("Payment id: " + id + " successfully removed", HttpStatus.GONE);

    }
}
