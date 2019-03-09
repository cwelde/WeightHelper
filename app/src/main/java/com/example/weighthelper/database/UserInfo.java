package com.example.weighthelper.database;

import android.database.sqlite.SQLiteDatabase;

import com.example.weighthelper.FoodLog;

public class UserInfo {

    public static final String TABLE_INFO = "info";
    public static final String COLUMN_ID = "userId";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_BMI = "bmi";
    public static final String COLUMN_CAL = "cal";
    public static final String COLUMN_GOAL = "goal";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_INFO + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_USERNAME + " TEXT," +
                    COLUMN_WEIGHT + " REAL," +
                    COLUMN_BMI + " REAL," +
                    COLUMN_CAL + " REAL," +
                    COLUMN_GOAL + " REAL" +
                    ");";

    public static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + TABLE_INFO;

}
