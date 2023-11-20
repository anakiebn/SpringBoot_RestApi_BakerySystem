package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.EmailDetails;
import com.anakie.restApiBakery.entity.Order;
import com.anakie.restApiBakery.entity.Payment;
import jakarta.mail.MessagingException;

public interface EmailService {

    void registrationEmail(String userEmail,String username);
    void passwordRecoveryEmail(String userEmail,String username);
    void orderConfirmationEmail(Order order) throws MessagingException;
    void paymentConfirmationEmail(Payment payment) throws MessagingException;
    String getCode();

}
