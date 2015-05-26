package br.com.nglauber.exemplolivros.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gisomar on 26/05/15.
 */
public class Preco implements Serializable {

    @SerializedName("amount")
    public String valor;
    @SerializedName("currencyCode")
    public String localidadeMoeda;

}
