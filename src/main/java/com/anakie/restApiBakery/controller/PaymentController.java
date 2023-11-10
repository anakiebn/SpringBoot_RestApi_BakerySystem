package com.anakie.restApiBakery.controller;

import com.anakie.restApiBakery.entity.Payment;
import com.anakie.restApiBakery.entity.PaymentDTO;
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
    public ResponseEntity<Payment> findById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(paymentService.findById(id), HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception occurred: " + ex.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Payment>> findAll() {
        try {
            return new ResponseEntity<>(paymentService.findAll(), HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception occurred: " + ex.getMessage());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping
    public ResponseEntity<Payment> save(@RequestBody PaymentDTO paymentDTO) {
        try {
            return new ResponseEntity<>(paymentService.save(paymentDTO), HttpStatus.CREATED);
        } catch (Exception ex) {
            log.error("Exception occurred: " + ex.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        try {
            paymentService.deleteById(id);
            return new ResponseEntity<>("Payment id: " + id + " successfully removed", HttpStatus.GONE);
        } catch (Exception ex) {
            log.error("Exception occurred: " + ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
