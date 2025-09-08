package com.example.petify.common.services;

import org.springframework.http.ResponseEntity;

public interface EmailService {
    ResponseEntity<?> sendEmail(String From,String To, String Subject, String Body);
}
