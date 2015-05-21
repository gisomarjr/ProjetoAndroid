package br.com.nglauber.exemplolivros;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.nglauber.exemplolivros.data.LivrosDbHelper;
import br.com.nglauber.exemplolivros.model.Livro;
import br.com.nglauber.exemplolivros.model.Volume;


public class DetalheLivroFragment extends Fragment {

    private Livro livro;
    private Volume volume;
    private MenuItem menuItem;

    public static DetalheLivroFragment novaInstancia(Livro livro){
        DetalheLivroFragment dlf = new DetalheLivroFragment();
        Bundle args = new Bundle();
        args.putSerializable("livro", livro);
        dlf.setArguments(args);
        return dlf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.livro = (Livro)getArguments().getSerializable("livro");

        volume = livro.volumes;

        View view = inflater.inflate(R.layout.fragment_detalhe_livro, container, false);

        ImageView imgCapa = (ImageView)view.findViewById(R.id.imgCapa);
        TextView txtTitulo = (TextView)view.findViewById(R.id.txtTitulo);
        TextView txtAno = (TextView)view.findViewById(R.id.txtAno);
        TextView txtAutor = (TextView)view.findViewById(R.id.txtAutor);
        TextView txtPaginas = (TextView)view.findViewById(R.id.txtPaginas);

        //Picasso.with(getActivity()).load(livro.capa).into(imgCapa);
        txtTitulo.setText(volume.titulo);
        txtAno.setText(String.valueOf(volume.dataPublicacao));
        txtAutor.setText(volume.descricao);
        txtPaginas.setText(String.valueOf(volume.informacaoLink));
        Picasso.with(getActivity())
                .load(livro.volumes.urlImagens.urlImagem)
                .into(imgCapa);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detalhe_livro, menu);

        menuItem = menu.findItem(R.id.action_favorito);
        if (isFavorito(this.livro)){
            menuItem.setIcon(R.drawable.ic_action_add_favorite);
        } else {
            menuItem.setIcon(R.drawable.ic_action_remove_favorito);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_favorito) {

            ContentValues values = new ContentValues();

            values.put(LivrosDbHelper.CAMPO_TITULO, livro.volumes.titulo);
            values.put(LivrosDbHelper.CAMPO_DATA_PUBLICACAO, livro.volumes.dataPublicacao);
            values.put(LivrosDbHelper.CAMPO_CAPA, livro.volumes.urlImagens.urlImagem);

            if (isFavorito(this.livro)) {
                getActivity().getContentResolver().delete(
                        Uri.parse(LivrosDbHelper.ENDERECO_PROVIDER),
                        LivrosDbHelper.CAMPO_TITULO +" = ?",
                        new String[]{ String.valueOf(livro.volumes.titulo) }
                );
            } else {
                getActivity().getContentResolver().insert(
                        Uri.parse(LivrosDbHelper.ENDERECO_PROVIDER), values
                );
            }
            getActivity().invalidateOptionsMenu();
            return true;


        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isFavorito(Livro livro) {
        Cursor cursor = getActivity().getContentResolver().query(
                Uri.parse(LivrosDbHelper.ENDERECO_PROVIDER),
                        new String[]{ LivrosDbHelper.CAMPO_ID },
                        LivrosDbHelper.CAMPO_TITULO +" = ?",
                        new String[]{ livro.volumes.titulo },
                        null);
        boolean existe = cursor.moveToNext();
        cursor.close();
        return existe;

    }
}
