package com.pgfmusic.popularmoviesapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pedrogonzalezferrandez on 04/01/16.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String createDB =
            "CREATE TABLE Movies (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "title TEXT NOT NULL, " +
                    "original_title TEXT NOT NULL, " +
                    "plot TEXT NOT NULL, " +
                    "poster_path TEXT NOT NULL, " +
                    "release_date TEXT NOT NULL, " +
                    "user_rating DOUBLE NOT NULL, " +
                    "is_favourite INTEGER NOT NULL)";

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createDB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // not needed. Usually destroy de old version and create a new one
    }
}
