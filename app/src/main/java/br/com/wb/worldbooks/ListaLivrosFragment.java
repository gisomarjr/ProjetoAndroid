package br.com.wb.worldbooks;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import br.com.wb.worldbooks.model.Itens;
import br.com.wb.worldbooks.model.Livro;


public class ListaLivrosFragment extends Fragment {

    ListView listView;
    Itens itens;
    DownloadLivrosTask task;
    ProgressDialog progressDialog;
    SearchView searchView;
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
        searchItem.setTitle(R.string.pesquisar_livro);

        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getResources().getString(R.string.pesquisar_livro));

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String pesquisaUsuario) {

                 task = new DownloadLivrosTask();


                     if (!task.getStatus().equals(AsyncTask.Status.RUNNING) && pesquisaUsuario != "") {
                          task.execute(pesquisaUsuario);
                          mSavedName = pesquisaUsuario;
                          searchView.setQuery("",true);
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
                Toast.makeText(getActivity(), R.string.comecar, Toast.LENGTH_LONG).show();
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


            try {

                Request request = new Request.Builder()
                        .url("https://www.googleapis.com/books/v1/volumes?q="+java.net.URLEncoder.encode(pesquisa[0].toString(), "ISO-8859-1")+"&key=AIzaSyCX-nh1USwGnZHsw1n3zbK97mttwqeiKwQ")
                        .build();


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

                String aguarde = getResources().getString(R.string.aguarde);
                String carregando = getResources().getString(R.string.carregando_livros);

                progressDialog = ProgressDialog.show(getActivity(), aguarde, carregando, true);
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

        if(itens == null){

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.conexao)
                    .setMessage(R.string.verificar_conexao)
                    .setCancelable(false)
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }else {

            if (itens.totalItens > 0) {
                mListaLivros.clear();
                for (Livro livro : itens.livros) {
                    livros.add(livro);
                    mListaLivros.add(livro);
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.alerta)
                        .setMessage(R.string.sem_resultados)
                        .setCancelable(false)
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
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
