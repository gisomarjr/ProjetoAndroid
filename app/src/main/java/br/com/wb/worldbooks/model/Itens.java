package br.com.wb.worldbooks.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gisomar on 19/05/15.
 */
public class Itens implements Serializable {

    @SerializedName("items")
    public List<Livro> livros;

    @SerializedName("totalItems")
    public int totalItens;


}
