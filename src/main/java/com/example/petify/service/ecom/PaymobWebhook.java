package com.example.petify.service.ecom;

import com.fasterxml.jackson.databind.JsonNode;

public interface PaymobWebhook {
    void handleTransaction(JsonNode payload);
}
