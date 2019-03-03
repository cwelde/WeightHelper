package com.example.weighthelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserScreen extends AppCompatActivity {
    Button foodLogBtn;
    Button activityBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);
        userScreen();
    }

    public void userScreen() {
        foodLogBtn = findViewById(R.id.button2);
        foodLogBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(UserScreen.this,FoodActivity.class);
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
    }
}
