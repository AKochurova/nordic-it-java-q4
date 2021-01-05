package com.example.demo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Jobs {
    public static String getJobs(String message,  String town) throws IOException {

        String str="https://api.superjob.ru/2.0/v3.r.133386385.0a3795f1baaab6cb9057bbd1af19f5b2ba967a72.c20cacaa5da55fe4c622288ac5fbbde1ed74759f/vacancies/?"+"town="+town+"&keyword="+message;
        URL url = new URL(str);


        ArrayList<Model> arr = new ArrayList<>();
        for(int i=0; i<5; i++){
            Model m = new Model();
            arr.add(m);
        }
        Scanner scanner = new Scanner((InputStream)url.getContent());
        String result = "";
        String answer = "";


        result += scanner.nextLine();

        JSONObject object = new JSONObject(result);

        JSONArray array = object.getJSONArray("objects");



        for (int i=0; i<arr.size(); i++) {

            JSONObject obj = array.getJSONObject(i);
            arr.get(i).setName((String) obj.get("profession"));
            arr.get(i).setUrl((String) obj.get("link"));
            if (!obj.get("address").equals(null))
                arr.get(i).setArea((String) obj.get("address"));
            else arr.get(i).setArea("не указано");
            arr.get(i).setFirm((String) obj.get("firm_name"));

            answer+=arr.get(i).getName()+"\n"+arr.get(i).getArea()+"\n"+arr.get(i).getFirm()+"\n"+arr.get(i).getUrl()+"\n"+"\n";
        }
        return answer;

    }
}
