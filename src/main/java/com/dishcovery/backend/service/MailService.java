package com.dishcovery.backend.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    public void sendMail(String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Verify Your Email");
            message.setFrom("jonjina7@gmail.com");
            String messageBody = """
                    Thank you for the sign up !
                   \s
                      `http://localhost:8080/register/verify-email?token=%s`            \s
                       \s
                   \s""".formatted(token);
            message.setText(messageBody);
            javaMailSender.send(message);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
