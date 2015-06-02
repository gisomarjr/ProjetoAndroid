package br.com.wb.worldbooks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.wb.worldbooks.model.Livro;


public class DetalheLivroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_livro);

        if (savedInstanceState == null) {
            Livro livro = (Livro) getIntent().getSerializableExtra("livro");

            DetalheLivroFragment detalheLivroFragment =
                    DetalheLivroFragment.novaInstancia(livro);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, detalheLivroFragment, "detalhe")
                    .commit();
        }
    }
}
