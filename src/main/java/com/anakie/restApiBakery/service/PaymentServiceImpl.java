package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Payment;

import com.anakie.restApiBakery.exception.PaymentNotFoundException;
import com.anakie.restApiBakery.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentServiceImpl  implements PaymentService{

    @Autowired
    private PaymentRepository paymentRepository;
    @Override
    public Payment addPayment(Payment payment) {
        return paymentRepository.save(payment);
    }


    @Override
    public Payment getPaymentById(Long id) throws PaymentNotFoundException {
        return paymentRepository.findById(id).orElseThrow(()->new PaymentNotFoundException("Payment "+id+" not found, use an existing id!"));
    }

    @Transactional
    @Override
    public int deletePaymentById(Long id) throws PaymentNotFoundException {
        if(!paymentRepository.existsById(id)){
            throw new PaymentNotFoundException("Can't delete payment "+id+" not found, use an existing id!");
        }
        return paymentRepository.deletePaymentById(id);
    }

    @Transactional
    @Override
    public Payment updatePayment(Payment payment) throws PaymentNotFoundException {
        if(!paymentRepository.existsById(payment.getId())){
            throw new PaymentNotFoundException("Can't update payment "+payment.getId()+" not found, use an existing id!");
        }
        return paymentRepository.save(payment);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
