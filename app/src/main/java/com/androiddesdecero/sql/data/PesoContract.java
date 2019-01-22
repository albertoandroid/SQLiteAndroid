package com.androiddesdecero.sql.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by albertopalomarrobledo on 21/1/19.
 */

public class PesoContract {

    //Content Authority
    public static final String CONTENT_AUTHORITY = "com.androiddesdecero.sql";

    //Base Content Authority
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //Nombre de Path
    public static final String PATH_PESO = "pesodb";

    public PesoContract(){

    }

    public static final class PesoEntry implements BaseColumns{
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PESO;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PESO;


        // Content URI para aceder a los datos de peso del provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PESO);

        public final static String TABLE_NAME = "pesodb";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_PESO_FECHA = "fecha";
        public final static String COLUMN_PESO_PESO = "peso";
    }
}
