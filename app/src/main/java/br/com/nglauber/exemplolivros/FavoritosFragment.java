package br.com.nglauber.exemplolivros;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import br.com.nglauber.exemplolivros.data.LivrosDbHelper;
import br.com.nglauber.exemplolivros.model.Livro;


public class FavoritosFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ID_LOADER = 0;
    ListView listView;
    LivroCursorAdapter adapter;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new LivroCursorAdapter(getActivity(), null, 0);
        getLoaderManager().initLoader(ID_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favoritos, container, false);

        listView = (ListView)v.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = adapter.getCursor();
                cursor.moveToPosition(i);

                Livro livro = new Livro();
                /*livro.titulo = cursor.getString(cursor.getColumnIndex(LivrosDbHelper.CAMPO_TITULO));
                livro.ano = cursor.getInt(cursor.getColumnIndex(LivrosDbHelper.CAMPO_ANO));
                livro.autor = cursor.getString(cursor.getColumnIndex(LivrosDbHelper.CAMPO_AUTOR));
                livro.capa = cursor.getString(cursor.getColumnIndex(LivrosDbHelper.CAMPO_CAPA));
                livro.paginas = cursor.getInt(cursor.getColumnIndex(LivrosDbHelper.CAMPO_PAGINAS));*/

                if (getActivity() instanceof AoClicarNoLivroListener) {
                    ((AoClicarNoLivroListener)getActivity()).onLivroClick(livro);
                }
            }
        });

        return v;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(),
                Uri.parse(LivrosDbHelper.ENDERECO_PROVIDER),
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
        listView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }
}
