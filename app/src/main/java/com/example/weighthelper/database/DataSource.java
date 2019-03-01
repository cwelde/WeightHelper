package com.example.weighthelper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataSource {

    private Context _context;
    private SQLiteDatabase _database;
    private SQLiteOpenHelper _dbHelper;

    public DataSource(Context context) {
        _context = context;
        _dbHelper = new DBHelper(_context);
        _database = _dbHelper.getWritableDatabase();
    }

    public void open(){
        _database = _dbHelper.getWritableDatabase();
    }

    public void close(){
        _dbHelper.close();
    }
}
