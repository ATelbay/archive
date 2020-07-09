package com.example.smqpr.tictactoes;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "test_database", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE table test_table (" +
                " id integer primary key autoincrement, " +
                " player varchar(10), " +
                " record integer default 0" +
                ");");

        ContentValues cv = new ContentValues();
        cv.put("player", "Arystan");
        db.insert("test_table",
                null,
                cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}