package com.example.demo.cache;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Aouth {
    public static Map<String, String> usersCodes = new ConcurrentHashMap<>();
    public static void setUsersCodes(String userId, String code) {
        usersCodes.put(userId, code);
    }
    public static String getUsersCodes(String userId) {
        String answ = usersCodes.get(userId);
        return answ;
    }
}
