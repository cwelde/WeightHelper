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
import java.util.Calendar;
import java.util.List;

import java.time.LocalDateTime;


public class Recipes extends AppCompatActivity {
    private RecipeClient client;
    private ArrayList<String> recipe_names;
    private List<String> ingredients;
    private int calories_needed;
    //private ListView recipe_results;
    private int list_size = 10;
    private String cals_range;
    private String carbs_range;
    private String fat_range;
    private String protein_range;
    private String diet = "";
    private int totalCalories;
    private int totalCarbs;
    private int totalFats;
    private int totalProteins;
    private int goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        setContentView(R.layout.recipe_results);

        if (extras != null) { //restoring data when switching between activities
            if (extras.containsKey("foodBundle")) {
                Bundle fBundle = extras.getBundle("foodBundle");
                totalCalories = Integer.parseInt(fBundle.getString("cals"));
                totalCarbs = Integer.parseInt(fBundle.getString("carbs"));
                totalFats = Integer.parseInt(fBundle.getString("fats"));
                totalProteins = Integer.parseInt(fBundle.getString("proteins"));
            }
            if (extras.containsKey("userBundle")) {
                Bundle uBundle = extras.getBundle("userBundle");
                goal = uBundle.getInt("goal");
            }
        }

        compRecipeNutrients(); // Creating the strings for nutrients range
        get_Recipes(); // Getting recipes from API
    }

    private int findMeal(){ // 3 = breakfast, 2 = lunch, 1 = dinner
        Calendar calendar = Calendar.getInstance();
        int time = calendar.get(Calendar.HOUR_OF_DAY);
        if (time > 5 && time < 11)
            return 3;
        else if (time >= 11 && time < 4)
            return 2;
        else
            return 1;
    }

    private void compRecipeNutrients(){
        int goal_fat = (goal/2)/(9); // 1/2 of daily calories from far, each calorie 1/9 gram of fat
        int goal_carbs = (goal/4)/4; // 1/4 of daily calories from carbs, each calorie 1/4 gram of fat
        int goal_protein = (goal/4)/4; // 1/4 of daily calories from protein, each calorie 1/4 gram of protein
        int meal = findMeal();
        int calories = (goal-totalCalories)/meal;
        int fat = (goal_fat-totalFats)/meal;
        int carbs = (goal_carbs-totalCarbs)/meal;
        int protein = (goal_protein-totalProteins)/meal;
        fat_range = Integer.toString(fat-3) +"-"+ Integer.toString(fat+3);
        carbs_range = Integer.toString(carbs-3) +"-"+ Integer.toString(carbs+3);
        protein_range = Integer.toString(protein-3) +"-"+ Integer.toString(protein+3);
        cals_range = Integer.toString(calories-30) +"-"+ Integer.toString(calories+30);
    }


    private void get_Recipes() {
        client = new RecipeClient();

        client.getRecipes(fat_range, carbs_range, protein_range, cals_range, diet, new JsonHttpResponseHandler() {
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