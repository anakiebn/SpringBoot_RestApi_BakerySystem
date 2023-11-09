package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Payment;
import com.anakie.restApiBakery.exception.PaymentNotFoundException;

import java.util.List;

public interface PaymentService {
    Payment save(Payment payments);
    Payment findById(Long id) throws PaymentNotFoundException;

    void deleteById(Long id) throws PaymentNotFoundException;

    Payment update(Payment payments) throws PaymentNotFoundException;

    List<Payment> findAll();
}
