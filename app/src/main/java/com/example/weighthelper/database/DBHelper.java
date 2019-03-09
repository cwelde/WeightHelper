package com.example.weighthelper.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.weighthelper.FoodLog;


/*
 *This is helper class for execution of SQL statements
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_FILE_NAME = "weightHelper.db";
    public static final  int DB_VERSION = 1;
    SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DB_FILE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserInfo.SQL_CREATE); //if there are more table you should call this statement again with the table name. Ex: tableName.SQL_CREATE
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(UserInfo.SQL_DELETE);
        db.execSQL(UserInfo.SQL_CREATE);
    }


    public long insertUser(String username,double bmi,double weight,double cal,double goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserInfo.COLUMN_USERNAME,username);
        values.put(UserInfo.COLUMN_BMI,bmi);
        values.put(UserInfo.COLUMN_GOAL,goal);
        values.put(UserInfo.COLUMN_WEIGHT,weight);
        values.put(UserInfo.COLUMN_CAL,cal);
        long id = 0;
        try {
            id = db.insertOrThrow(UserInfo.TABLE_INFO, null, values);
        } catch (SQLException e) {
            Log.e("Exception",e.getMessage());
            e.printStackTrace();
        }
        return id;
    }

    public long setNutrition(String username,double totalCal,double totalProtein,double totalCarb, double totalFat) { //sets user record with total calories
        SQLiteDatabase db = this.getWritableDatabase();
        long id = 0;
        return id;
    }


}
