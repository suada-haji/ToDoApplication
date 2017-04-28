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

    public DbHelper(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String creatTableQuery = "CREATE TABLE IF NOT EXISTS " + Constants.DB_TABLE + " (" +
                Constants.TABLE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Constants.TABLE_COLUMN_NAME + " TEXT NOT NULL, " +
                Constants.TABLE_COLUMN_SELECTED + " TEXT NOT NULL);";
        db.execSQL(creatTableQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + Constants.DB_TABLE;
        db.execSQL(dropTableQuery);
        onCreate(db);

    }

    public void insertNewTask(TaskModel task) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.TABLE_COLUMN_NAME, task.getTask_name());
        values.put(Constants.TABLE_COLUMN_SELECTED, task.isSelected());
        database.insertWithOnConflict(Constants.DB_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        database.close();
    }

    public void deleteTask(TaskModel task) {

        String dbQuery = "DELETE FROM " + Constants.DB_TABLE + " WHERE " +
                Constants.TABLE_COLUMN_NAME + " = '" + task.getTask_name() + "'";

        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(dbQuery);
        database.close();
    }

    private TaskModel getFromCursor(Cursor cursor) {
        TaskModel model = new TaskModel();

        model.setTask_id((int) cursor.getLong(0));
        model.setTask_name(cursor.getString(1));
        model.setSelected(Boolean.valueOf(cursor.getString(2)));
        return model;
    }

    public ArrayList<TaskModel> getTrackList() {
        SQLiteDatabase database = this.getReadableDatabase();

        String[] columns = {
                Constants.TABLE_COLUMN_ID,
                Constants.TABLE_COLUMN_NAME,
                Constants.TABLE_COLUMN_SELECTED
        };

        String order = Constants.TABLE_COLUMN_ID_ORDER;
        Cursor cursor = database.query(Constants.DB_TABLE, columns, null, null, null, null, order);

        ArrayList<TaskModel> tasks = new ArrayList<>();
        while (cursor.moveToNext()) {
            tasks.add(getFromCursor(cursor));
        }

        cursor.close();
        database.close();
        return tasks;
    }
 }
