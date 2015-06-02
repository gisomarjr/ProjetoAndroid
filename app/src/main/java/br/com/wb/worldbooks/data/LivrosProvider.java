package br.com.wb.worldbooks.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class LivrosProvider extends ContentProvider {

    LivrosDbHelper helper;

    public LivrosProvider() {
    }

    @Override
    public boolean onCreate() {
        helper = new LivrosDbHelper(getContext());
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = helper.getWritableDatabase();
        long id = db.insert(LivrosDbHelper.TABELA_LIVROS, null, values);
        db.close();

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int linhasExcluidas = db.delete(LivrosDbHelper.TABELA_LIVROS, selection, selectionArgs);
        db.close();

        getContext().getContentResolver().notifyChange(uri, null);
        return linhasExcluidas;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int linhasAlteradas = db.update(LivrosDbHelper.TABELA_LIVROS, values, selection, selectionArgs);
        db.close();

        getContext().getContentResolver().notifyChange(uri, null);
        return linhasAlteradas;
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {


        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(LivrosDbHelper.TABELA_LIVROS,
                projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(
                getContext().getContentResolver(), uri);

        return cursor;
    }
}
