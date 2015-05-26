package br.com.nglauber.exemplolivros;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import br.com.nglauber.exemplolivros.model.Livro;
import br.com.nglauber.exemplolivros.view.SlidingTabLayout;


public class MainActivity extends AppCompatActivity implements AoClicarNoLivroListener {

    ViewPager viewPager;
    SlidingTabLayout mSlidingTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(new LivrosPageAdapter(
                getSupportFragmentManager()));

        mSlidingTabLayout = (SlidingTabLayout)findViewById(R.id.tabs);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(viewPager);

        getSupportActionBar().setElevation(1);
    }

    @Override
    public void onLivroClick(Livro livro) {
        if (getResources().getBoolean(R.bool.isPhone)) {
            Intent it = new Intent(this, DetalheLivroActivity.class);
            it.putExtra("livro", livro);
            startActivity(it);

        } else {
            DetalheLivroFragment detalheLivroFragment =
                    DetalheLivroFragment.novaInstancia(livro);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, detalheLivroFragment, "detalhe")
                    .commit();
        }
    }

    private class LivrosPageAdapter extends FragmentPagerAdapter {
        public LivrosPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(position == 0 ? R.string.tab_livros : R.string.tab_favoritos);
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0){
                return new ListaLivrosFragment();
            } else {
                return new FavoritosFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
