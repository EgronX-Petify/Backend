package com.example.petify.Ecom.services;

import com.fasterxml.jackson.databind.JsonNode;

public interface PaymobWebhook {
    void handleTransaction(JsonNode payload);
}
