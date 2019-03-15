package com.example.weighthelper.database;

import android.database.sqlite.SQLiteDatabase;


public class UserInfo {
    //each entry in the database is the information for one day

    public static final String TABLE_INFO = "info";
    public static final String COLUMN_ID = "userId";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_BMI = "bmi";
    public static final String COLUMN_CAL = "cal";
    public static final String COLUMN_GOAL = "goal";
    public static final String COLUMN_TOTAL_CAL = "totalCal";
    public static final String COLUMN_TOTAL_PROTEIN = "totalProtein";
    public static final String COLUMN_TOTAL_CARB = "totalCarb";
    public static final String COLUMN_TOTAL_FAT = "totalFat";
    public static final String COLUMN_NET_CAL = "netCal";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_INFO + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_USERNAME + " TEXT," +
                    COLUMN_WEIGHT + " REAL," +
                    COLUMN_BMI + " REAL," +
                    COLUMN_CAL + " REAL," +
                    COLUMN_GOAL + " REAL," +
                    COLUMN_TOTAL_CAL + " REAL," +
                    COLUMN_TOTAL_CARB + " REAL," +
                    COLUMN_TOTAL_FAT + " REAL," +
                    COLUMN_TOTAL_PROTEIN + " REAL," +
                    COLUMN_NET_CAL + " REAL"+
                    ");";

    public static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + TABLE_INFO;

}
