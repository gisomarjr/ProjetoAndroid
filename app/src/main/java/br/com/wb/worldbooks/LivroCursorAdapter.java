package br.com.wb.worldbooks;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.wb.worldbooks.data.LivrosDbHelper;

public class LivroCursorAdapter extends CursorAdapter {

    public LivroCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item_livro, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtTitulo = (TextView)view.findViewById(R.id.txtTitulo);
        TextView txtAno = (TextView)view.findViewById(R.id.txtAno);
        ImageView imgCapa = (ImageView)view.findViewById(R.id.imgCapa);

        txtTitulo.setText(cursor.getString(cursor.getColumnIndex(LivrosDbHelper.CAMPO_TITULO)));
        txtAno.setText(String.valueOf(cursor.getString(cursor.getColumnIndex(LivrosDbHelper.CAMPO_DATA_PUBLICACAO))));
        Picasso.with(context)
                .load(cursor.getString(cursor.getColumnIndex(LivrosDbHelper.CAMPO_CAPA)))
                .into(imgCapa);
    }
}
