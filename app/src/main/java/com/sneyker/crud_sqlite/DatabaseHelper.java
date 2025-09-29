package com.sneyker.crud_sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UsuariosDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USUARIOS = "usuarios";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_EDAD = "edad";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_USUARIOS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NOMBRE + " TEXT," +
                    COLUMN_EMAIL + " TEXT," +
                    COLUMN_EDAD + " INTEGER" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        onCreate(db);
    }

    // CREATE - Agregar usuario
    public long agregarUsuario(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, usuario.getNombre());
        values.put(COLUMN_EMAIL, usuario.getEmail());
        values.put(COLUMN_EDAD, usuario.getEdad());

        long id = db.insert(TABLE_USUARIOS, null, values);
        db.close();
        return id;
    }

    // READ - Obtener usuario por ID
    public Usuario obtenerUsuario(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USUARIOS,
                new String[]{COLUMN_ID, COLUMN_NOMBRE, COLUMN_EMAIL, COLUMN_EDAD},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Usuario usuario = new Usuario(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EDAD))
            );
            cursor.close();
            return usuario;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    // READ - Obtener todos los usuarios
    public List<Usuario> obtenerTodosUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USUARIOS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Usuario usuario = new Usuario();
                usuario.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                usuario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)));
                usuario.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
                usuario.setEdad(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EDAD)));
                usuarios.add(usuario);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return usuarios;
    }

    // UPDATE - Actualizar usuario
    public int actualizarUsuario(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, usuario.getNombre());
        values.put(COLUMN_EMAIL, usuario.getEmail());
        values.put(COLUMN_EDAD, usuario.getEdad());

        return db.update(TABLE_USUARIOS, values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(usuario.getId())});
    }

    // DELETE - Eliminar usuario
    public void eliminarUsuario(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USUARIOS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    // Obtener conteo de usuarios
    public int getUsuariosCount() {
        String countQuery = "SELECT * FROM " + TABLE_USUARIOS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}