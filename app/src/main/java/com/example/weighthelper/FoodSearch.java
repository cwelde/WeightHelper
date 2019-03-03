package com.example.weighthelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FoodSearch {
    private int length; //number of search items returned
    private String food; //search item
    private ArrayList<String> names;
    private ArrayList<String> ndbnos;

    public int getLength() {
        return length;
    }

    public String getFood() {
        return food;
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public ArrayList<String> getNdbnos() {
        return ndbnos;
    }

    public static FoodSearch fromJson(JSONObject jsonObject) {
        FoodSearch fs = new FoodSearch();
        try {
            JSONObject l = jsonObject;
            fs.length = l.getInt("end");
            fs.food = l.getString("q");
            fs.names = new ArrayList<String>();
            fs.ndbnos = new ArrayList<String>();
            JSONArray items = l.getJSONArray("item");
            for (int i = 0; i < items.length(); i++) {
                fs.names.add(items.getJSONObject(i).getString("name"));
                fs.ndbnos.add(items.getJSONObject(i).getString("ndbno"));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return fs;
    }
}
