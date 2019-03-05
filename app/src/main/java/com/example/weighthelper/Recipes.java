package com.example.weighthelper;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;


public class Recipes {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_main);
    }

    public  Recipes(int protein, int fat, int carbs, int calories) {
        int minProt = protein - 3;
        int maxProt = protein + 3;
        int minFat = fat - 3;
        int maxFat = fat + 3;
        int minCarbs = carbs - 3;
        int maxCarbs = carbs + 3;
        int minCal = calories - 50;
        int maxCal = calories + 50;

        String url = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/findByNutrients?minCarbs="+minCarbs +
                "&maxCarbs="+maxCarbs+"&minProtein="+minProt+"&maxProtein="+maxProt+"&minCalories="+minCal+"&maxCalories="+maxCal +
                "&minFat="+minFat+"&maxFat="+maxFat;

        try {
            HttpResponse<JsonNode> response = Unirest.get(url)
                    .header("X-RapidAPI-Key", "1fd32effaamshe50509ebea5166bp10fc08jsna2a972da603f")
                    .asJson();
        }
        catch(UnirestException e){
            e.printStackTrace();
        }


    }
}
