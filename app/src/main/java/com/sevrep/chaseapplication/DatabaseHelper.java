package com.sevrep.chaseapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final Context c;
    private static final String DB_NAME = "myDB.db";
    private static final String TABLE_ITEMS = "tblItems";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        c = context;
    }

    public void onCreate(SQLiteDatabase db) {
        try {
            String sqlQuery = "CREATE TABLE " + TABLE_ITEMS + "(itemid integer primary key autoincrement, name text)";
            db.execSQL(sqlQuery);
            Toast.makeText(c, "Items table created successfully.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("DATABASEHELPER ", "Items table creation error.", e);
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    public void createItem(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        db.insert(TABLE_ITEMS, null, cv);
    }

    public boolean checkItemNameDuplicate(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "SELECT * FROM " + TABLE_ITEMS + " WHERE name = '" + name + "' ";
        Cursor c = db.rawQuery(sqlQuery, null);
        if (c.getCount() != 0) {
            return true;
        }
        c.close();
        return false;
    }

    public Cursor readItemData(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_ITEMS + " WHERE name ='" + name + "' ", null);
        c.moveToFirst();
        return c;
    }

    public Cursor readItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_ITEMS, null);
        c.moveToFirst();
        return c;
    }

    public void updateItemData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        db.update(TABLE_ITEMS, cv, " name = '" + name + "' ", null);
    }

    public void deleteItemData(int itemid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, " itemid = '" + itemid + "' ", null);
    }

}

