package com.kotsokrat.revizor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;


public class DBA {
    private Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public final static String ID = "_id";
    public final static String NUM = "num";
    public final static String ITEM = "item";
    public final static String CONF = "conf";
    public final static String OWNER = "owner";
    public final static String DEPARTMENT = "department";
    public final static String SENIOR = "senior";
    public final static String QUANTITY = "quantity";
    public final static String BARCODE = "barcode";
    public final static String LAST_VERIFIED = "last_verified";

    public final static String TABLE_NAME = "invent";

    public final static int revizorCheckPeroid = 60; // day numbers before state changed


    public DBA(Context context) {
        this.context = context;
    }

    /**********************************************************************************************
     BASIC DB OPERATIONS
     **********************************************************************************************/

    public void openDB(){
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }


    public void closeDB(){
        if (dbHelper != null) dbHelper.close();
    }


    /**********************************************************************************************
     SAVE / LOAD ITEMS DATA
     **********************************************************************************************/
    public void saveItemsToDB(JSONArray jArray) {
        try {
            for (int i = 0; i < jArray.length(); i++) {
                ContentValues cv = new ContentValues();

                cv.put(NUM, jArray.getJSONObject(i).getString(NUM));
                cv.put(ITEM, jArray.getJSONObject(i).getString(ITEM));
                cv.put(CONF, jArray.getJSONObject(i).getString(CONF));
                cv.put(OWNER, jArray.getJSONObject(i).getString(OWNER));
                cv.put(DEPARTMENT, jArray.getJSONObject(i).getString(DEPARTMENT));
                cv.put(SENIOR, jArray.getJSONObject(i).getString(SENIOR));
                cv.put(QUANTITY, jArray.getJSONObject(i).getString(QUANTITY));
                cv.put(BARCODE, jArray.getJSONObject(i).getString(BARCODE));

                int state = Integer.parseInt(jArray.getJSONObject(i).getString(LAST_VERIFIED));
                if (state == -1 || state > revizorCheckPeroid ) {
                    // if out of date or never checked
                    cv.put(LAST_VERIFIED, R.drawable.question);
                } else {
                    // if check ok
                    cv.put(LAST_VERIFIED, R.drawable.check);
                }


                //cv.put(LAST_VERIFIED, jArray.getJSONObject(i).getString(LAST_VERIFIED));
                //cv.put(LAST_VERIFIED, "hren tobi");

                db.insert(TABLE_NAME, null, cv);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getInumByTableId(long id){
        Cursor cursor = db.query(TABLE_NAME, null, "_id = ?", new String[] { Long.toString(id) }, null, null, null);
        Log.d("myTag", "_id: " + id);
        Log.d("myTag", "Cursor row count: " + Integer.toString(cursor.getCount()));
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            Log.d("myTag", "inum: " + cursor.getString(cursor.getColumnIndex(NUM)));

            return cursor.getString(cursor.getColumnIndex(NUM));
        }else {
            Log.d("myTag", "!!! ZERO CURSOR ROWS !!!");
            return null;
        }
    }

    public void flushDB(){
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }


    public Cursor getAllItemsFromDB(){
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }




}
