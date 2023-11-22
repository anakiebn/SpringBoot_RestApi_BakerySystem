package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Order;
import com.anakie.restApiBakery.entity.Payment;
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
    private RecordService recordService;

    private String code;
    @Value("${spring.mail.username}")
    private String sender;


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
        log.info("Email sent to "+userEmail);
        
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
        log.info("Email sent to "+userEmail);
    }

    @Override
    public void invoiceEmail(Payment payment) throws MessagingException {
        log.info("Creating invoice email");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        String body = "Download your invoice ## " + payment.getId()+"\nPayment method: "+payment.getPaymentMethod();


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
