package com.example.weighthelper;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class Recipes extends AppCompatActivity {
    private RecipeClient client;
    private ArrayList<String> recipe_names;
    private int list_size = 10;
    private String cals_range;
    private String carbs_range;
    private String fat_range;
    private String protein_range;
    private String diet = "";
    private int totalCalories;
    private float totalCarbs;
    private float totalFats;
    private float totalProteins;
    private int cal;
    private int nutrients_range = 10;
    private int calorie_range = 100;
    private Bundle fBundle;
    private Bundle uBundle;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extras = getIntent().getExtras();
        setContentView(R.layout.recipe_results);

        if (extras != null) { //restoring data when switching between activities
            if (extras.containsKey("foodBundle")) {
                fBundle = extras.getBundle("foodBundle");
                totalCalories = Integer.parseInt(fBundle.getString("cals"));
                totalCarbs = Float.parseFloat(fBundle.getString("carbs"));
                totalFats = Float.parseFloat(fBundle.getString("fats"));
                totalProteins = Float.parseFloat(fBundle.getString("proteins"));
            }
            if (extras.containsKey("userBundle")) {
                uBundle = extras.getBundle("userBundle");
                double cal_d = uBundle.getDouble("cal");
                System.out.println("goal_d: " + cal_d);
                cal = (int)cal_d;
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
        else if (time >= 11 && time < 16)
            return 2;
        else
            return 1;
    }

    private void compRecipeNutrients(){
        float goal_fat = (cal/2)/9; // 1/2 of daily calories from far, each calorie 1/9 gram of fat
        float goal_carbs = (cal/4)/4; // 1/4 of daily calories from carbs, each calorie 1/4 gram of fat
        float goal_protein = (cal/4)/4; // 1/4 of daily calories from protein, each calorie 1/4 gram of protein
        int meal = findMeal();
        System.out.println("totalCalories: " + totalCalories + ", goal = "+ cal);
        int calories = (cal-totalCalories)/meal;
        float fat = (goal_fat-totalFats)/meal;
        float carbs = (goal_carbs-totalCarbs)/meal;
        float protein = (goal_protein-totalProteins)/meal;
        fat_range = Float.toString(fat-nutrients_range) +"-"+ Float.toString(fat+nutrients_range);
        carbs_range = Float.toString(carbs-nutrients_range) +"-"+ Float.toString(carbs+nutrients_range);
        protein_range = Float.toString(protein-nutrients_range) +"-"+ Float.toString(protein+nutrients_range);
        cals_range = Integer.toString(calories-calorie_range) +"-"+ Integer.toString(calories+calorie_range);
        System.out.println("fat_range: " + fat_range + ", carbs_range = "+ carbs_range+", protein_range " + protein_range+", calories: " +cals_range);
    }


    private void get_Recipes() {
        client = new RecipeClient();

        client.getRecipes(fat_range, carbs_range, protein_range, cals_range, diet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, final JSONObject responseBody) {
                final ListView all_recipes = findViewById(R.id.recipeList);

                try {
                    recipe_names = new ArrayList<String>();
                    JSONArray hits = responseBody.getJSONArray("hits");
                    for (int i = 0; i < list_size; i++) {
                        recipe_names.add(hits.getJSONObject(i).getJSONObject("recipe").getString("label"));
                    }

                    ArrayAdapter names_adapter = new ArrayAdapter<String>(Recipes.this, R.layout.recipes_listview, recipe_names);
                    all_recipes.setAdapter(names_adapter);
                    all_recipes.setClickable(true);
                    all_recipes.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    try {
                                        setContentView(R.layout.recipe_info);
                                        Button backButton = findViewById(R.id.recipeBack);
                                        backButton.setOnClickListener(
                                                new View.OnClickListener() {
                                                    public void onClick(View view) {
                                                        setContentView(R.layout.recipe_results);
                                                    }
                                                }
                                        );
                                        ArrayList<String> ingredient_list = new ArrayList<String>();
                                        JSONObject rec = responseBody.getJSONArray("hits").getJSONObject(position).getJSONObject("recipe");
                                        JSONArray ingredient_arr = rec.getJSONArray("ingredientLines");
                                        for (int i=0;i<ingredient_arr.length();i++){ // Getting ingredients
                                            ingredient_list.add(ingredient_arr.getString(i));
                                        }

                                        ArrayAdapter ing_adapter = new ArrayAdapter<String>(Recipes.this, R.layout.ingredients_listview, ingredient_list);
                                        ListView recipe_ingredients = findViewById(R.id.ingredients);
                                        recipe_ingredients.setAdapter(ing_adapter);

                                        TextView cals = findViewById(R.id.cals);
                                        cals.setText(rec.getString("calories"));

                                        TextView fat = findViewById(R.id.fat);
                                        fat.setText(rec.getJSONObject("totalNutrients").getJSONObject("FAT").getString("quantity"));

                                        TextView carbs = findViewById(R.id.carbs);
                                        carbs.setText(rec.getJSONObject("totalNutrients").getJSONObject("CHOCDF").getString("quantity"));

                                        TextView proteins = findViewById(R.id.proteins);
                                        proteins.setText(rec.getJSONObject("totalNutrients").getJSONObject("PROCNT").getString("quantity"));
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