package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Payment;
import com.anakie.restApiBakery.exception.DuplicateEmailException;
import jakarta.mail.MessagingException;

public interface EmailService {

    void registrationEmail(String userEmail,String username) throws DuplicateEmailException;
    void passwordRecoveryEmail(String userEmail,String username);
    void invoiceEmail(Payment payment) throws MessagingException;
    String getCode();

}
