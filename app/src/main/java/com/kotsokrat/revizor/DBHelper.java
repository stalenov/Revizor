package com.kotsokrat.revizor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "";
    private static final String CREATE_DB_QUERY = "CREATE TABLE " + DB.TABLE_NAME + " ("
            + "_ID INTEGER PRIMARY KEY, "
            + DB.NUM + " TEXT "
            + DB.ITEM + " TEXT "
            + DB.CONF + " TEXT "
            + DB.OWNER + " TEXT "
            + DB.DEPARTMENT + " TEXT "
            + DB.SENIOR + " TEXT "
            + DB.QUANTITY + " TEXT "
            + DB.BARCODE + " TEXT "
            + DB.LAST_VERIFIED + " TEXT)";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}