package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Payment;
import com.anakie.restApiBakery.exception.PaymentNotFoundException;

import java.util.List;

public interface PaymentService {
    Payment addPayment(Payment payments);
    Payment getPaymentById(Long id) throws PaymentNotFoundException;

    int deletePaymentById(Long id) throws PaymentNotFoundException;

    Payment updatePayment(Payment payments) throws PaymentNotFoundException;

    List<Payment> getAllPayments();
}
