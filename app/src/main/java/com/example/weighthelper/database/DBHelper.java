package com.example.weighthelper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


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
        onCreate(db);
    }
}
