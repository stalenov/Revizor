package com.kotsokrat.revizor;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import org.json.JSONArray;


public class DB {
    public final static String NUM = "num";
    public final static String ITEM = "item";
    public final static String CONF = "conf";
    public static final String OWNER = "owner";
    public static final String DEPARTMENT = "department";
    public static final String SENIOR = "senior";
    public static final String QUANTITY = "quantity";
    public static final String BARCODE = "barcode";
    public static final String LAST_VERIFIED = "last_verified";

    public static final String TABLE_NAME = "invent";
    Context context;

    public void loadItemsFromREST(JSONArray jArray, SQLiteDatabase db) {
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
                cv.put(LAST_VERIFIED, jArray.getJSONObject(i).getString(LAST_VERIFIED));
                db.insert(TABLE_NAME, null, cv);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public DB(Context context) {
        this.context = context;
    }
}
