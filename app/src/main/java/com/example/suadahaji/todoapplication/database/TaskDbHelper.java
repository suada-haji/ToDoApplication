package com.example.suadahaji.todoapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by suadahaji.
 */
public class TaskDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME= "noteinfo.db";

    private static final int DATABASE_VERSION = 1;


    public TaskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

    static {
        /**
         * Registering the Note model
         */
        cupboard().register(TaskModel.class);
    }

    // When the system first runs, and the table doesn't exist
    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * This ensures that all tables are created
         */
        cupboard().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /**
         * This will upgrade tables, adding columns and new tables.
         */
        cupboard().withDatabase(db).upgradeTables();
    }
}
