package com.kotsokrat.revizor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "revizor";
    private static final String CREATE_ITEMS_TABLE_QUERY = "CREATE TABLE " + DBA.TABLE_NAME + " ("
            + DBA.ID + " INTEGER PRIMARY KEY, "
            + DBA.NUM + " TEXT, "
            + DBA.ITEM + " TEXT, "
            + DBA.CONF + " TEXT, "
            + DBA.OWNER + " TEXT, "
            + DBA.DEPARTMENT + " TEXT, "
            + DBA.SENIOR + " TEXT, "
            + DBA.QUANTITY + " TEXT, "
            + DBA.BARCODE + " TEXT, "
            + DBA.LAST_VERIFIED + " TEXT, "
            + DBA.LAST_VERIFIED_ICON + " TEXT)";

    private static final String CREATE_DEVNAMES_TABLE_QUERY = "CREATE TABLE " + DBA.TABLE_NAME_DEVICES + " ("
            + DBA.ID + " INTEGER PRIMARY KEY, "
            + DBA.NAME + " TEXT)";

    private static final String CREATE_DEVCONFS_TABLE_QUERY = "CREATE TABLE " + DBA.TABLE_NAME_CONFIGS + " ("
            + DBA.ID + " INTEGER PRIMARY KEY, "
            + DBA.NAME + " TEXT)";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ITEMS_TABLE_QUERY);
        db.execSQL(CREATE_DEVNAMES_TABLE_QUERY);
        db.execSQL(CREATE_DEVCONFS_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

