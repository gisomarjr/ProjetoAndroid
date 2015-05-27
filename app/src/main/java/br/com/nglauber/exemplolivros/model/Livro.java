package br.com.nglauber.exemplolivros.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Livro implements Serializable {

    @SerializedName("volumeInfo")
    public Volume volumes;

    @SerializedName("saleInfo")
    public Venda venda;


}
