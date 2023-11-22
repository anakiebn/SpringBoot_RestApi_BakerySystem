package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Payment;
import com.anakie.restApiBakery.entity.PaymentDTO;
import com.anakie.restApiBakery.exception.*;
import jakarta.mail.MessagingException;

import java.util.List;

public interface PaymentService {
    Payment save(PaymentDTO paymentsDTO) throws OrderNotFoundException, ProductNotFoundException, InsufficientFundsException, UserNotFoundException, AccountNotFoundException, MessagingException;
    Payment findById(Long id) throws PaymentNotFoundException;

    void deleteById(Long id) throws PaymentNotFoundException;

    Payment update(Payment payments) throws PaymentNotFoundException;

    List<Payment> findAll();
}
