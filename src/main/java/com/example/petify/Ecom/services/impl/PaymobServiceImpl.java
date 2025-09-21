package com.example.petify.Ecom.services.impl;

import com.example.petify.Ecom.dto.PaymentDto;
import com.example.petify.Ecom.services.PaymobService;
import com.example.petify.config.AppConfig;
import com.example.petify.exception.PaymentGatewayException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class PaymobServiceImpl implements PaymobService {
    private final AppConfig appConfig;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;

    @Override
    public String authenticate() {
        var url = "https://accept.paymob.com/api/auth/tokens";
        Map<String, String> body = Map.of("api_key", appConfig.getPaymobApiKey());
        var response = restTemplate.postForEntity(url, body, Map.class);
        if  (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return (String) response.getBody().get("token");
        }else {
            throw new PaymentGatewayException("could not Auth payment gateway");
        }
    }

    @Override
    public Map generateIntentionPayment(PaymentDto payment) {
        String url = "https://accept.paymob.com/v1/intention/";

        // Body
        Map<String, Object> body = new HashMap<>();
        body.put("amount", payment.getAmountInCents());
        body.put("currency", payment.getCurrency());

        body.put("payment_methods",  payment.getPaymentMethods());

        Map billingMap = Map.of(
                "first_name", payment.getBillingData().getFirstName(),
                "last_name", payment.getBillingData().getLastName(),
                "phone_number", payment.getBillingData().getPhoneNumber(),
                "email", payment.getBillingData().getEmail()
        );
        body.put("billing_data", billingMap);

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Token " + appConfig.getPaymobSecretKey());

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        var resp = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                String.class
        );
        if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
            try{
                return objectMapper.readValue(resp.getBody(), Map.class);
            }catch (Exception e) {
                throw new PaymentGatewayException("could not parse payment response");
            }
        }else {
            throw new PaymentGatewayException("could not generate intention payment gateway");
        }
    }

    @Override
    public void refund(String transactionId, @NotNull Integer amountInCents) {
        String url = "https://accept.paymob.com/api/acceptance/void_refund/refund";
        // body
        Map <String, Object> body = new HashMap<>();
        body.put("transaction_id", transactionId);
        body.put("amount_cents", amountInCents);

        // headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Token " + appConfig.getPaymobSecretKey());

        var request = new HttpEntity<>(body, headers);
        var resp = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                String.class
        );
        if (!resp.getStatusCode().is2xxSuccessful()) {
            throw new PaymentGatewayException("could not generate intention payment gateway");
        }
    }

    @Override
    public Map getTransactionByTrnxOrderId(Long trnxOrderId) {
        var auth_token = this.authenticate();
        String url = "https://accept.paymob.com/api/ecommerce/orders/transaction_inquiry";

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Body
        Map <String, Object> body = new HashMap<>();
        body.put("auth_token", auth_token);
        body.put("order_id", trnxOrderId);

        var request = new HttpEntity<>(body, headers);
        var response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        }else {
            throw new PaymentGatewayException("could not get transaction.");
        }
    }
}
