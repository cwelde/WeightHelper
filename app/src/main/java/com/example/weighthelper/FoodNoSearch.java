package com.example.weighthelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FoodNoSearch { //search with ndbno
    private String food; //name
    private String ndbno;
    private ArrayList<String> measurements; //default: 100 grams. measures: label
    private ArrayList<String> cals; //label: "name: "kcal"", measures: value. default: 100 grams


    public String getFood() {
        return food;
    }

    public String getNdbno() {
        return ndbno;
    }

    public ArrayList<String> getMeasurements() {
        return measurements;
    }

    public ArrayList<String> getCals() {
        return cals;
    }


    public static ArrayList<String> allMeasurement(JSONArray jsonArray) {
        ArrayList<String> measurements = new ArrayList<String>(jsonArray.length()+1);
        measurements.add("100 grams"); //default value for measurement
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject measure = null;
            String m = null;
            try {
                measure = jsonArray.getJSONObject(i); //entire measures block
                m = measure.getString("label");
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            if (m != null) {
                measurements.add(m);
            }
        }
        return measurements;
    }

    public static ArrayList<String> allCal(JSONArray jsonArray,ArrayList<String> calArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject cal = null;
            String c = null;
            try {
                cal = jsonArray.getJSONObject(i);
                c = cal.getString("value");
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            if (c != null) {
                calArray.add(c);
            }
        }
        return calArray;
    }

    public String chooseMeasurement() {
        return "";
    }

    public static FoodNoSearch fromJson(JSONObject jsonObject) { //label: measurement
        FoodNoSearch fns = new FoodNoSearch();
        try {
            JSONObject f = jsonObject.getJSONObject("food"); //food object
            fns.food = f.getString("name");
            fns.ndbno = f.getString("ndbno");
            JSONArray nutrients = f.getJSONArray("nutrients");
            JSONArray measurements = null;
            for (int i = 0; i < nutrients.length(); i++) {
                String name = nutrients.getJSONObject(i).getString("unit");
                if (name.equals("kcal")) {
                    measurements = nutrients.getJSONObject(i).getJSONArray("measures");
                    fns.cals = new ArrayList<>(measurements.length()+1);
                    fns.cals.add(nutrients.getJSONObject(i).getString("value")); //default calorie value for 100 grams
                    break;
                }
            }
            if (measurements != null) {
                fns.measurements = allMeasurement(measurements);
                fns.cals = allCal(measurements,fns.cals);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return fns;
    }
}
