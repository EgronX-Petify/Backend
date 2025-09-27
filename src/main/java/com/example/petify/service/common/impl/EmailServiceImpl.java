package com.example.petify.service.common.impl;

import com.example.petify.service.common.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    public ResponseEntity<?> sendEmail(String From, String To, String Subject, String Body) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(From);
        mailMessage.setTo(To);
        mailMessage.setSubject(Subject);
        mailMessage.setText(Body);

        mailSender.send(mailMessage);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Email has been sent successfully");
    }
}
