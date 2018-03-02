package com.example.fenyv.fittdroiddrawer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fenyv on 2018. 03. 01..
 */

public class DBHandler extends SQLiteOpenHelper {
    public final String DATABASE_NAME="FittDroid.db";
    public final String TABLE_NAME="Statistics";
    public final String COL_DATE="Date";
    public final String COL_HEIGHT="Neck";
    public final String COL_WEIGHT="Weight";
    public final String COL_HIP="Hip";
    public final String COL_NECK="Neck";





    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
