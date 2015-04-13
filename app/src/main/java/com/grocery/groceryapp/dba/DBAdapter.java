package com.grocery.groceryapp.dba;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by shanestluce on 3/7/15.
 */
public class DBAdapter {
    private static DBAdapter dba;
    public static final String DATABASE_NAME = "Groceries";
    public static final int DATABASE_VERSION = 1;
    public static String TAG = "DBAdapter";

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;


    private DBAdapter(Context ctx)
    {
        this.context = ctx;
        //       delete();
        DBHelper = new DatabaseHelper(context);
    }

    public static DBAdapter getSingleton(Context ctx)
    {
        if(dba == null){
            dba = new DBAdapter(ctx);
        }
        return dba;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            //   	db.delete(GroceryItemDBAdapter.DATABASE_TABLE, null, null);
            db.execSQL(GroceryItemDBAdapter.DATABASE_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion
                    + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + GroceryItemDBAdapter.DATABASE_TABLE);
            onCreate(db);
        }
    }

    //---opens the database---
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---delete the database---
    public void delete()
    {
        context.deleteDatabase(DATABASE_NAME);
    }

    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }
}
