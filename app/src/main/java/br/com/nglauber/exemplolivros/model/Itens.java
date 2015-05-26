package br.com.nglauber.exemplolivros.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gisomar on 19/05/15.
 */
public class Itens implements Serializable {

    @SerializedName("items")
    public List<Livro> livros;

}
