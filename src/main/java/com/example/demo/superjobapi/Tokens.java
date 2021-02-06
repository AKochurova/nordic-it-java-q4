package com.example.demo.superjobapi;

import com.example.demo.cache.Aouth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Slf4j
public class Tokens {
    public static String getTokens(/*int userId*/String login, String password) throws IOException {
        /*String str = "https://api.superjob.ru/2.0/oauth2/access_token/?code="+Aouth.getUsersCodes(""+userId)+"&redirect_uri=https://jobseeker-bot.herokuapp.com/getcode/"+userId+"&client_id=1599&client_secret=v3.r.133386385.0a3795f1baaab6cb9057bbd1af19f5b2ba967a72.c20cacaa5da55fe4c622288ac5fbbde1ed74759f";
        URL url = new URL(str);

        Scanner scanner = new Scanner((InputStream) url.getContent());
        String result = "";
        result += scanner.nextLine();

        JSONObject object = new JSONObject(result);
        String token = (String) object.get("access_token");

        return token;*/
        String str = "https://api.superjob.ru/2.0/oauth2/password/?login="+login+"&password="+password+"&client_id=1599&client_secret=v3.r.133386385.0a3795f1baaab6cb9057bbd1af19f5b2ba967a72.c20cacaa5da55fe4c622288ac5fbbde1ed74759f";
        URL url = new URL(str);

        Scanner scanner = new Scanner((InputStream) url.getContent());
        String result = "";
        result += scanner.nextLine();

        JSONObject object = new JSONObject(result);
        String token = (String) object.get("access_token");
        log.info("Token: "+token);

        return token;
    }
    public static boolean getFavs(String login, String password, String idFav) throws IOException{
        URL url2 = new URL("https://api.superjob.ru/2.0/favorites/"+idFav+"/");
        HttpURLConnection con = (HttpURLConnection) url2.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Bearer "+getTokens(login, password));

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);

        }
        log.info("Info: " + content.toString());
        in.close();
        con.disconnect();

        JSONObject object2 = new JSONObject(content.toString());
        Boolean res = (Boolean) object2.get("result");
        boolean answer=true;
        if (!res)
            answer=false;

        return answer;
    }
    public static String getFavsList(/*int userId*/String login, String password) throws IOException{
        URL url3 = new URL("https://api.superjob.ru/2.0/favorites/");
        HttpURLConnection con = (HttpURLConnection) url3.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer "+getTokens(login, password));

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);

        }
        in.close();
        con.disconnect();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(content.toString());
        JsonNode objContent = root.path("objects");

        FavVacancy[] vacancies = mapper.readValue(objContent.toString(), FavVacancy[].class);

        StringBuilder string = new StringBuilder();

        for (FavVacancy i : vacancies){
            string.append(i.getProfession()).append("\n").append(i.getLink()).append("\n");
        }
        return string.toString();
    }

}
