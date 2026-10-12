package com.seuapp.bancodados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ProdutoDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "produtos.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "produtos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOME = "nome";
    public static final String COLUMN_PRECO = "preco";

    public ProdutoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOME + " TEXT NOT NULL, " +
                COLUMN_PRECO + " REAL NOT NULL);";
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Para atualizações futuras da estrutura do banco
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean inserirProduto(String nome, double preco) {
        // Validação básica antes de inserir: segunda barreira contra dados inválidos
        if (nome == null || nome.trim().length() < 3 || preco <= 0) {
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(COLUMN_NOME, nome.trim());
        valores.put(COLUMN_PRECO, preco);
        long resultado = db.insert(TABLE_NAME, null, valores);
        db.close();
        return resultado != -1;
    }

    public List<Produto> listarProdutos() {
        List<Produto> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String nome = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME));
                double preco = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRECO));
                lista.add(new Produto(id, nome, preco));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }
}
