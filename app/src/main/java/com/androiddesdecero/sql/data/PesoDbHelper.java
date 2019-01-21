package com.androiddesdecero.sql.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by albertopalomarrobledo on 21/1/19.
 */

public class PesoDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pesobasedatos.db";
    private static final int DATABASE_VERSION = 1;

    public PesoDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PESO_TABLE = "CREATE TABLE "
                + PesoContract.PesoEntry.TABLE_NAME + " ("
                + PesoContract.PesoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PesoContract.PesoEntry.COLUMN_PESO_FECHA + " TEXT NOT NULL, "
                + PesoContract.PesoEntry.COLUMN_PESO_PESO + " TEXT);";

        db.execSQL(SQL_CREATE_PESO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
