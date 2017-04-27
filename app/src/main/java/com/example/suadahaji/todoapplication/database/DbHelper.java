package com.example.suadahaji.todoapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.suadahaji.todoapplication.utils.Constants;

import java.util.ArrayList;

/**
 * Created by suadahaji
 */

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String creatTableQuery = "CREATE TABLE IF NOT EXISTS " + Constants.DB_TABLE + " (" +
                Constants.TABLE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Constants.TABLE_COLUMN_NAME + " TEXT NOT NULL );";
        db.execSQL(creatTableQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + Constants.DB_TABLE;
        db.execSQL(dropTableQuery);
        onCreate(db);

    }

    public void insertNewTask(String task) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.TABLE_COLUMN_NAME, task);
        database.insertWithOnConflict(Constants.DB_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        database.close();
    }

    public void deleteTask(String task) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(Constants.DB_TABLE, Constants.TABLE_COLUMN_NAME + " - ?", new String[]{task});
        database.close();
    }

    public ArrayList<String> getTrackList() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(Constants.DB_TABLE, new String[]{Constants.TABLE_COLUMN_NAME}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(Constants.TABLE_COLUMN_NAME);
            taskList.add(cursor.getString(index));
        }
        cursor.close();
        database.close();
        return taskList;
    }
 }
