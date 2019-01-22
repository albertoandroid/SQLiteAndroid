package com.androiddesdecero.sql;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.androiddesdecero.sql.data.PesoContract;
import com.androiddesdecero.sql.data.PesoDbHelper;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.SimpleTimeZone;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int PESO_LOADER = 0;
    private String fecha;
    private String peso;
    private EditText introducirPeso;
    private Button button;
    private Button btBorrar;
    //private TextView textView;
    private PesoDbHelper mDbHelper;
    private ListView listView;
    private PesoCursorAdapter adapter;

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
        //textView = findViewById(R.id.textview);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                peso = introducirPeso.getText().toString();
                fecha = obtenerFecha();
                insertarPeso();
                mostrarInfoDatabase();
            }
        });

        btBorrar = findViewById(R.id.btBorrar);
        btBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContentResolver().delete(PesoContract.PesoEntry.CONTENT_URI, null, null);
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
        ContentValues values = new ContentValues();
        values.put(PesoContract.PesoEntry.COLUMN_PESO_FECHA, fecha);
        values.put(PesoContract.PesoEntry.COLUMN_PESO_PESO, peso);
        Uri newUri = getContentResolver().insert(PesoContract.PesoEntry.CONTENT_URI, values);
    }

    private void mostrarInfoDatabase(){
        listView = findViewById(R.id.lvLista);
        adapter = new PesoCursorAdapter(this, null);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
                Uri currentPesoUri = ContentUris.withAppendedId(PesoContract.PesoEntry.CONTENT_URI, id);
                intent.setData(currentPesoUri);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(PESO_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                PesoContract.PesoEntry._ID,
                PesoContract.PesoEntry.COLUMN_PESO_FECHA,
                PesoContract.PesoEntry.COLUMN_PESO_PESO
        };
        //Este Loader se ejecuta el ContentProver en el hilo de Background
        return new CursorLoader(this,
                PesoContract.PesoEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
