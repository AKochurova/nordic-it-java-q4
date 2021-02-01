package com.example.demo.superjobapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Jobs {
    public static String getJobs(String message, int num, String town) throws IOException {

        String str = "https://api.superjob.ru/2.0/v3.r.133386385.0a3795f1baaab6cb9057bbd1af19f5b2ba967a72.c20cacaa5da55fe4c622288ac5fbbde1ed74759f/vacancies/?" + "town=" + town + "&keyword=" + message+"&count=5";
        URL url = new URL(str);

        Scanner scanner = new Scanner((InputStream) url.getContent());
        String result = "";
        result += scanner.nextLine();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(result);
        JsonNode objContent = root.path("objects");

        Model[] mods = mapper.readValue(objContent.toString(), Model[].class);

        StringBuilder stringBuilder = new StringBuilder(mods[num].getProfession()+"\n");

        if (!mods[num].getAddress().equals(null)){
            stringBuilder.append(mods[num].getAddress()).append("\n");
        }
        else stringBuilder.append("Адрес не указан\n");
        stringBuilder.append(mods[num].getFirm_name()).append("\n");
        stringBuilder.append(mods[num].getLink()).append("\n");

        return stringBuilder.toString();

    }
}
