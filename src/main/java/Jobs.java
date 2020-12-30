import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Jobs {
    public static String getJobs(String message, ArrayList<Model> arr, int num) throws IOException {
        //String str="https://api.hh.ru/vacancies?text="+message+"&area=113";
        String str="https://api.superjob.ru/2.0/v3.r.133386385.0a3795f1baaab6cb9057bbd1af19f5b2ba967a72.c20cacaa5da55fe4c622288ac5fbbde1ed74759f/vacancies/?keyword="+message;
        URL url = new URL(str);

        //ArrayList<Model> arr = new ArrayList<>();

        Scanner scanner = new Scanner((InputStream)url.getContent());
        String result = "";

        while (scanner.hasNext()){
            result += scanner.nextLine();
        }
        JSONObject object = new JSONObject(result);
        //JSONArray array = object.getJSONArray("items");
        JSONArray array = object.getJSONArray("objects");
        //for(int i=0; i< 5;i++){

            //Model m = new Model();
            //arr.add(m);

            JSONObject obj = array.getJSONObject(num);
            ///*model*/arr.get(i).setName((String) obj.get("name"));
            arr.get(num).setName((String) obj.get("profession"));
            arr.get(num).setUrl((String) obj.get("link"));
            if(!obj.get("address").equals(null))
            arr.get(num).setArea((String) obj.get("address"));
            else arr.get(num).setArea("не указано");
            arr.get(num).setFirm((String) obj.get("firm_name"));


            /*JSONObject area = obj.getJSONObject("area");
            arr.get(i).setArea((String) area.get("name"));
            JSONObject snippet = obj.getJSONObject("snippet");
            arr.get(i).setRequirement(snippet.get("requirement").toString());



            arr.get(i).setUrl(obj.get("url").toString());*/


        //}


            return arr.get(num).getName()+"\n"+arr.get(num).getArea()+"\n"+arr.get(num).getFirm()+"\n"+arr.get(num).getUrl()+"\n";

    }
}
