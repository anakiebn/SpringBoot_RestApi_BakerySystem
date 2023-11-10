package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Payment;
import com.anakie.restApiBakery.entity.PaymentDTO;
import com.anakie.restApiBakery.exception.InsufficientFundsException;
import com.anakie.restApiBakery.exception.OrderNotFoundException;
import com.anakie.restApiBakery.exception.PaymentNotFoundException;
import com.anakie.restApiBakery.exception.ProductNotFoundException;

import java.util.List;

public interface PaymentService {
    Payment save(PaymentDTO paymentsDTO) throws OrderNotFoundException, ProductNotFoundException, InsufficientFundsException;
    Payment findById(Long id) throws PaymentNotFoundException;

    void deleteById(Long id) throws PaymentNotFoundException;

    Payment update(Payment payments) throws PaymentNotFoundException;

    List<Payment> findAll();
}
