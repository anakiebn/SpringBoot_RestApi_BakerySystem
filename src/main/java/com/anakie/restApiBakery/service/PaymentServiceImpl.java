package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.*;
import com.anakie.restApiBakery.exception.*;
import com.anakie.restApiBakery.repository.*;
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
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    private final AccountStatusHistoryRepository accountStatusHistoryRepository;


    private final EmailService emailService;



    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, OrderService orderService,
                              AccountService accountService, PaymentStatusHistoryRepository paymentStatusHistoryRepository, OrderStatusHistoryRepository orderStatusHistoryRepository,
                              AccountStatusHistoryRepository accountStatusHistoryRepository,EmailService emailService) {
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
        this.accountService = accountService;
        this.paymentStatusHistoryRepository = paymentStatusHistoryRepository;
        this.orderStatusHistoryRepository = orderStatusHistoryRepository;
        this.accountStatusHistoryRepository = accountStatusHistoryRepository;
        this.emailService=emailService;
    }

    @Override
    public Payment save(PaymentDTO paymentDTO) throws OrderNotFoundException, ProductNotFoundException, InsufficientFundsException, UserNotFoundException, AccountNotFoundException, MessagingException {
        Payment payment = paymentDTO.toPayment(orderService);
        Order order = payment.getOrder();

        if (order == null || !orderService.existsById(order.getId())) {
            throw new OrderNotFoundException("Null order, payment must have order");
        }
        PAYMENT_METHOD current = payment.getPaymentMethod();

        // ensures that our payment method is valid
        if (!Arrays.asList(PAYMENT_METHOD.values()).contains(current)) {
            throw new InvalidPaymentMethod("Invalid payment method, Use either 'CARD', 'ACCOUNT' or 'BOTH'");
        }

        // here we pay depending on the chosen payment method
        switch (payment.getPaymentMethod()) {
            case BOTH -> payment = accountAndCard(payment);
            case CARD -> payment = cardPayment(payment);
            case ACCOUNT -> payment = accountPayment(payment);
        }
        OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setStatus(Status.In_Progress);  // after paying, we update order status to 'PAID'
        order.getOrderStatusHistories().add(orderStatusHistory);
        orderStatusHistory.setOrder(orderService.update(order)); // update order, let status reference it,
        orderStatusHistoryRepository.save(orderStatusHistory); // save it to database

        //create and send invoice
        emailService.invoiceEmail(payment);

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
    private Payment cardPayment(Payment payment) throws InsufficientFundsException, AccountNotFoundException, UserNotFoundException {
        Long userId = payment.getOrder().getUser().getId();
        double totalPrice = orderService.calculateTotal(payment.getOrder());
        Account account = accountService.findByUserId(userId);
        // tracks the payment status history
        PaymentStatusHistory paymentStatusHistory = new PaymentStatusHistory();


        // if we have insufficient funds... we throw
        if (payment.getAmount() < totalPrice) {
            throw new InsufficientFundsException("Insufficient funds! Required amount, R " + totalPrice + " but you provided R " + payment.getAmount());
        }

        // just a stub to simulate other reasons that might cause payment to fail
        if (new Random().nextBoolean()) {
            throw new PaymentFailedException("Payment failed, Try again, just a drill/stub error ");
        }

        // if they are paying more than they should, we give them a change
        if (payment.getAmount() > totalPrice) {
            accountService.fundAccount(account.getId(), payment.getAmount() - totalPrice); // we deposit the change to their app account,
            AccountStatusHistory accountStatusHistory = new AccountStatusHistory(); // with all these updates on account, we use status to flag changes made so far
            accountStatusHistory.setDateTime(LocalDateTime.now());
            accountStatusHistory.setAccount(account); // link the status the account
            accountStatusHistory.setStatus(Status.Got_Change);
            accountStatusHistoryRepository.save(accountStatusHistory);  // save the status to db
        }
        paymentStatusHistory.setStatus(Status.Successful);


        // if program is here means we have enough funds and successful payment
        // save payment to the  database so that we can get its ID, this enables use to link the status to payment

        // this method subtracts ingredients in database, since baking means using stock ingredients
        orderService.bakeProducts(payment.getOrder());
        paymentStatusHistory.setPayment(payment = paymentRepository.save(payment)); // link the status to payment, since it must reference the payment
        paymentStatusHistory.setDateTime(LocalDateTime.now());
        paymentStatusHistoryRepository.save(paymentStatusHistory); //save the status
        return payment;
    }

    private Payment accountPayment(Payment payment) {
        Long userId = payment.getOrder().getUser().getId();
        double totalPrice = orderService.calculateTotal(payment.getOrder());
        Account account = accountService.findByUserId(userId);

        PaymentStatusHistory paymentStatusHistory = new PaymentStatusHistory();


        // if we have enough funds
        if (account.getAmount() >= totalPrice) {

            AccountStatusHistory accountStatusHistory = new AccountStatusHistory();
            accountStatusHistory.setStatus(Status.Paid_With_Account); // when we pay from account
            accountStatusHistory.setDateTime(LocalDateTime.now());
            account.getAccountStatusHistories().add(accountStatusHistory);

            paymentStatusHistory.setStatus(Status.Successful);
            paymentStatusHistory.setDateTime(LocalDateTime.now());
            paymentStatusHistory.setPayment(payment = paymentRepository.save(payment)); // link the status to payment, since it must reference the payment
            paymentStatusHistoryRepository.save(paymentStatusHistory);

            account.setAmount(account.getAmount() - totalPrice);
            accountService.update(account);
        } else {
            throw new InsufficientFundsException("Insufficient funds on account, " + account.getId() + ", use other payment methods");
        }
        return payment;
    }

    private Payment accountAndCard(Payment payment) {
        Long userId = payment.getOrder().getUser().getId();
        double totalPrice = orderService.calculateTotal(payment.getOrder());
        Account account = accountService.findByUserId(userId);

        PaymentStatusHistory paymentStatusHistory = new PaymentStatusHistory();

        // just a stub to simulate other reasons that might cause payment to fail
        if (new Random().nextBoolean()) {
            throw new PaymentFailedException("Payment failed, Try again");
        }
        // if we have insufficient funds... we throw
        if (payment.getAmount() + account.getAmount() < totalPrice) {
            throw new InsufficientFundsException("Insufficient funds! Required amount, R " + totalPrice + " but you provided R " + payment.getAmount());
        }
        AccountStatusHistory accountStatusHistory = new AccountStatusHistory();
        accountStatusHistory.setStatus(Status.Partially); // when we pay from account
        accountStatusHistory.setDateTime(LocalDateTime.now());

        paymentStatusHistory.setStatus(Status.Successful);
        paymentStatusHistory.setDateTime(LocalDateTime.now());

        // link the status to payment, since it must reference the payment
        paymentStatusHistory.setPayment(payment = paymentRepository.save(payment));
        paymentStatusHistoryRepository.save(paymentStatusHistory);

        double change = (payment.getAmount() + account.getAmount()) - totalPrice;

        if (change > 0) {
            accountService.fundAccount(account.getId(), change);
            accountStatusHistory.setStatus(Status.Got_Change); // when we pay from account
        }
        // now update your account
        account.setAmount(change);
        accountService.update(account);

        return payment;
    }
}
