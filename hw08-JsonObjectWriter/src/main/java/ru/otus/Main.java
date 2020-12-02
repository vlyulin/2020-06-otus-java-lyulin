package ru.otus;

import ru.otus.examples.AnyObject;
import com.google.gson.Gson;
import ru.otus.mygson.MyGson;

public class Main {
    public static void main(String[] args) {
        AnyObject anyObject = new AnyObject();
        Gson gson = new Gson();
        String json = gson.toJson(anyObject);
        System.out.println(json);

        MyGson myGson = new MyGson();
        String myJson = myGson.toJson(anyObject);
        System.out.println("myJson: " + myJson);

        System.out.println("- Is it equivalent?\n- " + json.equals(myJson));
    }
}
