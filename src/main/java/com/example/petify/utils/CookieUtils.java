package com.example.petify.utils;


import jakarta.servlet.http.Cookie;

import java.util.Arrays;

public class CookieUtils {
    public static String getCookieValue(Cookie[] cookie, String cookieName) {
        return Arrays.stream(cookie)
                .filter(c -> cookieName.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No refresh token"));
    }
    public static Cookie createCookie(String name, String value, String path, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // TODO: for development (make true in PROD)
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        return cookie;
    }
}
