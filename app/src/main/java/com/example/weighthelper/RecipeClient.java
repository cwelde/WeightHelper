package com.example.weighthelper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class RecipeClient {
    private final String API_KEY = "edb94f2486d7222dd1d107251829bcea";
    private final String API_ID = "ab15b38f";
    private final String API_BASE_URL = "https://api.edamam.com/";
    private AsyncHttpClient client;

    public RecipeClient() {
        this.client = new AsyncHttpClient();
    }

    public void getRecipes(String fatRange, String carbsRange, String protRange, String calRange, String diet, JsonHttpResponseHandler handler) {
        String url = API_BASE_URL + "search?q=&FAT=" + fatRange + "&CHOCDF=" + carbsRange+"&PROCNT=" + protRange +
                "&from=0&to=10&calories=" + calRange + "&app_id=" + API_ID + "&app_key=" + API_KEY;
        if (!diet.equals("")){
            url += "&health="+diet;
        }
        client.get(url,handler);
    }
}

