package com.example.weighthelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserScreen extends AppCompatActivity {
    Button foodLogBtn;
    Button activityBtn;
    TextView cals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);
        userScreen();
        Bundle bundle = getIntent().getExtras();
        double bmi = bundle.getDouble("bmi");
        double cal = bundle.getDouble("cal");
        double w = bundle.getDouble("weight");
        String s = "" + cal;
        String s1 = "" + bmi;
        String s2 = "" + w;
        TextView textView = (TextView) findViewById(R.id.calView);
        textView.setText(s);

        TextView textView2 = (TextView) findViewById(R.id.bmiView);
        textView2.setText(s1);

        TextView textView3 = (TextView) findViewById(R.id.weightView);
        textView3.setText(s2);

        String z;





    }

    public void userScreen() {
        final Bundle extras = getIntent().getExtras(); //passing information between activities
        foodLogBtn = findViewById(R.id.button2);
        foodLogBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(UserScreen.this,FoodActivity.class);
                        if (extras != null) { //when changing back to food activity, transfer information
                            intent.putExtra("cals", extras.getString("cals"));
                            intent.putExtra("fats", extras.getString("fats"));
                            intent.putExtra("carbs", extras.getString("carbs"));
                            intent.putExtra("proteins", extras.getString("proteins"));
                            intent.putExtra("dayLog", extras.getSerializable("dayLog"));
                            intent.putExtra("totalLog", extras.getSerializable("totalLog"));
                        }
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

        cals = findViewById(R.id.textView6);
        if (extras != null) { //set cals eaten
            cals.setText(extras.getString("cals"));
        }

    }
}
