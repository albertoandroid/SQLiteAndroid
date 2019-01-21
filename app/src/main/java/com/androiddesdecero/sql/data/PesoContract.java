package com.androiddesdecero.sql.data;

import android.provider.BaseColumns;

/**
 * Created by albertopalomarrobledo on 21/1/19.
 */

public class PesoContract {

    public PesoContract(){

    }

    public static final class PesoEntry implements BaseColumns{
        public final static String TABLE_NAME = "pesodb";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_PESO_FECHA = "fecha";
        public final static String COLUMN_PESO_PESO = "peso";
    }
}
