package com.grocery.groceryapp.dba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.grocery.groceryapp.data.GroceryItem;

import java.util.ArrayList;

/**
 * Created by shanestluce on 3/7/15.
 */
public class GroceryItemDBAdapter {
    private static GroceryItemDBAdapter gda;
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_FROM = "store";
    public static final String KEY_HISTORY = "history";

    public static final String DATABASE_TABLE = "Grocery";

    public static final String DATABASE_TABLE_CREATE =
            "create table " + DATABASE_TABLE + " (" +
                    KEY_ROWID + " integer primary key autoincrement, " +
                    KEY_NAME + " text non null, " +
                    KEY_QUANTITY + " integer, " +
                    KEY_FROM + " text, "+
                    KEY_HISTORY + " integer);";

    private final Context context;
    private DatabaseHelper DBHelper;
    public SQLiteDatabase db;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DBAdapter.DATABASE_NAME, null, DBAdapter.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub

        }

    }

    private GroceryItemDBAdapter(Context ctx){
        context = ctx;
    }

    public static GroceryItemDBAdapter getSingleton(Context ctx){
        if(gda == null){
            gda = new GroceryItemDBAdapter(ctx);
        }
        return gda;
    }

    public GroceryItemDBAdapter open() throws SQLException {
        DBHelper = new DatabaseHelper(context);
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        DBHelper.close();
    }


    //---insert a gItem into the database---
    public long insertGroceryItem(GroceryItem gItem)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, gItem.getName());
        args.put(KEY_QUANTITY, gItem.getQuantity());
        args.put(KEY_FROM, gItem.getFrom());
        args.put(KEY_HISTORY, gItem.isHistory());
        return db.insert(DATABASE_TABLE, null, args);
    }

    //---deletes a particular gItem---
    public boolean deleteGroceryItem(long rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //---retrieves all the gItems---
    public ArrayList<GroceryItem> getAllGroceryItems()
    {
        return convertCursor(db.query(DATABASE_TABLE, new String[] {
                        KEY_ROWID,
                        KEY_NAME,
                        KEY_QUANTITY,
                        KEY_FROM,
                        KEY_HISTORY},
                null,
                null,
                null,
                null,
                null));
    }
    /*
    public ArrayList<GroceryItem> getGroceryItems(String key, String value){
    	Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                		KEY_ROWID, 
                		KEY_NAME,
                		KEY_DESCRIPTION,
                		KEY_START,
                        KEY_END,
                        KEY_PRIORITY,
                        KEY_COMPLETION
                		}, 
                		key + "=" + value, 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }*/

    //---retrieves all current gItem---
    public ArrayList<GroceryItem> getCurrentGroceryItems() throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                                KEY_ROWID,
                                KEY_NAME,
                                KEY_QUANTITY,
                                KEY_FROM,
                                KEY_HISTORY
                        },
                        KEY_HISTORY + "=0",
                        null,
                        null,
                        null,
                        null,
                        null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            return convertCursor(mCursor);
        }
        return null;
    }

    //---retrieves all history gItem---
    public ArrayList<GroceryItem> getHistoryGroceryItems() throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                                KEY_ROWID,
                                KEY_NAME,
                                KEY_QUANTITY,
                                KEY_FROM,
                                KEY_HISTORY
                        },
                        KEY_HISTORY + "=1",
                        null,
                        null,
                        null,
                        null,
                        null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            return convertCursor(mCursor);
        }
        return null;
    }

    //---retrieves a particular gItem---
    public GroceryItem getGroceryItem(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                                KEY_ROWID,
                                KEY_NAME,
                                KEY_QUANTITY,
                                KEY_FROM,
                                KEY_HISTORY
                        },
                        KEY_ROWID + "=" + rowId,
                        null,
                        null,
                        null,
                        null,
                        null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            return convertCursor(mCursor).get(0);
        }
        return null;
    }

    //---updates a gItem---
    public boolean updateGroceryItem(GroceryItem gItem)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, gItem.getName());
        args.put(KEY_FROM, gItem.getFrom());
        args.put(KEY_QUANTITY, gItem.getQuantity());
        args.put(KEY_HISTORY, gItem.isHistory());
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + gItem.getId(), null) > 0;
    }

    public ArrayList<GroceryItem> convertCursor(Cursor cur)
    {
        ArrayList<GroceryItem> GROCERIES = new ArrayList<GroceryItem>();
        if(cur.moveToFirst()){
            do{
                GroceryItem g = new GroceryItem(cur.getString(cur.getColumnIndex(KEY_NAME)), cur.getInt(cur.getColumnIndex(KEY_QUANTITY)));
                g.set_id(cur.getLong(cur.getColumnIndexOrThrow(KEY_ROWID)));
                g.setFrom(cur.getString(cur.getColumnIndexOrThrow(KEY_FROM)));

                GROCERIES.add(g);
            }while(cur.moveToNext());
        }
        return GROCERIES;
    }
}
