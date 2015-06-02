package br.com.wb.worldbooks.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gisomar on 21/05/15.
 */
public class Venda implements Serializable {


    @SerializedName("saleability")
    public String status;
    @SerializedName("buyLink")
    public String linkVenda;
    @SerializedName("listPrice")
    public Preco preco;
}
