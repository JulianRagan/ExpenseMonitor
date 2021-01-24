package com.jrcw.expensemonitor.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseAccess {
    private DBHelper helper;
    private Context context;
    private SQLiteDatabase database;

    public DatabaseAccess(Context c){
        context = c;
    }

    public DatabaseAccess open() throws SQLException {
        helper = new DBHelper(context);
        database = helper.getWritableDatabase();
        return this;
    }

    public void close(){
        helper.close();
    }

    public Cursor fetchAny(String qry){
        Cursor c = database.rawQuery(qry, null);
        return c;
    }

    public void executeQuery(String qry){
        database.execSQL(qry);
    }
}
