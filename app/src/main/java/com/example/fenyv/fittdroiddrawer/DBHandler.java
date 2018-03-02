package com.example.fenyv.fittdroiddrawer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fenyv on 2018. 03. 01..
 */

public class DBHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="FittDroid.db";
    public static final String TABLE_NAME="Statistics";
    public static final String COL_DATE="Date";
    public static final String COL_HEIGHT="Height";
    public static final String COL_WEIGHT="Weight";
    public static final String COL_WAIST="Waist";
    public static final String COL_HIP="Hip";
    public static final String COL_NECK="Neck";



    Context c;


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
        c=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLE_NAME+"("+
                COL_DATE+" DATE  PRIMARY KEY,"+
                COL_HEIGHT+" FLOAT,"+
                COL_WEIGHT+" FLOAT,"+
                COL_WAIST+" FLOAT ," +
                COL_HIP+" FLOAT,"+
                COL_NECK+" FLOAT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("CREATE TABLE  "+ TABLE_NAME+"("+
                COL_DATE+" DATE  PRIMARY KEY,"+
                COL_HEIGHT+" FLOAT,"+
                COL_WEIGHT+" FLOAT,"+
                COL_WAIST+" FLOAT ," +
                COL_HIP+" FLOAT,"+
                COL_NECK+" FLOAT )");

    }

    public boolean insertData(float height, float weight,float hip,float waist,float neck){


        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        db.delete(TABLE_NAME,"Date='"+date+"'",null);
        contentValues.put(COL_DATE,date);
        contentValues.put(COL_HEIGHT,height);
        contentValues.put(COL_WEIGHT,weight);
        contentValues.put(COL_WAIST,weight);
        contentValues.put(COL_HIP,hip);
        contentValues.put(COL_NECK,neck);
        long result=db.insert(TABLE_NAME,null,contentValues);
        if(result==-1){
            return  false;
        }

        return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE Date='"+"2018-03-03'",null);
        return  res;
    }
}
