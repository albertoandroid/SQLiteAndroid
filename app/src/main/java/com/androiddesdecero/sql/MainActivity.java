package com.androiddesdecero.sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androiddesdecero.sql.data.PesoContract;
import com.androiddesdecero.sql.data.PesoDbHelper;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.SimpleTimeZone;

public class MainActivity extends AppCompatActivity {

    private String fecha;
    private String peso;
    private EditText introducirPeso;
    private Button button;
    private TextView textView;
    private PesoDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializar();
    }

    private void inicializar(){
        //para acceder a nuestra base de datos, primero instanciamos a nuestra subclse de SQLiteOpenHelper
        mDbHelper = new PesoDbHelper(this);
        introducirPeso = findViewById(R.id.introPeso);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textview);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                peso = introducirPeso.getText().toString();
                fecha = obtenerFecha();
                insertarPeso();
                mostrarInfoDatabase();
            }
        });
    }

    private String obtenerFecha(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String fechaActual = format.format(cal.getTime());
        return fechaActual;
    }

    private void insertarPeso(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PesoContract.PesoEntry.COLUMN_PESO_FECHA, fecha);
        values.put(PesoContract.PesoEntry.COLUMN_PESO_PESO, peso);
        db.insert(PesoContract.PesoEntry.TABLE_NAME, null, values);
    }

    private void mostrarInfoDatabase(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                PesoContract.PesoEntry._ID,
                PesoContract.PesoEntry.COLUMN_PESO_FECHA,
                PesoContract.PesoEntry.COLUMN_PESO_PESO
        };
        Cursor cursor = db.query(PesoContract.PesoEntry.TABLE_NAME, projection, null, null, null, null, null);
        int idColumnIndex = cursor.getColumnIndex(PesoContract.PesoEntry._ID);
        int fechaColumnIndex = cursor.getColumnIndex(PesoContract.PesoEntry.COLUMN_PESO_FECHA);
        int pesoColumnIndex = cursor.getColumnIndex(PesoContract.PesoEntry.COLUMN_PESO_PESO);
        while (cursor.moveToNext()){
            int currentID = cursor.getInt(idColumnIndex);
            String fechaActual = cursor.getString(fechaColumnIndex);
            String pesoActual = cursor.getString(pesoColumnIndex);
            textView.append("\n" + currentID + " " + fechaActual + " " + pesoActual);
        }
        cursor.close();

        /*
        Cursor cursor = db.rawQuery("SELECT * FROM " + PesoContract.PesoEntry.TABLE_NAME, null);
        try{
            textView.setText("Numero de Filas " + cursor.getCount());
        } finally {
            //Siempre hay que cerrar el cursor
            cursor.close();
        }
        */
    }
}
