package com.example.suadahaji.todoapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.suadahaji.todoapplication.utils.Constants;

/**
 * Created by suadahaji
 */

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String creatTableQuery = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_NAME + " (" +
                Constants.TABLE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Constants.TABLE_COLUMN_NAME + " TEXT NOT NULL );";
        db.execSQL(creatTableQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + Constants.TABLE_NAME;
        db.execSQL(dropTableQuery);
        onCreate(db);

    }
}
