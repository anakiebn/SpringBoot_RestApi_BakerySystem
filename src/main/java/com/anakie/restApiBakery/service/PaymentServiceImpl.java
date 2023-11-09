package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Payment;
import com.anakie.restApiBakery.exception.PaymentNotFoundException;
import com.anakie.restApiBakery.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment save(Payment payment) {

        return paymentRepository.save(payment);
    }

    @Override
    public Payment findById(Long id) throws PaymentNotFoundException {
        return paymentRepository.findById(id).orElseThrow(() -> new PaymentNotFoundException("Payment " + id + " not found, use an existing id!"));
    }

    @Override
    public void deleteById(Long id) throws PaymentNotFoundException {
        if (!paymentRepository.existsById(id)) {
            throw new PaymentNotFoundException("Can't delete payment " + id + " not found, use an existing id!");
        }
        paymentRepository.deleteById(id);
    }

    @Override
    public Payment update(Payment payment) throws PaymentNotFoundException {
        if (!paymentRepository.existsById(payment.getId())) {
            throw new PaymentNotFoundException("Can't update payment " + payment.getId() + " not found, use an existing id!");
        }
        return paymentRepository.save(payment);
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }
}
