package com.example.weighthelper;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.weighthelper.database.DBHelper;
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
       Intent intent = new Intent(MainActivity.this, UserScreen.class);
       startActivity(intent);

   }
}
