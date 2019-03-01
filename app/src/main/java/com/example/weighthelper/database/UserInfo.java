package com.example.weighthelper.database;

public class UserInfo {

    public static final String TABLE_INFO = "info";
    public static final String COLUMN_ID = "userId";
    public static final String COLUMN_LASTNAME = "lastName";
    public static final String COLUMN_FIRSTNAME = "firstName";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_WEIGHT = "WEIGHT";
    public static final String COLUMN_HEIGHT = "height";
    public static final String COLUMN_GOAL = "goal";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_INFO + "(" +
                    COLUMN_ID + " TEXT PRIMARY KEY," +
                    COLUMN_LASTNAME + " TEXT," +
                    COLUMN_FIRSTNAME + " TEXT," +
                    COLUMN_AGE + " INTEGER," +
                    COLUMN_WEIGHT + " REAL," +
                    COLUMN_HEIGHT + " REAL," +
                    COLUMN_GOAL + " TEXT" + ");";

    public static final String SQL_DELETE =
            "DROP TABLE " + TABLE_INFO;

}
