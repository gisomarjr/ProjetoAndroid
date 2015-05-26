package br.com.nglauber.exemplolivros.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gisomar on 21/05/15.
 */
public class Venda implements Serializable {

    /**
     * Status
     * FOR_SALE
     * FREE
     * NOT_FOR_SALE
     */
    @SerializedName("saleability")
    public String status;
    @SerializedName("buyLink")
    public String linkVenda;
    @SerializedName("listPrice")
    public Preco preco;
}
