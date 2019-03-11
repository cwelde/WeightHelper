package com.example.weighthelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FoodNoSearch { //search with ndbno
    private String food; //name
    private String ndbno;
    private ArrayList<String> measurements; //default: 100 grams. measures: label
    private ArrayList<String> cals; //"unit: "kcal"", measures: value. default: 100 grams
    private ArrayList<String> proteins; //"name: "Protein"", measures: value. in grams
    private ArrayList<String> fats; //name: Total lipid (fat). in grams
    private ArrayList<String> carbs; //name: "Carbohydrate, by difference" in grams


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

    public ArrayList<String> getProteins() { return proteins; }

    public ArrayList<String> getFats() { return fats; }

    public ArrayList<String> getCarbs() { return carbs; }


    public static ArrayList<String> allMeasurement(JSONArray jsonArray) {
        ArrayList<String> measurements = new ArrayList<String>(jsonArray.length()+1);
        measurements.add("100 grams (default)"); //default value for measurement
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

    public static ArrayList<String> allNutrient(JSONArray jsonArray,ArrayList<String> oArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = null;
            String c = null;
            try {
                o = jsonArray.getJSONObject(i);
                c = o.getString("value");
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            if (c != null) {
                oArray.add(c);
            }
        }
        return oArray;
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
                String name2 = nutrients.getJSONObject(i).getString("name");
                measurements = nutrients.getJSONObject(i).getJSONArray("measures");
                fns.measurements = allMeasurement(measurements); //add measurements, same for every nutrient
                if (name.equals("kcal")) { //calories
                    fns.cals = new ArrayList<>(measurements.length()+1);
                    fns.cals.add(nutrients.getJSONObject(i).getString("value")); //default value for 100 grams
                    fns.cals = allNutrient(measurements,fns.cals);

                }

                if (name2.equals("Protein")) {
                    fns.proteins = new ArrayList<>(measurements.length()+1);
                    fns.proteins.add(nutrients.getJSONObject(i).getString("value")); //default value for 100 grams
                    fns.proteins = allNutrient(measurements,fns.proteins);
                }

                if (name2.equals("Total lipid (fat)")) {
                    fns.fats = new ArrayList<>(measurements.length()+1);
                    fns.fats.add(nutrients.getJSONObject(i).getString("value")); //default value for 100 grams
                    fns.fats = allNutrient(measurements,fns.fats);

                }

                if (name2.equals("Carbohydrate, by difference")) {
                    fns.carbs = new ArrayList<>(measurements.length()+1);
                    fns.carbs.add(nutrients.getJSONObject(i).getString("value")); //default value for 100 grams
                    fns.carbs = allNutrient(measurements,fns.carbs);

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return fns;
    }
}
