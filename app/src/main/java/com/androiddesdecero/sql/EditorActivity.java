package com.androiddesdecero.sql;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androiddesdecero.sql.data.PesoContract;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_PESO_LOADER = 1;
    private Uri mCurrentPesoUri;
    private TextView textView;
    private EditText editText;
    private Button button;
    private Button btBorrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        inicializarEditor();
    }

    private void inicializarEditor(){
        textView = findViewById(R.id.tvFecha1);
        editText = findViewById(R.id.etPeso1);
        button = findViewById(R.id.guardar);
        btBorrar = findViewById(R.id.borrar);

        final Intent intent = getIntent();
        mCurrentPesoUri = intent.getData();
        getLoaderManager().initLoader(EXISTING_PESO_LOADER, null, this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(PesoContract.PesoEntry.COLUMN_PESO_FECHA, textView.getText().toString());
                values.put(PesoContract.PesoEntry.COLUMN_PESO_PESO, editText.getText().toString());
                getContentResolver().update(mCurrentPesoUri, values, null, null);
                Intent intentMain = new Intent(EditorActivity.this, MainActivity.class);
                startActivity(intentMain);
            }
        });

        btBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContentResolver().delete(mCurrentPesoUri, null, null);
                finish();
            }
        });
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
                mCurrentPesoUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor == null || cursor.getCount()<1){
            return;
        }
        if(cursor.moveToFirst()){
            int pesoColumnIndex = cursor.getColumnIndex(PesoContract.PesoEntry.COLUMN_PESO_PESO);
            int fechaColumnIndex = cursor.getColumnIndex(PesoContract.PesoEntry.COLUMN_PESO_FECHA);

            String pesoAux = cursor.getString(pesoColumnIndex);
            String fechaAux = cursor.getString(fechaColumnIndex);

            textView.setText(fechaAux);
            editText.setText(pesoAux);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        textView.setText("");
        editText.setText("");
    }
}
