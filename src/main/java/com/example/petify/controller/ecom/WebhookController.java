package com.example.petify.Ecom.controller;

import com.example.petify.Ecom.dto.PaymentDto;
import com.example.petify.Ecom.services.PaymobWebhook;
import com.example.petify.config.AppConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("api/v1/webhooks")
@RequiredArgsConstructor
public class WebhookController {
    private final AppConfig appConfig;
    private final PaymobWebhook paymobWebhook;

    @PostMapping("/paymob")
    public ResponseEntity<?> handlePaymobWebhook(
            @RequestParam(name = "hmac", required = true) String hmac,
            @RequestBody String payload) throws Exception {

        // verify the request
        if (!verifyHmac(hmac, payload)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid HMAC");
        }

        // parse json payload
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(payload);

        String req_type = json.path("type").asText();

        if (req_type.equals("TRANSACTION")) {
            paymobWebhook.handleTransaction(json);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("webhook sent successfully");
    }

    private boolean verifyHmac(String payload, String receivedHmac) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha512Hmac = Mac.getInstance("HmacSHA512");
        SecretKeySpec keySpec = new SecretKeySpec(appConfig.getPaymobHmacKey().getBytes(), "HmacSHA512");
        sha512Hmac.init(keySpec);

        byte[] hmacBytes = sha512Hmac.doFinal(payload.getBytes());
        String calculatedHmac = bytesToHex(hmacBytes);

        return calculatedHmac.equalsIgnoreCase(receivedHmac);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
