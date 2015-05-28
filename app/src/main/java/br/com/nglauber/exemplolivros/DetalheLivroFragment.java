package br.com.nglauber.exemplolivros;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.nglauber.exemplolivros.data.LivrosDbHelper;
import br.com.nglauber.exemplolivros.model.Livro;
import br.com.nglauber.exemplolivros.model.Preco;
import br.com.nglauber.exemplolivros.model.Volume;


public class DetalheLivroFragment extends Fragment {

    private Livro livro;
    private Volume volume;
    private Preco preco;
    private MenuItem menuItemFavorito;
    private MenuItem menuItemVenda;

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
        preco = livro.venda.preco;
//        String valor = preco.valor;
//        String status = livro.venda.status;

        View view = inflater.inflate(R.layout.fragment_detalhe_livro, container, false);

        ImageView imgCapa = (ImageView)view.findViewById(R.id.imgCapa);
        TextView txtTitulo = (TextView)view.findViewById(R.id.txtTitulo);
        TextView txtAno = (TextView)view.findViewById(R.id.txtAno);
        TextView txtAutor = (TextView)view.findViewById(R.id.txtAutor);
        TextView txtPaginas = (TextView)view.findViewById(R.id.txtPaginas);
        TextView txtPreco = (TextView) view.findViewById(R.id.txtPreco);
        Button btnCompra = (Button) view.findViewById(R.id.btnComprar);

        //Picasso.with(getActivity()).load(livro.capa).into(imgCapa);
        txtTitulo.setText(volume.titulo);
        txtAno.setText(String.valueOf(volume.dataPublicacao));
        txtAutor.setText(volume.descricao);
        switch (livro.venda.status){

            case "FOR_SALE":
                txtPreco.setText(" - R$: " + livro.venda.preco.valor);
                btnCompra.setBackgroundColor(Color.parseColor("#00EE00"));
                btnCompra.setTextColor(Color.parseColor("#FFFFFF"));
                btnCompra.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = null;
                        intent = new Intent(Intent.ACTION_VIEW,	Uri.parse(livro.venda.linkVenda));

                        startActivity(intent);

                    }
                })
                ;
                break;
            case "FREE":
                txtPreco.setText(R.string.compra_disponivel_gratis);
                btnCompra.setBackgroundColor(Color.parseColor("#00EE00"));
                btnCompra.setText("Adquirir");
                btnCompra.setTextColor(Color.parseColor("#FFFFFF"));
                btnCompra.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = null;
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(livro.venda.linkVenda));

                        startActivity(intent);

                    }
                })
                ;
                break;
            case "NOT_FOR_SALE":
                txtPreco.setText(R.string.compra_indisponivel);
                btnCompra.setClickable(false);
                btnCompra.setText("Não disponível");
                btnCompra.setTextColor(Color.parseColor("#C3C3C3"));

                break;
            default:
                txtPreco.setText(R.string.compra_nao_informada);
        }
        Picasso.with(getActivity())
                .load(livro.volumes.urlImagens.urlImagem)
                .into(imgCapa);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detalhe_livro, menu);

        //ITEM DO FAVORITO
        menuItemFavorito = menu.findItem(R.id.action_favorito);

        if (isFavorito(this.livro)){
            menuItemFavorito.setIcon(R.drawable.ic_action_add_favorite);
        } else {
            menuItemFavorito.setIcon(R.drawable.ic_action_remove_favorito);
        }

        //ITEM DA VENDA DO LIVRO
        menuItemVenda = menu.findItem(R.id.action_venda);
        String compraDisponivel = getResources().getString(R.string.compra_disponivel);

        switch (livro.venda.status){

            case "FOR_SALE":
                menuItemVenda.setTitle(compraDisponivel +" - R$: " + livro.venda.preco.valor);
                menuItemVenda.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Intent intent = null;
                        intent = new Intent(Intent.ACTION_VIEW,	Uri.parse(livro.venda.linkVenda));

                        startActivity(intent);
                        return false;
                    }
                });
                break;
            case "FREE":
                menuItemVenda.setTitle(R.string.compra_disponivel_gratis);
                menuItemVenda.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Intent intent = null;
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(livro.venda.linkVenda));

                        startActivity(intent);
                        return false;
                    }
                });
                break;
            case "NOT_FOR_SALE":
                menuItemVenda.setTitle(R.string.compra_indisponivel);
                break;
            default:
                menuItemVenda.setTitle(R.string.compra_nao_informada);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_favorito) {

            ContentValues values = new ContentValues();

            values.put(LivrosDbHelper.CAMPO_TITULO, livro.volumes.titulo);
            values.put(LivrosDbHelper.CAMPO_DATA_PUBLICACAO, livro.volumes.dataPublicacao);
            values.put(LivrosDbHelper.CAMPO_CAPA, livro.volumes.urlImagens.urlImagem);
            values.put(LivrosDbHelper.CAMPO_DESCRICAO, livro.volumes.descricao);
            values.put(LivrosDbHelper.CAMPO_URL_LIVRO, livro.volumes.informacaoLink);
            //SALVAR INFORMAÇÕES TAMBÉM DE VENDA DO LIVRO
            values.put(LivrosDbHelper.CAMPO_VENDA_LINK, livro.venda.linkVenda);
            values.put(LivrosDbHelper.CAMPO_VENDA_STATUS, livro.venda.status);
            if(livro.venda.preco != null) {
                values.put(LivrosDbHelper.CAMPO_VALOR_LIVRO, livro.venda.preco.valor);
            }

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

                        new String[]{ livro.volumes.titulo},
                        null);
        boolean existe = cursor.moveToNext();
        cursor.close();
        return existe;

    }


}
