package br.com.nglauber.exemplolivros;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.nglauber.exemplolivros.model.Imagens;
import br.com.nglauber.exemplolivros.model.Livro;
import br.com.nglauber.exemplolivros.model.Volume;

public class LivrosAdapter extends ArrayAdapter<Livro>{

    public LivrosAdapter(Context context, List<Livro> livros) {
        super(context, 0, livros);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_livro, null);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.imgCapa);
        TextView txtTitulo = (TextView)convertView.findViewById(R.id.txtTitulo);
        TextView txtAno = (TextView)convertView.findViewById(R.id.txtAno);

        Livro livro = getItem(position);


        txtTitulo.setText(livro.volumes.titulo);
        txtAno.setText(livro.volumes.dataPublicacao);

        //verifico se a imagem é nula
        if(livro.volumes.urlImagens != null) {
            Picasso.with(getContext())
                    .load(livro.volumes.urlImagens.urlImagem)
                    .into(imageView);
        }else {
            //imagem caso o não encontre o livro
            Picasso.with(getContext())
                    .load("http://rlv.zcache.com.br/ponto_de_interrogacao_dos_desenhos_animados_papel_timbrado-ra082215bdfb44a0d9fc49d7ba691a9df_vg63g_8byvr_512.jpg")
                    .into(imageView);
        }

        return convertView;
    }
}
