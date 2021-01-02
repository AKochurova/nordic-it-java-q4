package com.example.demo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Jobs {
    public static String getJobs(String message, ArrayList<Model> arr, int num, String result) throws IOException {


        JSONObject object = new JSONObject(result);

        JSONArray array = object.getJSONArray("objects");

        JSONObject obj = array.getJSONObject(num);

        arr.get(num).setName((String) obj.get("profession"));
        arr.get(num).setUrl((String) obj.get("link"));
        if(!obj.get("address").equals(null))
            arr.get(num).setArea((String) obj.get("address"));
        else arr.get(num).setArea("не указано");
        arr.get(num).setFirm((String) obj.get("firm_name"));

        return arr.get(num).getName()+"\n"+arr.get(num).getArea()+"\n"+arr.get(num).getFirm()+"\n"+arr.get(num).getUrl()+"\n";

    }
}
