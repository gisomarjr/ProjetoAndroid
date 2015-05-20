package br.com.nglauber.exemplolivros.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LivrosDbHelper extends SQLiteOpenHelper {

    public static final String ENDERECO_PROVIDER = "content://br.com.nglauber.livros";

    public static final String NOME_DO_BANCO = "dbLivros";
    public static final int VERSAO_DO_BANCO = 1;

    public static final String TABELA_LIVROS = "favoritos";
    public static final String CAMPO_ID = "_id";
    public static final String CAMPO_TITULO = "titulo";
    public static final String CAMPO_ANO = "ano";
    public static final String CAMPO_AUTOR = "autor";
    public static final String CAMPO_PAGINAS = "paginas";
    public static final String CAMPO_CAPA = "capa";

    public LivrosDbHelper(Context context) {
        super(context, NOME_DO_BANCO, null, VERSAO_DO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE "+ TABELA_LIVROS +" (" +
                        CAMPO_ID      +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                        CAMPO_TITULO  +" TEXT NOT NULL UNIQUE," +
                        CAMPO_ANO     +" INTEGER," +
                        CAMPO_AUTOR   +" TEXT," +
                        CAMPO_PAGINAS +" INTEGER," +
                        CAMPO_CAPA    +" TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
