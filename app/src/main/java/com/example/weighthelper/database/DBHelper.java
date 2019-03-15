package com.example.weighthelper.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    public boolean doesUserExist(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        String select = "SELECT * FROM " + UserInfo.TABLE_INFO + " WHERE " + UserInfo.COLUMN_USERNAME
                + " ='"+username+"'";
        Cursor cursor = db.rawQuery(select,null);
        boolean exist = false;
        if (cursor.moveToFirst()) {
            exist = true;
        }

        cursor.close();
        db.close();
        return exist;
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

    public long getLastInsertID(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        String get = "SELECT " + UserInfo.COLUMN_ID + " FROM " + UserInfo.TABLE_INFO +
                " WHERE "+UserInfo.COLUMN_USERNAME + "='" + username + "'";
        Cursor cursor = db.rawQuery(get,null);
        cursor.moveToLast();
        long id = cursor.getLong(0);
        cursor.close();
        return id;
    }

    public Cursor getLastEntry(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        String get = "SELECT * FROM " + UserInfo.TABLE_INFO +
                " WHERE "+UserInfo.COLUMN_USERNAME + "='" + username + "'";
        Cursor cursor = db.rawQuery(get,null);
        cursor.moveToLast();
        return cursor;
    }

    public double getAvgCalories(String username) {
        double cal = 0.0;
        SQLiteDatabase db = this.getWritableDatabase();
        String get = "SELECT AVG(" + UserInfo.COLUMN_TOTAL_CAL + ") as Total FROM " + UserInfo.TABLE_INFO +
                " WHERE "+UserInfo.COLUMN_USERNAME + "='" + username + "'";
        Cursor cursor = db.rawQuery(get,null);
        if (cursor.moveToFirst()) {
            cal = cursor.getInt(cursor.getColumnIndex("Total"));
        }
        return cal;
    }

    public boolean setNutrition(long id,double totalCal,double totalProtein,double totalCarb, double totalFat) { //sets user record with total calories
        SQLiteDatabase db = this.getWritableDatabase();

        String updateCal = "UPDATE " + UserInfo.TABLE_INFO + " SET " + UserInfo.COLUMN_TOTAL_CAL + " ="
                +totalCal+ " WHERE " + UserInfo.COLUMN_ID + " = " + id + "";

        String updateCarb = "UPDATE " + UserInfo.TABLE_INFO + " SET " + UserInfo.COLUMN_TOTAL_CARB + " ="
                +totalCarb+ " WHERE " + UserInfo.COLUMN_ID + " = " + id + "";

        String updateFat = "UPDATE " + UserInfo.TABLE_INFO + " SET " + UserInfo.COLUMN_TOTAL_FAT + " ="
                +totalFat+ " WHERE " + UserInfo.COLUMN_ID + " = " + id + "";

        String updateProtein = "UPDATE " + UserInfo.TABLE_INFO + " SET " + UserInfo.COLUMN_TOTAL_PROTEIN + " ="
                +totalProtein+ " WHERE " + UserInfo.COLUMN_ID + " = " + id + "";

        db.execSQL(updateCal);
        db.execSQL(updateCarb);
        db.execSQL(updateFat);
        db.execSQL(updateProtein);

        return true;
    }

    public boolean setNetCal(long id, double netCal) { //sets net calories, including calories burned
        SQLiteDatabase db = this.getWritableDatabase();

        String updateCal = "UPDATE " + UserInfo.TABLE_INFO + " SET " + UserInfo.COLUMN_NET_CAL + " ="
                +netCal+ " WHERE " + UserInfo.COLUMN_ID + " = " + id + "";

        db.execSQL(updateCal);

        return true;
    }



}
