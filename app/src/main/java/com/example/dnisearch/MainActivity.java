package com.example.dnisearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dnisearch.db.DBHelper;
import com.example.dnisearch.model.ExtraccionData;
import com.example.dnisearch.model.Movimiento;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovimientoAdapter movimientoAdapter;
    private List<Movimiento> movimientos = new ArrayList<>();
    private DBHelper dbHelper;
    private Button scan_barcode_button,btnAdd;
    private EditText editTextRuc1,editTextRazonSocial1,editTextIGV,editTextTotal,editTextDireccion,editTextFecha,editTextNroDoc;
    String tipo = "ruc";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);

        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        movimientoAdapter = new MovimientoAdapter(movimientos);
        recyclerView.setAdapter(movimientoAdapter);

        // Cargar los movimientos almacenados en la base de datos
        loadMovimientos();

        btnAdd = findViewById(R.id.btnAdd);
        scan_barcode_button = findViewById(R.id.scan_barcode_button);
        editTextRuc1 = (EditText) findViewById(R.id.editTextRuc1);
        editTextRazonSocial1 = findViewById(R.id.editTextRazonSocial1);
        editTextIGV = findViewById(R.id.editTextIGV);
        editTextTotal = findViewById(R.id.editTextTotal);
        editTextDireccion = findViewById(R.id.editTextDireccion);
        editTextFecha = findViewById(R.id.editTextFecha);
        editTextNroDoc = findViewById(R.id.editTextNroDoc);


        scan_barcode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExtraccionData extraccionData = new ExtraccionData();
                GmsBarcodeScannerOptions.Builder optionsBuilder = new GmsBarcodeScannerOptions.Builder();
                optionsBuilder.allowManualInput();
                GmsBarcodeScanner gmsBarcodeScanner = GmsBarcodeScanning.getClient(MainActivity.this, optionsBuilder.build());
                gmsBarcodeScanner
                        .startScan()
                        //.addOnSuccessListener(barcode -> barcodeResultView.setText(extraccionData.getSuccessfulMessage(barcode,tipo)))
                        .addOnSuccessListener(barcode -> {
                            String dataArray = extraccionData.getSuccessfulMessage(barcode, tipo);
                            String[] partes = dataArray.split("\\n");
                            String nroDoc= partes[2]+"-"+partes[3];
                            editTextRuc1.setText(partes[0]);
                            editTextRazonSocial1.setText(partes[10]);
                            editTextIGV.setText(partes[4]);
                            editTextTotal.setText(partes[5]);
                            editTextDireccion.setText(partes[11]);
                            editTextFecha.setText(partes[6]);
                            editTextNroDoc.setText(nroDoc);

                        });
                        //.addOnFailureListener(e -> barcodeResultView.setText(extraccionData.getErrorMessage(e)))
                        //.addOnCanceledListener(() -> barcodeResultView.setText("El escáner de código está cancelado."));
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String totalText = editTextTotal.getText().toString();
                String detalle = "Ruc: " + editTextRuc1.getText().toString()
                        + "Razon_social: " + editTextRazonSocial1.getText().toString()
                        + "Direccion: " + editTextDireccion.getText().toString()
                        + "Nº Doc: " + editTextNroDoc.getText().toString()
                        + "Fecha: " + editTextFecha.getText().toString();
                float totalValue = Float.parseFloat(totalText);
                Movimiento movimiento = new Movimiento(detalle, totalValue, "EGRESOS", "123456");
                long newRowId = dbHelper.insertMovimiento(movimiento);
                limpiar();
                // Luego, notifica al adaptador que los datos han cambiado para que se actualicen en la interfaz de usuario
                movimientoAdapter.notifyDataSetChanged();
            }
        });

    }

    public void limpiar(){
        editTextRuc1.setText("");
        editTextRazonSocial1.setText("");
        editTextIGV.setText("");
        editTextTotal.setText("");
        editTextDireccion.setText("");
        editTextFecha.setText("");
        editTextNroDoc.setText("");
    }

    private void loadMovimientos() {
        // Lógica para cargar los movimientos desde la base de datos
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DBHelper.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        movimientos.clear(); // Limpiar la lista actual antes de cargar los nuevos datos

        while (cursor.moveToNext()) {
            String detalle = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_DETALLE));
            float total = cursor.getFloat(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_TOTAL));
            String tipoMovimiento = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_TIPO_MOVIMIENTO));
            String tarjetaId = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_TARJETA_ID));

            Movimiento movimiento = new Movimiento(detalle, total, tipoMovimiento, tarjetaId);
            movimientos.add(movimiento);
        }

        cursor.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close(); // Cerrar la conexión a la base de datos
    }
}