package com.skillerwhale.wordle.lib;

import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cookies {
    private final HttpExchange exchange;
    private final Map<String, String> cookies;

    public Cookies(HttpExchange exchange) {
        this.exchange = exchange;
        this.cookies = parseCookies(exchange);
    }

    public String get(String name) {
        return cookies.get(name);
    }

    public void set(String name, String value) {
        cookies.put(name, value);
        // Set the cookie in the response headers
        String cookieHeader = name + "=" + value + "; Path=/; HttpOnly";
        exchange.getResponseHeaders().add("Set-Cookie", cookieHeader);
    }

    public String getOrCreateSessionId() {
        String sessionId = get("sessionId");
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            set("sessionId", sessionId);
        }
        return sessionId;
    }

    private Map<String, String> parseCookies(HttpExchange exchange) {
        Map<String, String> cookieMap = new HashMap<>();

        String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");
        if (cookieHeader != null) {
            String[] cookies = cookieHeader.split(";");
            for (String cookie : cookies) {
                String[] parts = cookie.trim().split("=", 2);
                if (parts.length == 2) {
                    cookieMap.put(parts[0], parts[1]);
                }
            }
        }

        return cookieMap;
    }
}
