package br.com.nglauber.exemplolivros.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Editora {
    @SerializedName("novatec")
    public List<Categoria> categorias;
}
