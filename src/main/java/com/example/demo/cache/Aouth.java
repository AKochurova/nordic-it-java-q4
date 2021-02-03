package com.example.demo.cache;

import com.example.demo.botapi.BotState;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Aouth {
    private Map<Integer, String> usersCodes = new ConcurrentHashMap<>();
    public void setUsersCodes(int userId, String code) {
        usersCodes.put(userId, code);
    }
    public String getUsersCodes(int userId) {
        String answ = usersCodes.get(userId);
        return answ;
    }
}
