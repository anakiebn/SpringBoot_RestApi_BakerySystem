package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Payment;
import com.anakie.restApiBakery.exception.DuplicateEmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import java.io.File;
import java.util.Objects;
import java.util.UUID;
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    private SimpleMailMessage simpleMailMessage;
    @Autowired
    private DocumentService recordService;

    @Autowired
    private UserService userService;

    private String code;
    @Value("${spring.mail.username}")
    private String sender;


    @Override
    public void registrationEmail(String userEmail, String username) throws DuplicateEmailException {

        if(userEmail==null || username==null){
            throw new NullPointerException("Null userEmail or username not allowed, provide none-null strings");
        }
        if(userService.existsByEmail(userEmail)){
            throw new DuplicateEmailException("Duplicate email not allowed, log in or use a new email");
        }
        simpleMailMessage = new SimpleMailMessage();

        code=UUID.randomUUID().toString().substring(0,8);

        String subject = "Registration confirmation email";

        String body = "Hello " + username + "\n\n" +
                "Thanks for joining our bakery app, click the following link to activate your account." +
                "Link: http://localhost:8080/api/v1/registration/" + code + " ";

        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setTo(userEmail);


        javaMailSender.send(simpleMailMessage); // send the email
        log.info("Email sent to "+userEmail);
        
    }

    @Override
    public void passwordRecoveryEmail(String userEmail,String username) {

        log.info("preparing password recovery email");
        code=UUID.randomUUID().toString().substring(0,8);
        String subject = "Password recovery service";

        String body = "Hello " + username + "/n/n" +
                "You've requested to change your password, click the following link to complete your operation" +
                "Link: http://localhost:8080/api/v1/recovery/" + code;

        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setTo(userEmail);
        javaMailSender.send(simpleMailMessage);
        log.info("Email sent to "+userEmail);
    }

    @Override
    public void invoiceEmail(Payment payment) throws MessagingException {
        log.info("Creating invoice email");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        String body = "Invoice number: " + payment.getId()+"\nPayment method: "+payment.getPaymentMethod();


        String subject = "Bakery Order Invoice";
        mimeMessageHelper.setFrom(sender);
        mimeMessageHelper.setTo(payment.getOrder().getUser().getEmail());
        mimeMessageHelper.setText(body);
        mimeMessageHelper.setSubject(subject);

        // Adding the attachment
        FileSystemResource file = new FileSystemResource(new File(recordService.invoice(payment.getOrder())));

        mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);

        // Sending the mail
        javaMailSender.send(mimeMessage);
        log.info("Email sent to "+payment.getOrder().getUser().getEmail());
    }
    public String getCode(){
        return code;
    }

}
