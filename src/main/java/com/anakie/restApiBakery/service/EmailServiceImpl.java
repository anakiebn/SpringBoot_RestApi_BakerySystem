package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.EmailDetails;
import com.anakie.restApiBakery.entity.Order;
import com.anakie.restApiBakery.entity.Payment;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
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
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    private SimpleMailMessage simpleMailMessage;
    private MimeMessageHelper mimeMessageHelper;
    private MimeMessage mimeMessage;

    private String code;
    @Value("${spring.mail.username}")
    private String sender;

//    @PostConstruct
//    public void init() {
//
//        mimeMessage = javaMailSender.createMimeMessage();
//        mimeMessageHelper = new MimeMessageHelper(mimeMessage);
//    }
    @Override
    public void registrationEmail(String userEmail, String username) {
        simpleMailMessage = new SimpleMailMessage();

        code=UUID.randomUUID().toString().substring(0,8);

        String subject = "Registration confirmation email";
        StringBuilder body = new StringBuilder();

        body.append("Hello ").append(username).append("\n\n")
                .append("Thanks for joining our bakery app, click the following link to activate your account.")
                .append("Link: http://localhost:8080/api/v1/registration/").append(code).append(" ");

        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body.toString());
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setTo(userEmail);


        javaMailSender.send(simpleMailMessage); // send the email
        
    }

    @Override
    public void passwordRecoveryEmail(String userEmail,String username) {

        code=UUID.randomUUID().toString().substring(0,8);
        String subject = "Password recovery service";
        StringBuilder body = new StringBuilder();

        body.append("Hello ").append(username).append("/n/n")
                .append("You've requested to change your password, click the following link to complete your operation")
                .append("Link: http://localhost:8080/api/v1/recover").append(code);

        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body.toString());
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setTo(userEmail);
        javaMailSender.send(simpleMailMessage);
    }

    @Override
    public void orderConfirmationEmail(Order order) throws MessagingException {

        mimeMessage = javaMailSender.createMimeMessage();
        mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

              StringBuilder body = new StringBuilder();
              body.append("Hello ").append(order.getUser().getUsername()).append("/n/n");

        String subject = "Bakery Order confirmation";
        mimeMessageHelper.setFrom(sender);
        mimeMessageHelper.setTo(order.getUser().getEmail());
        mimeMessageHelper.setText(body.toString());
        mimeMessageHelper.setSubject(subject);
        String attachment="";
        // Adding the attachment
        FileSystemResource file = new FileSystemResource(new File(attachment));

        mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);

        // Sending the mail
        javaMailSender.send(mimeMessage);
    }

    @Override
    public void paymentConfirmationEmail(Payment payment) throws MessagingException {
//        mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//        StringBuilder body = new StringBuilder();
//        String subject = "";
//        mimeMessageHelper.setFrom(sender);
//        mimeMessageHelper.setTo(payment.getOrder().getUser().getEmail());
//        mimeMessageHelper.setText(body.toString());
//        mimeMessageHelper.setSubject(subject);
//
//
//        String attachment="";
//        // Adding the attachment
//        FileSystemResource file = new FileSystemResource(new File(attachment));
//
//        mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
//
//        // Sending the mail
//        javaMailSender.send(mimeMessage);


    }
    public String getCode(){
        return code;
    }
}
