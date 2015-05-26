package br.com.nglauber.exemplolivros;

import android.app.ProgressDialog;
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

import br.com.nglauber.exemplolivros.model.Itens;
import br.com.nglauber.exemplolivros.model.Livro;


public class ListaLivrosFragment extends Fragment {

    ListView listView;
    Itens itens;
    DownloadLivrosTask task;
    ProgressDialog progressDialog;
    static String mSavedName;
    private String searchedName;
    static ArrayList<Livro> mListaLivros = new ArrayList<>();
    public ArrayList<Livro> listaLivros;
    public ListaLivrosFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        if(savedInstanceState != null){
            mSavedName = savedInstanceState.getString(searchedName);
            mListaLivros = (ArrayList<Livro>)savedInstanceState.getSerializable("listalivros");

        }

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


                     if (!task.getStatus().equals(AsyncTask.Status.RUNNING)) {
                          task.execute(pesquisaUsuario);
                          mSavedName = pesquisaUsuario;
                     }


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



        if (itens == null) {
            if (task == null) {
                Toast.makeText(getActivity(), "Para começar, clique na lupa e digite a busca.", Toast.LENGTH_LONG).show();
            }

        }
        if (mListaLivros != null){
            listView.setAdapter(new LivrosAdapter(getActivity(), mListaLivros));

        }
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
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state

        savedInstanceState.putString(searchedName, mSavedName);

        savedInstanceState.putSerializable("listalivros", (ArrayList<Livro>) mListaLivros);
       // savedInstanceState.putInt(STATE_LEVEL, mCurrentLevel);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    private void preencherLista() {
        List<Livro> livros = new ArrayList<>();
        if(itens != null) {
            mListaLivros.clear();
            for (Livro livro : itens.livros) {
                livros.add(livro);
                mListaLivros.add(livro);
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


    @Override
    public void onPause() {

        if(progressDialog != null) {
            progressDialog.dismiss();
        }
        super.onPause();
    }


}
