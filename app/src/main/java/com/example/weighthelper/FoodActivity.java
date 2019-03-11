package com.example.weighthelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FoodActivity extends AppCompatActivity {
    private FoodClient client;
    private ListView foods; //allows for user to select particular brand? of food when logging something eg butter
    private ListView lvMeasures; //allows for user to select measurement of food eg 1 cup diced
    private Button searchButton; //food search
    private Button logButton; //to food log screen
    private Button homeButton;
    private Button toSearchButton; //back to search screen
    private Button newButton; //to start a new day of food log
    private Button nutrientButton; //to nutrients screen
    private Button nHome;
    private Button nSearch;
    private EditText text; //user's search term widget
    private TextView fatView;
    private TextView carbView;
    private TextView proteinView;

    private ArrayList<String> names; //for user to select name from results of user search by name
    private ArrayList<String> ndbnos; //use for searching up nutrients
    private ArrayList<String> measures; //for user to select their measurement of their logged food

    private ArrayList<String> cals; //use for selecting calorie amount for user-searchTerm measurement
    private ArrayList<String> proteins; //use for selecting calorie amount for user-searchTerm measurement
    private ArrayList<String> fats; //use for selecting calorie amount for user-searchTerm measurement
    private ArrayList<String> carbs; //use for selecting calorie amount for user-searchTerm measurement

    private String searchTerm; //user's search Term

    //user data to save when switching between activities vvv

    private ArrayList<FoodLog> logEntries = new ArrayList<>(100); //logged foods
    private int totalCalories = 0; //total all of calories consumed
    private float totalProteins = 0;
    private float totalFats = 0;
    private float totalCarbs = 0 ;

    //total log, each entry is a day of logs. to be used to store in database..????
    private ArrayList<ArrayList<FoodLog>> totalLog = new ArrayList<>(100);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) { //restoring data when switching between activities
            if (extras.containsKey("foodBundle")) {
                Bundle fBundle = extras.getBundle("foodBundle");
                totalCalories = Integer.parseInt(fBundle.getString("cals"));
                logEntries = (ArrayList<FoodLog>) fBundle.getSerializable("dayLog");
                totalLog = (ArrayList<ArrayList<FoodLog>>) fBundle.getSerializable("totalLog");
                totalCarbs = Float.parseFloat(fBundle.getString("carbs"));
                totalFats = Float.parseFloat(fBundle.getString("fats"));
                totalProteins = Float.parseFloat(fBundle.getString("proteins"));
            }
        }
        setContentView(R.layout.food_search);
        searchButton = findViewById(R.id.searchButton);
        text = findViewById(R.id.foodSearch);
        searchButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        searchTerm = text.getText().toString();
                        searchFood(searchTerm);
                    }
                }
        );

        logButton = findViewById(R.id.logButton);
        logButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        logScreen();
                    }
                }
        );

        nutrientButton = findViewById(R.id.nutrientsButton);
        nutrientButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        nutrientScreen();
                    }
                }
        );
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putInt("cals",totalCalories);
        savedInstanceState.putSerializable("dayLog",logEntries);
        savedInstanceState.putSerializable("totalLog",totalLog);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        totalCalories = savedInstanceState.getInt("cals");
        logEntries = (ArrayList<FoodLog>) savedInstanceState.getSerializable("dayLog");
        totalLog = (ArrayList<ArrayList<FoodLog>>) savedInstanceState.getSerializable("totalLog");
    }


    public void searchFood(String food) {
        client = new FoodClient();

        client.searchFood(food, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject responseBody) {
                JSONObject l = null; //list of search
                try {
                    l = responseBody.getJSONObject("list");
                    FoodSearch fs = FoodSearch.fromJson(l);
                    names = fs.getNames();
                    ndbnos = fs.getNdbnos();
                    setContentView(R.layout.food_search_results);
                    ArrayAdapter<String> adapt = new ArrayAdapter<String>(FoodActivity.this,R.layout.food_search_listview,names);
                    ListView listView = findViewById(R.id.foodList);
                    listView.setAdapter(adapt);
                    TextView header = findViewById(R.id.header);
                    header.setText("Results for: " + searchTerm);

                    foods = findViewById(R.id.foodList);
                    foods.setClickable(true); //for user to select a food
                    foods.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Object o = foods.getItemAtPosition(position);
                                    FoodLog log = new FoodLog(o.toString());
                                    logEntries.add(log); //add food to log
                                    String ndbno = ndbnos.get(position);
                                    searchNDBno(ndbno);
                                    //searchNDBno("01009"); //hard code for cheddar cheese...
                                }
                            }
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public ArrayList<FoodLog> getLogEntries() { //log entries for one day?
        return logEntries;
    }

    public ArrayList<String> isNull(ArrayList<String> measures) { //sometimes doesn't have all information????
        ArrayList<String> n = new ArrayList<>(measures.size());
        for (int i = 0; i < measures.size(); i++) {
            n.add("0");
        }
        return n;
    }

    public void searchNDBno(String ndbno) {
        client = new FoodClient();
        client.searchNDBno(ndbno, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject responseBody) {
                JSONObject r = null; //food report
                try {
                    r = responseBody.getJSONObject("report");
                    FoodNoSearch fns = FoodNoSearch.fromJson(r);
                    measures = fns.getMeasurements();
                    cals = fns.getCals();
                    if (cals == null) cals = isNull(measures);
                    fats = fns.getFats();
                    if (fats== null) fats = isNull(measures);
                    carbs = fns.getCarbs();
                    if (carbs == null) carbs = isNull(measures);
                    proteins = fns.getProteins();
                    if (proteins == null) proteins = isNull(measures);


                    setContentView(R.layout.food_measurement_results);
                    ArrayAdapter<String> adapt = new ArrayAdapter<String>(FoodActivity.this,R.layout.food_search_listview,measures);
                    ListView listView = findViewById(R.id.measureList);
                    listView.setAdapter(adapt);
                    TextView header = findViewById(R.id.header2);
                    header.setText("Measurements for: " + logEntries.get(logEntries.size()-1).getFood());
                    lvMeasures = findViewById(R.id.measureList);
                    lvMeasures.setClickable(true);
                    lvMeasures.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String cal = cals.get(position); //use index to select calorie amount
                                    String protein = proteins.get(position);
                                    String fat = fats.get(position);
                                    String carb = carbs.get(position);
                                    totalCalories += Integer.parseInt(cal);
                                    totalFats += Float.parseFloat(fat);
                                    totalProteins += Float.parseFloat(protein);
                                    totalCarbs += Float.parseFloat(carb);
                                    logEntries.get(logEntries.size()-1).setCal(cal);
                                    logEntries.get(logEntries.size()-1).setProtein(protein);
                                    logEntries.get(logEntries.size()-1).setCarb(carb);
                                    logEntries.get(logEntries.size()-1).setFat(fat);
                                    searchScreen(); //back to food search
                                }
                            }
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void newDay() { //adds current food log to list of logs, starts a new day (foodlog)
        totalLog.add(getLogEntries()); //add to list of logs
        logEntries.clear();
        totalCalories = 0;
        totalCarbs = 0;
        totalProteins = 0;
        totalFats = 0;
        logScreen(); //resets log screen
    }

    public String getTotalCalories() { //returns total calories consumed as a string in total food log
        return Integer.toString(totalCalories);
    }

    public String getTotalCarbs() { //returns total calories consumed as a string in total food log
        return Float.toString(totalCarbs);
    }

    public String getTotalFats() { //returns total calories consumed as a string in total food log
        return Float.toString(totalFats);
    }

    public String getTotalProteins() { //returns total calories consumed as a string in total food log
        return Float.toString(totalProteins);
    }

    public void logScreen() { //goes to, sets food log display
        setContentView(R.layout.food_log);
        TableLayout table = findViewById(R.id.foodTable);

        for(int i = 0; i < logEntries.size(); i++) {
            TableRow row = new TableRow(this);
            String f = logEntries.get(i).getFood();
            String c = logEntries.get(i).getCal();
            TextView fView = new TextView(this);
            fView.setText(""+f);
            fView.setTextSize(20);
            TextView cView = new TextView(this);
            cView.setText(""+c+" cals");
            cView.setTextSize(20);
            cView.setGravity(Gravity.RIGHT);

            TextView space = new TextView(this); //to separate name and col
            space.setText("   ");
            row.addView(fView);
            row.addView(space);
            row.addView(cView);
            table.addView(row);
        }

        toSearchButton = findViewById(R.id.logSearch);
        toSearchButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        searchScreen();
                    }
                }
        );

        newButton = findViewById(R.id.newDay);
        newButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        newDay();
                    }
                }
        );

        homeButton = findViewById(R.id.logHome);
        homeButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        homeScreen();
                    }
                }
        );


    }

    public void searchScreen() { //goes back to search screen to log another food item
        setContentView(R.layout.food_search);
        searchButton = findViewById(R.id.searchButton);
        text = findViewById(R.id.foodSearch);
        searchButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        searchTerm = text.getText().toString();
                        searchFood(searchTerm);
                    }
                }
        );

        logButton = findViewById(R.id.logButton);
        logButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        logScreen();
                    }
                }
        );

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        homeScreen();
                    }
                }
        );

        nutrientButton = findViewById(R.id.nutrientsButton);
        nutrientButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        nutrientScreen();
                    }
                }
        );
    }

    public void homeScreen() { //goes to home screen, updates total calories, saves information
        Intent i = new Intent(FoodActivity.this,UserScreen.class);
        i.putExtra("cals", getTotalCalories());
        i.putExtra("dayLog",logEntries);
        i.putExtra("totalLog",totalLog);
        i.putExtra("fats", getTotalFats());
        i.putExtra("carbs",getTotalCarbs());
        i.putExtra("proteins",getTotalProteins());
        startActivity(i);
    }

    public void nutrientScreen() {
        setContentView(R.layout.food_nutrient);
        fatView = findViewById(R.id.fats);
        carbView = findViewById(R.id.carbs);
        proteinView = findViewById(R.id.proteins);
        fatView.setText(getTotalFats());
        carbView.setText(getTotalCarbs());
        proteinView.setText(getTotalProteins());
        nHome = findViewById(R.id.nHome);
        nSearch = findViewById(R.id.nSearch);

        nHome.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        homeScreen();
                    }
                }
        );

        nSearch.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        searchScreen();
                    }
                }
        );

    }

}



