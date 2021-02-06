package com.example.demo.cache;


import com.example.demo.botapi.BotState;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Aouth {
   /* public static Map<String, String> usersCodes = new ConcurrentHashMap<>();
    public static void setUsersCodes(String userId, String code) {
        usersCodes.put(userId, code);
    }
    public static String getUsersCodes(String userId) {
        return usersCodes.get(userId);
    }*/
   public static Map<Integer, UserAouthData> userAouth = new ConcurrentHashMap<>();

    public UserAouthData getUserAouth(int userId) {
        return userAouth.get(userId);
    }

    public void setUserAouth(int userId, UserAouthData userAouthData) {
        userAouth.put(userId, userAouthData);
    }
}
