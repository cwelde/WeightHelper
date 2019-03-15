package com.example.weighthelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.weighthelper.database.DBHelper;

public class UserScreen extends AppCompatActivity {
    Button foodLogBtn;
    Button activityBtn;
    Button recipeBtn;
    TextView cals;
    private String username;
    private double bmi;
    private double cal;
    private double w;
    private double goal;
    private int steps = 0;
    private double total;
    private double burned = 0;
    Bundle extras;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);

        Bundle bundle = getIntent().getExtras();
        bmi = bundle.getDouble("bmi");
        cal = bundle.getDouble("cal");
        w = bundle.getDouble("weight");
        String s = "" + cal;
        String s1 = "" + bmi;
        String s2 = "" + w;
        TextView textView = (TextView) findViewById(R.id.calView);
        textView.setText(s);

        TextView textView2 = (TextView) findViewById(R.id.bmiView);
        textView2.setText(s1);

        TextView textView3 = (TextView) findViewById(R.id.weightView);
        textView3.setText(s2);

        username = bundle.getString("username");
        goal = bundle.getDouble("goal");
        String s3 = "" + goal;

        TextView textView4 = findViewById(R.id.goalView);
        textView4.setText(s3);

        cals = findViewById(R.id.textView6);
        String eaten = "0";
        if (bundle.containsKey("cals")) {
            eaten = bundle.getString("cals");
            cals.setText(bundle.getString("cals"));}


        //insert user into db if username hasn't been registered yet
        db = new DBHelper(getApplicationContext());
        if (!db.doesUserExist(username))
            db.insertUser(username,bmi,w,cal,goal);

        userScreen();

        // If the Users eaten calories is 1.5x greater than the recommended calories
        // We "recommend" that the user walk to work, or search for a nutrition recipe in our
        // Database

        TextView calEaten = findViewById(R.id.textView6);
        String calE = calEaten.getText().toString();
        double ce = Double.parseDouble(calE);
        ce = ce * 1.5;

        if(ce > cal)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setTitle("We've noticed your Caloric intake is relatively high today")
                    .setMessage("We recommend walking or biking to your next location.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Yes button clicked, do something

                        }
                    })
                    .show();
        }

        double avg = db.getAvgCalories(username);
        TextView avgView = findViewById(R.id.avgView);
        avgView.setText(Double.toString(avg));

        if(bundle.containsKey("steps")) {
            steps = bundle.getInt("steps");
        }

        if( steps%20 == 0) {
            burned = steps / 20;
            TextView burnedView = findViewById(R.id.textView7);
            burnedView.setText(Double.toString(burned));
        }

        TextView totalView = findViewById(R.id.textView8);
        total = Double.parseDouble(eaten) - burned;
        totalView.setText(Double.toString(total));

    }

    @Override
    protected void onStop() {
        super.onStop();
        Bundle extras = new Bundle();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

        }
    }

    public Bundle fBundleHelper() {
        Bundle extras = getIntent().getExtras();
        Bundle foodBundle = new Bundle();
        foodBundle.putString("cals", extras.getString("cals"));
        foodBundle.putString("fats", extras.getString("fats"));
        foodBundle.putString("carbs", extras.getString("carbs"));
        foodBundle.putString("proteins", extras.getString("proteins"));
        foodBundle.putSerializable("dayLog", extras.getSerializable("dayLog"));
        foodBundle.putSerializable("totalLog", extras.getSerializable("totalLog"));
        return foodBundle;
    }

    public Bundle uBundleHelper() {
        Bundle userBundle = new Bundle();
        userBundle.putString("username",username);
        userBundle.putDouble("bmi",bmi);
        userBundle.putDouble("cal",cal);
        userBundle.putDouble("weight",w);
        userBundle.putDouble("goal",goal);
        return userBundle;
    }


    public void userScreen() {
        extras = getIntent().getExtras();
        foodLogBtn = findViewById(R.id.button2);
        foodLogBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(UserScreen.this,FoodActivity.class);
                        if (extras != null) { //when changing back to food activity, transfer information
                            if (extras.containsKey("totalLog")) {
                                intent.putExtra("foodBundle", fBundleHelper());
                            }
                        }

                        intent.putExtra("userBundle",uBundleHelper());

                        startActivity(intent);

                    }
                }
        );

        activityBtn = findViewById(R.id.button6);
        activityBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(UserScreen.this,SensorActivity.class);
                        startActivity(intent);
                    }
                }
        );

        recipeBtn = findViewById(R.id.btnRecipes);
        recipeBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(UserScreen.this,Recipes.class);
                        if (extras != null) { //when changing back to food activity, transfer information
                            if (extras.containsKey("totalLog")) {
                                intent.putExtra("foodBundle", fBundleHelper());
                            }
                        }

                        intent.putExtra("userBundle",uBundleHelper());
                        startActivity(intent);
                    }
                }
        );



    }
}
