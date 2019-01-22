package com.androiddesdecero.sql.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.androiddesdecero.sql.data.PesoDbHelper;
import com.androiddesdecero.sql.data.PesoContract.PesoEntry;

/**
 * Created by albertopalomarrobledo on 21/1/19.
 */

public class PesoProvider extends ContentProvider {
    //Uri matcher code para la table de peso
    private static final int PESO = 100;
    //Uri matcher code para un único resultado de la table de pesos
    private static final int PESO_ID = 101;
    //Objeto UriMatcher para comprobar el content Uri
    private static final UriMatcher sUriMatcher =  new UriMatcher(UriMatcher.NO_MATCH);
    //Static Initializer. Se ejecuta la primera vez que algo es llamdos desde la clase
    static{

        sUriMatcher.addURI(PesoContract.CONTENT_AUTHORITY, PesoContract.PATH_PESO, PESO);
        sUriMatcher.addURI(PesoContract.CONTENT_AUTHORITY, PesoContract.PATH_PESO + "/#", PESO_ID);
    }

    /**
     * Inicializa el provider y el objetivo dabase helper
     */
    private PesoDbHelper mDbHelper;
    @Override
    public boolean onCreate() {
        mDbHelper = new PesoDbHelper(getContext());
        return true;
    }

    /**
     * Realiza la solicitud para la URI. Necesita projection, projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        //Base de Datos en Modo Lectura
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        //Cursor con el resultado de la solicitud
        Cursor cursor;
        //match URI
        int match = sUriMatcher.match(uri);
        switch (match){
            case PESO:
                cursor = database.query(PesoEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PESO_ID:
                selection = PesoEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(PesoEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * Insertar nuevos datos en el provider con los ContentValues
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PESO:
                return insertPeso(uri, contentValues);
            default:
                throw new IllegalArgumentException("Datos no validos " + uri);
        }
    }

    private Uri insertPeso (Uri uri, ContentValues values){
        //Base de datos Modo Escritura
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        // Insertamos el nuevo peso con los datos
        long id = database.insert(PesoEntry.TABLE_NAME, null, values);
        //Si id == -1 ha habido error al sinsertar datos
        if (id == -1) {
            Log.e(PesoProvider.class.getSimpleName(), "Fallo al insertar datos " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri,id);
    }

    /**
     * Actualiza los datos con la selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PESO:
                return updatePeso(uri, contentValues, selection, selectionArgs);
            case PESO_ID:
                selection = PesoEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePeso(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
    private int updatePeso(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(PesoEntry.COLUMN_PESO_PESO)) {
            String pesoAux = values.getAsString(PesoEntry.COLUMN_PESO_PESO);
            if (pesoAux == null) {
                throw new IllegalArgumentException("Peso require peso");
            }
        }
        if (values.containsKey(PesoEntry.COLUMN_PESO_FECHA)) {
            String fechaAux = values.getAsString(PesoEntry.COLUMN_PESO_FECHA);
            if (fechaAux == null) {
                throw new IllegalArgumentException("Peso requiere fecha");
            }
        }
        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(PesoEntry.TABLE_NAME, values, selection, selectionArgs);
        if(rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Returns the number of database rows affected by the update statement
        return rowsUpdated;
    }
    /**
     * Borra los datos con la selection and selection arguments
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PESO:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(PesoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PESO_ID:
                // Delete a single row given by the ID in the URI
                selection = PesoEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(PesoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if(rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    /**
     * Devuelve de MIME type of data for teh content URI
     */

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PESO:
                return PesoEntry.CONTENT_LIST_TYPE;
            case PESO_ID:
                return PesoEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

}
