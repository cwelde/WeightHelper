package com.example.weighthelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    private EditText text; //user's search term widget

    private ArrayList<String> names; //for user to select name from results of user search by name
    private ArrayList<String> ndbnos; //use for searching up nutrients
    private ArrayList<String> measures; //for user to select their measurement of their logged food
    private ArrayList<String> cals; //use for selecting calorie amount for user-searchTerm measurement

    private String searchTerm; //user's search Term

    private ArrayList<FoodLog> logEntries = new ArrayList<>(100); //logged foods
    private int totalCalories = 0; //total all of calories consumed

    //total log, each entry is a day of logs. to be used to store in database..????
    private ArrayList<ArrayList<FoodLog>> totalLog = new ArrayList<>(100);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    ListView listView = (ListView) findViewById(R.id.foodList);
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
                                    totalCalories += Integer.parseInt(cal);
                                    logEntries.get(logEntries.size()-1).setCal(cal);
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
        totalLog.add(getLogEntries());
        logEntries.clear();
        logScreen(); //resets log screen
    }

    public String getTotalCalories() { //returns total calories consumed in total food log
        return Integer.toString(totalCalories);
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
            cView.setText(""+c);
            cView.setTextSize(20);
            row.addView(fView);
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
    }

    public void searchScreen() { //goes back to search screen to log another food item
        setContentView(R.layout.food_search);
        searchButton = findViewById(R.id.searchButton);
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
    }

}



