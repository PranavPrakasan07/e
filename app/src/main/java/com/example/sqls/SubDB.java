package com.example.sqls;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SubDB extends SQLiteOpenHelper {
    public SubDB(@Nullable Context context) {
        super(context, "watch_history.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table watch_history (videoTitle text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists watch_history");
        onCreate(db);

    }
}
