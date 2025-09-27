package com.example.petify.service.common;

import org.springframework.http.ResponseEntity;

public interface EmailService {
    ResponseEntity<?> sendEmail(String From,String To, String Subject, String Body);
}
