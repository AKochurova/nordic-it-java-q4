package com.example.demo.superjobapi;

import com.example.demo.cache.Aouth;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Tokens {
    public static String getTokens(int userId) throws IOException {
        String str = "https://api.superjob.ru/2.0/oauth2/access_token/?code="+Aouth.getUsersCodes(""+userId)+"&redirect_uri=https://jobseeker-bot.herokuapp.com/getcode/"+userId+"&client_id=1599&client_secret=v3.r.133386385.0a3795f1baaab6cb9057bbd1af19f5b2ba967a72.c20cacaa5da55fe4c622288ac5fbbde1ed74759f";
        URL url = new URL(str);

        Scanner scanner = new Scanner((InputStream) url.getContent());
        String result = "";
        result += scanner.nextLine();

        JSONObject object = new JSONObject(result);
        String token = (String) object.get("access_token");
        return token;
    }
}
