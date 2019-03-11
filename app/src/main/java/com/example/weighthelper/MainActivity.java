package com.example.weighthelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.weighthelper.database.DataSource;

public class MainActivity extends AppCompatActivity {

    DataSource _dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _dataSource = new DataSource(this);
        _dataSource.open();
        Toast.makeText(this, "Database acquired", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause(){
        super.onPause();
        _dataSource.close();
    }

    @Override
    protected void onResume(){
        super.onResume();
        _dataSource.open();
    }

   public void nextActivity(View v)
   {
       /* Pass User Values to the Next Intent */

       Intent intent = new Intent(MainActivity.this, UserScreen.class);

       //Feet
       EditText edit =  (EditText) findViewById(R.id.feetText);
       String feet = (String) edit.getText().toString();
       double feetFinal = Double.parseDouble(feet);

       //Inches
       EditText edit1 =  (EditText) findViewById(R.id.inchesText1);
       String inches = (String) edit1.getText().toString();
       double inchesFinal = Double.parseDouble(inches);

       //Weight
       EditText edit2 =  (EditText) findViewById(R.id.weightText);
       String weight = (String) edit2.getText().toString();
       double weightFinal = Double.parseDouble(weight);

       double heightInMeters = ((feetFinal * 12) + inchesFinal) * 0.0254;
       double BMI = (weightFinal * 0.453592) / (heightInMeters * heightInMeters);
       double recCalories = weightFinal * 10.5;

       BMI=BMI*100;              // this sets a to 354.555555
       BMI=Math.floor(BMI);      // this sets a to 354
       BMI=BMI/100;



       Bundle bundle = new Bundle();

       bundle.putDouble("bmi", BMI);
       bundle.putDouble("cal",recCalories);
       bundle.putDouble("weight",weightFinal);



       intent.putExtras(bundle);
       startActivity(intent);

   }
}
