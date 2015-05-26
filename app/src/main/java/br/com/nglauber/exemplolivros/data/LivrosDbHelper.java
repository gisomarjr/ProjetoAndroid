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
    public static final String CAMPO_DATA_PUBLICACAO = "dataPublicacao";
    public static final String CAMPO_CAPA = "capa";
    public static final String CAMPO_DESCRICAO = "descricao";
    public static final String CAMPO_URL_LIVRO = "url";
    public static final String CAMPO_VENDA_STATUS = "statusVenda";
    public static final String CAMPO_VENDA_LINK = "linkVenda";
    public static final String CAMPO_VALOR_LIVRO = "valorLivro";

    public LivrosDbHelper(Context context) {
        super(context, NOME_DO_BANCO, null, VERSAO_DO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE "+ TABELA_LIVROS +" (" +
                        CAMPO_ID      +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                        CAMPO_TITULO  +" TEXT NOT NULL UNIQUE," +
                        CAMPO_DATA_PUBLICACAO     +" TEXT," +
                        CAMPO_DESCRICAO     +" TEXT," +
                        CAMPO_URL_LIVRO     +" TEXT," +
                        CAMPO_VENDA_LINK     +" TEXT," +
                        CAMPO_VENDA_STATUS     +" TEXT," +
                        CAMPO_VALOR_LIVRO     +" TEXT," +
                        CAMPO_CAPA    +" TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
