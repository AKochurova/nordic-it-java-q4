package com.example.demo.cache;

import com.example.demo.botapi.BotState;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Aouth {
    private Map<String, String> usersCodes = new ConcurrentHashMap<>();
    public void setUsersCodes(String userId, String code) {
        usersCodes.put(userId, code);
    }
    public String getUsersCodes(String userId) {
        String answ = usersCodes.get(userId);
        return answ;
    }
}
