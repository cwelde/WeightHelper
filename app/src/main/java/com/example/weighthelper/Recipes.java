package com.example.weighthelper;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Checkable;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Recipes extends AppCompatActivity {
    private RecipeClient client;
    private ArrayList<String> recipe_names;
    private List<String> ingredients;
    //private ListView recipe_results;
    private int list_size = 10;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_results);

        get_Recipes(20,20,20,500, "");
    }


    private void get_Recipes(int protein, int fat, int carbs, int calories, String diet) {
        String fatRange = Integer.toString(fat-5) +"-"+ Integer.toString(fat+5);
        String carbsRange = Integer.toString(carbs-5) +"-"+ Integer.toString(carbs+5);
        String protRange = Integer.toString(protein-5) +"-"+ Integer.toString(protein+5);
        String calRange = Integer.toString(calories-50) +"-"+ Integer.toString(calories+50);

        client = new RecipeClient();

        client.getRecipes(fatRange, carbsRange, protRange, calRange, diet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, final JSONObject responseBody) {
                final ListView all_recipes = findViewById(R.id.recipeList);

                try {
                    recipe_names = new ArrayList<String>();
                    for (int i = 0; i < list_size; i++) {
                        recipe_names.add(responseBody.getJSONArray("hits").getJSONObject(i).getJSONObject("recipe").getString("label"));
                        System.out.println(recipe_names.get(i));
                    }

                    ArrayAdapter adapter = new ArrayAdapter<String>(Recipes.this, R.layout.recipes_listview, recipe_names);
                    all_recipes.setAdapter(adapter);
                    all_recipes.setClickable(true);
                    all_recipes.setOnItemClickListener(

                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    try {
                                        JSONObject rec = responseBody.getJSONArray("hits").getJSONObject(position).getJSONObject("recipe");
                                        setContentView(R.layout.recipe_info);
                                        ingredients = Arrays.asList(rec.getString("ingredientLines"));
                                        ArrayAdapter ing_adapter = new ArrayAdapter<String>(Recipes.this, R.layout.ingredients_listview, ingredients);
                                        ListView recipe_ingredients = findViewById(R.id.ingredients);
                                        recipe_ingredients.setAdapter(ing_adapter);
                                        TextView recipe_name = findViewById(R.id.recipeName);
                                        recipe_name.setText(rec.getString("label"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}