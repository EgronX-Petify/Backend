package com.example.petify.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class  SecureTokenGen {
    public static String generate() {
        byte[] randomBytes = new byte[64];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
