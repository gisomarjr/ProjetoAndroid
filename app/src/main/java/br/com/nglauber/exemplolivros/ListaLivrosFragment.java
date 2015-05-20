package br.com.nglauber.exemplolivros;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.nglauber.exemplolivros.model.Categoria;
import br.com.nglauber.exemplolivros.model.Editora;
import br.com.nglauber.exemplolivros.model.Itens;
import br.com.nglauber.exemplolivros.model.Livro;
import br.com.nglauber.exemplolivros.model.Volume;


public class ListaLivrosFragment extends Fragment {

    ListView listView;
    Editora editora;
    Itens itens;
    DownloadLivrosTask task;
    ProgressDialog progressDialog;

    public ListaLivrosFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_principal, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String pesquisaUsuario) {

                    task = new DownloadLivrosTask();
                    task.execute(pesquisaUsuario);


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed

                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded

                return true;  // Return true to expand action view
            }
        });

    }


   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_favorito) {


        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);


        listView = (ListView)view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Livro livro = (Livro) listView.getAdapter().getItem(i);

                if (getActivity() instanceof AoClicarNoLivroListener) {
                    ((AoClicarNoLivroListener)getActivity()).onLivroClick(livro);
                }
            }
        });

        preencherLista();
        //Pesquisar no banco ---

       /* if (itens == null){
            if (task == null){
                task = new DownloadLivrosTask();
                task.execute("");
            } else if (task.getStatus() == AsyncTask.Status.FINISHED){
                preencherLista();
            }
        } else {
            preencherLista();
        }*/



        return view;
    }

    class DownloadLivrosTask extends AsyncTask<String, Void, Itens>{

        @Override
        protected Itens doInBackground(String... pesquisa) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://www.googleapis.com/books/v1/volumes?q="+pesquisa[0]+"&key=AIzaSyCX-nh1USwGnZHsw1n3zbK97mttwqeiKwQ")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String json = response.body().string();

                Gson gson = new Gson();
                itens = gson.fromJson(json, Itens.class);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return itens;
        }

        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(getActivity(), "Aguarde...", "Carregando Livros...", true);
            progressDialog.setCancelable(false);
        }

        @Override
        protected void onPostExecute(Itens itens) {
            super.onPostExecute(itens);
            progressDialog.dismiss();
            preencherLista();
        }
    }

    private void preencherLista() {
        List<Livro> livros = new ArrayList<>();
        if(itens != null) {
            for (Livro livro : itens.livros) {
                livros.add(livro);
            }
        }else{
            Toast.makeText(getActivity(),"Não encontramos Resultados",Toast.LENGTH_LONG).show();
        }

        listView.setAdapter(new LivrosAdapter(getActivity(), livros));

        // Se é tablet e existe algum livro na lista, selecione-o
        if (getActivity() instanceof AoClicarNoLivroListener
                && getResources().getBoolean(R.bool.isTablet)
                && livros.size() > 0){
            ((AoClicarNoLivroListener)getActivity()).onLivroClick(livros.get(0));
        }
    }
}
