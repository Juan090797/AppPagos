package com.example.dnisearch.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dnisearch.model.Movimiento;

import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {
    // Información de la base de datos
    private static final String DATABASE_NAME = "mi_base_de_datos.db";
    private static final int DATABASE_VERSION = 1;

    // Definición de la tabla
    public static final String TABLE_NAME = "movimientos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DETALLE = "detalle";
    public static final String COLUMN_TOTAL = "total";
    public static final String COLUMN_TIPO_MOVIMIENTO = "tipo_movimiento";
    public static final String COLUMN_FECHA_CREATE = "fecha_create";
    public static final String COLUMN_FECHA_UPDATE = "fecha_update";
    public static final String COLUMN_TARJETA_ID = "tarjeta_id";

    // Consulta SQL para crear la tabla
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_DETALLE + " TEXT," +
                    COLUMN_TOTAL + " REAL," +
                    COLUMN_TIPO_MOVIMIENTO + " TEXT," +
                    COLUMN_FECHA_CREATE + " TEXT," +
                    COLUMN_FECHA_UPDATE + " TEXT," +
                    COLUMN_TARJETA_ID + " TEXT)";

    // Consulta SQL para eliminar la tabla
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    // Método para insertar un nuevo registro en la tabla
    // Método para insertar un nuevo movimiento en la base de datos
    public long insertMovimiento(Movimiento movimiento) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DETALLE, movimiento.getDetalle());
        values.put(COLUMN_TOTAL, movimiento.getTotal());
        values.put(COLUMN_TIPO_MOVIMIENTO, movimiento.getTipoMovimiento());
        values.put(COLUMN_FECHA_CREATE, new Date().toString());
        values.put(COLUMN_TARJETA_ID, movimiento.getTarjeta_id());

        return db.insert(TABLE_NAME, null, values);
    }

    // Otros métodos como actualizar, eliminar y consultar registros podrían ser implementados aquí
}
