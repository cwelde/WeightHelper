package com.example.weighthelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        String test;
        if (bundle.containsKey("cals")) {
            test = bundle.getString("cals");
            cals.setText(bundle.getString("cals"));}


        //insert user into db if username hasn't been registered yet
        db = new DBHelper(getApplicationContext());
        if (!db.doesUserExist(username))
            db.insertUser(username,bmi,w,cal,goal);

        userScreen();

    }

    public Bundle fBundleHelper() {
        final Bundle extras = getIntent().getExtras();
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
                        startActivity(intent);
                    }
                }
        );



    }
}
