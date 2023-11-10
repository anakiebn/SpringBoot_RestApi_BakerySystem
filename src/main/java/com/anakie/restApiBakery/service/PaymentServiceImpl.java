package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.*;
import com.anakie.restApiBakery.exception.InsufficientFundsException;
import com.anakie.restApiBakery.exception.OrderNotFoundException;
import com.anakie.restApiBakery.exception.PaymentNotFoundException;
import com.anakie.restApiBakery.exception.ProductNotFoundException;
import com.anakie.restApiBakery.repository.PaymentRepository;
import com.anakie.restApiBakery.repository.PaymentStatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private PaymentStatusHistoryRepository paymentStatusHistoryRepository;

    @Override
    public Payment save(PaymentDTO paymentDTO) throws OrderNotFoundException, ProductNotFoundException, InsufficientFundsException {
        // ensure that we have valid order
        Payment payment=paymentDTO.toPayment(orderService);

        if (payment.getOrder() == null || !orderService.existsById(payment.getOrder().getId())) {
            assert payment.getOrder() != null;
            throw new OrderNotFoundException("Order id= "+payment.getOrder().getId()+" not found, payment must have order");
        }
        Order order = payment.getOrder();

        // checks if the funds are sufficient
        if (payment.getAmount() < order.getTotalPrice()) {
            throw new InsufficientFundsException("Insufficient funds! Required amount, R " + order.getTotalPrice() + " but you provided R " + payment.getAmount());
        }

        PaymentStatusHistory paymentStatusHistory = new PaymentStatusHistory();

        // save payment to db so we can have its id
        payment = paymentRepository.save(payment);

        // if they are paying more than they should, we give them a change
        if (payment.getAmount() > order.getTotalPrice()) {
            refundUser(payment.getAmount() - order.getTotalPrice());
            paymentStatusHistory.setStatus(Status.SETTLED_WITH_CHANGE);
        }

        // if the amount paid is equal to the orders' total price,
        if (payment.getAmount() == order.getTotalPrice()) {
            paymentStatusHistory.setStatus(Status.SETTLED);
        }

        // then link the statuses to a payment
        // will automatically link the payment to statuses in visa-versa
        paymentStatusHistory.setPayment(payment);
        paymentStatusHistory.setDateTime(LocalDateTime.now());

        payment.getPaymentStatusHistory().add(paymentStatusHistoryRepository.save(paymentStatusHistory));
        //we now return the payment
        return payment;
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

    public void refundUser(double amount) {

    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }
}
