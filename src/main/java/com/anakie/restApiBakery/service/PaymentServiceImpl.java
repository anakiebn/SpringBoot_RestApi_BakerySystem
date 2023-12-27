package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.*;
import com.anakie.restApiBakery.exception.*;
import com.anakie.restApiBakery.repository.AccountStatusHistoryRepository;
import com.anakie.restApiBakery.repository.OrderStatusHistoryRepository;
import com.anakie.restApiBakery.repository.PaymentRepository;
import com.anakie.restApiBakery.repository.PaymentStatusHistoryRepository;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final AccountService accountService;
    private final PaymentStatusHistoryRepository paymentStatusHistoryRepository;


    private final EmailService emailService;


    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, OrderService orderService, AccountService accountService, PaymentStatusHistoryRepository paymentStatusHistoryRepository, OrderStatusHistoryRepository orderStatusHistoryRepository, AccountStatusHistoryRepository accountStatusHistoryRepository, EmailService emailService) {
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
        this.accountService = accountService;
        this.paymentStatusHistoryRepository = paymentStatusHistoryRepository;
        this.emailService = emailService;
    }

    @Override
    public Payment save(PaymentDTO paymentDTO) throws OrderNotFoundException, ProductNotFoundException, InsufficientFundsException, UserNotFoundException, AccountNotFoundException, MessagingException {
        Payment payment = paymentDTO.toPayment(orderService);
        Order order = payment.getOrder();

        if (order == null || !orderService.existsById(order.getId())) {
            throw new OrderNotFoundException("Payment failed, Null/ Invalid order not allowed");
        }
        if (paymentRepository.findAll().stream().anyMatch(eachPayment -> eachPayment.getOrder().getId().equals(order.getId()))) {
            throw new PaymentFailedException("Payment failed, Order already paid for");
        }

        PAYMENT_METHOD currentPaymentMethod = payment.getPaymentMethod();

        // ensures that our payment method is valid
        if (!Arrays.asList(PAYMENT_METHOD.values()).contains(currentPaymentMethod)) {
            throw new InvalidPaymentMethod("Invalid payment method, Use either 'CARD', 'ACCOUNT' or 'ACCOUNT_AND_CARD'");
        }
        // here we pay depending on the chosen payment method
        switch (currentPaymentMethod) {
            case ACCOUNT_AND_CARD -> payment = accountAndCard(payment);
            case CARD -> payment = cardPayment(payment);
            case ACCOUNT -> payment = accountPayment(payment);
        }

        try {
            orderService.bakeProducts(payment.getOrder()); // this method subtracts ingredients in database, since baking means using stock ingredients
            orderService.changeOrderStatus(order, Status.Preparing);  // after paying we change order status to `preparing`
            try {
                emailService.invoiceEmail(payment); // create and send invoice
            } catch (Exception ex) {
                throw new PaymentFailedException("Payment failed due to: " + ex.getMessage());
            }
            return payment;
        } catch (OutOfStockException ex) {
            throw new PaymentFailedException("Payment failed due to: " + ex.getMessage());
        }


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
    public Payment update(PaymentDTO paymentDTO) throws PaymentNotFoundException {
        if (!paymentRepository.existsById(paymentDTO.toPayment(orderService).getId())) {
            throw new PaymentNotFoundException("Can't update payment " + paymentDTO.toPayment(orderService).getId() + " not found, use an existing id!");
        }
        return paymentRepository.save(paymentDTO.toPayment(orderService));
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    // if the user pays from their back accounts not the app account, we call this method
    private Payment cardPayment(Payment payment) {

        Long userId = payment.getOrder().getUser().getId();
        double totalPrice = payment.getOrder().getTotalPrice();
        Account account = accountService.findByUserId(userId);

        // if we have insufficient funds... we throw
        if (payment.getAmount() < totalPrice) {
            throw new InsufficientFundsException("Insufficient funds! Required amount, R " + totalPrice + " but you provided R " + payment.getAmount());
        }


        // if they are paying more than they should, we give them a change
        if (payment.getAmount() > totalPrice) {
            accountService.fundAccount(account, payment.getAmount() - totalPrice, Status.Got_Change); // we deposit the change to their app account,
        }

        // if program is here means we have enough funds and successful payment
        // save payment to the  database so that we can get its ID, this enables use to link the status to payment
      return  changePaymentStatus(payment = paymentRepository.save(payment), Status.Successful); // link the status to payment, since it must reference the payment
    }

    private Payment accountPayment(Payment payment) {
        Long userId = payment.getOrder().getUser().getId();
        double totalPrice = payment.getOrder().getTotalPrice();
        Account account = accountService.findByUserId(userId);

        // if we have enough funds
        if (account.getAmount() >= totalPrice) {

            // reducing the money in the user app account
            // operation 1= means we're paying with a card and account
            // operation 0= means we're paying with an account only
            try {
                accountService.useFunds(account, totalPrice, Status.Paid_With_Account, 0);
            } catch (Exception e) {
                throw new InvalidPaymentMethod(e.getMessage());
            }

            changePaymentStatus(payment = paymentRepository.save(payment), Status.Successful);  // link the status to payment, but save payment 1st to get its id

        } else {
            throw new InsufficientFundsException("Insufficient funds on account, " + account.getId() + ", use other payment methods");
        }
        return payment;
    }

    private Payment accountAndCard(Payment payment) {

        Long userId = payment.getOrder().getUser().getId();
        double totalPrice = payment.getOrder().getTotalPrice();
        Account account = accountService.findByUserId(userId);

        // just a stub to simulate other reasons that might cause payment to fail
        if (new Random().nextBoolean()) {
            throw new PaymentFailedException("Payment failed, Try again");
        }
        // if we have insufficient funds... we throw an exception
        if ((payment.getAmount() + account.getAmount()) < totalPrice) {
            throw new InsufficientFundsException("Insufficient funds! Required amount, R " + totalPrice + " but you provided R " + payment.getAmount());
        }

        double leftMoney = totalPrice - payment.getAmount(); // we pay some cash with card money, what's left will be paid from app account money
        // reducing the money in the user app account
        // operation 1= means we're paying with a card and account
        // operation 0= means we're paying with an account only
        //  Status.partially means we paid some cash with account and some with card
        try {
            accountService.useFunds(account, leftMoney, Status.Partially, 1);
        } catch (Exception e) {
            throw new InvalidPaymentMethod(e.getMessage()); // I'll need to come back and fix how I handle this part.

        }

        changePaymentStatus(payment = paymentRepository.save(payment), Status.Successful); // link the status to payment, since it must reference the payment

        return payment;
    }

    @Override
    public Payment changePaymentStatus(Payment payment, Status status) {
        PaymentStatusHistory paymentStatusHistory = new PaymentStatusHistory();
        paymentStatusHistory.setPayment(payment);
        paymentStatusHistory.setStatus(status);
        paymentStatusHistory.setDateTime(LocalDateTime.now());
        payment.getPaymentStatusHistories().add( paymentStatusHistoryRepository.save(paymentStatusHistory));
        return payment;
    }
}
