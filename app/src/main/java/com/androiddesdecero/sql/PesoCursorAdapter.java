package com.androiddesdecero.sql;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.androiddesdecero.sql.data.PesoContract;

/**
 * Created by albertopalomarrobledo on 21/1/19.
 */

public class PesoCursorAdapter extends CursorAdapter {

    public PesoCursorAdapter(Context context, Cursor c){
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView pesoTextView = view.findViewById(R.id.tvPeso);
        TextView fechaTextView = view.findViewById(R.id.tvFecha);

        int pesoColumnIndex = cursor.getColumnIndex(PesoContract.PesoEntry.COLUMN_PESO_PESO);
        int fechaColumnIndex = cursor.getColumnIndex(PesoContract.PesoEntry.COLUMN_PESO_FECHA);

        String pesoPeso = cursor.getString(pesoColumnIndex);
        String pesoFecha = cursor.getString(fechaColumnIndex);

        pesoTextView.setText(pesoPeso);
        fechaTextView.setText(pesoFecha);
    }
}
