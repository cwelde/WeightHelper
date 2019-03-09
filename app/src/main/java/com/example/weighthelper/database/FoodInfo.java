package com.example.weighthelper.database;

import android.database.sqlite.SQLiteDatabase;

import com.example.weighthelper.FoodLog;

public class FoodInfo {
    public static final String TABLE_INFO = "foodlogs";
    public static final String USER_TABLE = "info"; //references user info table
    public static final String COLUMN_fID = "foodId";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_FOOD_NAME = "food";
    public static final String COLUMN_CAL = "cal";
    public static final String COLUMN_CARB = "carb";
    public static final String COLUMN_FAT = "fat";
    public static final String COLUMN_PROTEIN = "protein";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_INFO + "(" +
                    COLUMN_fID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_FOOD_NAME + " TEXT," +
                    COLUMN_CAL + " TEXT," +
                    COLUMN_CARB + " TEXT," +
                    COLUMN_FAT + " TEXT," +
                    COLUMN_PROTEIN + " TEXT," +
                    COLUMN_USERNAME + " TEXT REFERENCES " + USER_TABLE +
                    ");";

    public static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + TABLE_INFO;

}
