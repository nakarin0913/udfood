package com.udrumobile.foodapplication.databaselocal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


public class SQLiteHelper extends SQLiteOpenHelper
{
    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database=getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String username,String password){


        SQLiteDatabase database=getWritableDatabase();
        String sql = "INSERT INTO DBMEMLOGIN VALUES (NULL,?,?)";

        SQLiteStatement statement= database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1,username);
        statement.bindString(2,password);

        statement.executeInsert();

    }

    public Cursor getData(String sql){
        SQLiteDatabase database=getReadableDatabase();
        return database.rawQuery(sql,null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
