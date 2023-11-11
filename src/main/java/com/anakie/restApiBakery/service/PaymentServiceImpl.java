package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.*;
import com.anakie.restApiBakery.exception.*;
import com.anakie.restApiBakery.repository.AccountStatusHistoryRepository;
import com.anakie.restApiBakery.repository.OrderStatusHistoryRepository;
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
    private AccountService accountService;
    @Autowired
    private PaymentStatusHistoryRepository paymentStatusHistoryRepository;
    @Autowired
    private OrderStatusHistoryRepository orderStatusHistoryRepository;
    @Autowired
    private AccountStatusHistoryRepository accountStatusHistoryRepository;

    @Override
    public Payment save(PaymentDTO paymentDTO) throws OrderNotFoundException, ProductNotFoundException, InsufficientFundsException, UserNotFoundException, AccountNotFoundException {

        Payment payment = paymentDTO.toPayment(orderService);
        Order order = payment.getOrder();

        if (order == null || !orderService.existsById(order.getId())) {
            throw new OrderNotFoundException("Null order, payment must have order");
        }

        Account account = accountService.findByUserId(order.getUser().getId());

        switch (payment.getPaymentMethod()) {
            case BOTH -> payment = bothPayment(payment, order.getTotalPrice(), account.getId());
            case CARD -> payment = cardPayment(payment, order.getTotalPrice(), account.getId());
            case ACCOUNT -> payment = accountPayment(payment, order.getTotalPrice(), account.getId());
        }
        OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setStatus(Status.PAID);  // after paying, we update order status to 'PAID'
        order.getOrderStatusHistories().add(orderStatusHistory);
        orderStatusHistory.setOrder(orderService.update(order)); // update order, let status reference it,
        orderStatusHistoryRepository.save(orderStatusHistory); // save it to database
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

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    //if the user pays from their back accounts, we call this method
    private Payment cardPayment(Payment payment, double orderTotalPrice, Long accountId) throws InsufficientFundsException, AccountNotFoundException, UserNotFoundException {
        Long userId=payment.getOrder().getUser().getId();
        // checks if the funds are sufficient
        if (payment.getAmount() < orderTotalPrice) {
            throw new InsufficientFundsException("Insufficient funds! Required amount, R " + orderTotalPrice + " but you provided R " + payment.getAmount());
        }

        // tracks the payment status history
        PaymentStatusHistory paymentStatusHistory = new PaymentStatusHistory();

        // if they are paying more than they should, we give them a change
        if (payment.getAmount() > orderTotalPrice) {
            // we deposit the change to their app account,
            accountService.fundAccount(accountId, payment.getAmount() - orderTotalPrice);
            // with all these updates on account, we use status to flag changes made so far
            AccountStatusHistory accountStatusHistory=new AccountStatusHistory();
            accountStatusHistory.setDateTime(LocalDateTime.now());
            accountStatusHistory.setAccount(accountService.findByUserId(userId));
            accountStatusHistory.setStatus(Status.FUNDED);
            // save the status to db
            accountStatusHistoryRepository.save(accountStatusHistory);
            paymentStatusHistory.setStatus(Status.SETTLED_WITH_CHANGE);
        }

        // if the amount paid is equal to the orders' total price,
        if (payment.getAmount() == orderTotalPrice) {
            paymentStatusHistory.setStatus(Status.SETTLED);
        }

        payment = paymentRepository.save(payment); // save payment so we can get its ID
        paymentStatusHistory.setPayment(payment); // link the status to payment, since it must reference the payment
        paymentStatusHistory.setDateTime(LocalDateTime.now());
        paymentStatusHistoryRepository.save(paymentStatusHistory); //save the status
        return payment;
    }

    public Payment accountPayment(Payment payment, double orderTotalPrice, Long accountId) {
        return payment;
    }

    public Payment bothPayment(Payment payment, double orderTotalPrice, Long accountId) {
        return payment;
    }
}
