package com.example.petify.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    private int refreshExpirationSec;
    private boolean isDev;

    private String paymobBaseUrl;
    private String paymobHmacKey;
    private String paymobApiKey;
    private String paymobSecretKey;
    private String paymobPublicKey;
    private String PaymobIntegrationId;
    private String webhookUrl;

}
