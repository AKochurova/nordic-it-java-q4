package com.example.demo.superjobapi;

import com.example.demo.cache.Aouth;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Slf4j
public class Tokens {
    public static String getTokens(int userId, Message message) throws IOException {
        String str = "https://api.superjob.ru/2.0/oauth2/access_token/?code="+Aouth.getUsersCodes(""+userId)+"&redirect_uri=https://jobseeker-bot.herokuapp.com/getcode/"+userId+"&client_id=1599&client_secret=v3.r.133386385.0a3795f1baaab6cb9057bbd1af19f5b2ba967a72.c20cacaa5da55fe4c622288ac5fbbde1ed74759f";
        URL url = new URL(str);

        Scanner scanner = new Scanner((InputStream) url.getContent());
        String result = "";
        result += scanner.nextLine();

        JSONObject object = new JSONObject(result);
        String token = (String) object.get("access_token");


        URL url2 = new URL("https://api.superjob.ru/2.0/user/current");
        //URL url2 = new URL("https://api.superjob.ru/2.0/favorites/"+message+"/");
        HttpURLConnection con = (HttpURLConnection) url2.openConnection();
        con.setRequestMethod("GET");
        //con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Bearer "+token);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);

        }
        log.info("Info: " + content.toString());
        in.close();
        con.disconnect();


        return content.toString();
    }
}
