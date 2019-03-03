package com.example.weighthelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class FoodClient {
    private final String API_KEY = "6Ozn9qWkcB8rKQ9jtA6aVGQIsP1HMPkJNyvfR58A";
    private final String API_BASE_URL = "https://api.nal.usda.gov/ndb/";
    private AsyncHttpClient client;

    public FoodClient() {
        this.client = new AsyncHttpClient();
    }

    public void searchFood(String food, JsonHttpResponseHandler handler) {
        String foodUrl = "search/?format=json&q="+food+"&sort=n&max=25&offset=0&api_key="+API_KEY;
        String url = getApiUrl(foodUrl);
        client.get(url,handler);
    }

    public void searchNDBno(String ndbno, JsonHttpResponseHandler handler) {
        String foodUrl = "reports/?ndbno="+ndbno+"&type=b&format=json&api_key="+API_KEY;
        String url = getApiUrl(foodUrl);
        client.get(url,handler);
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }
}
