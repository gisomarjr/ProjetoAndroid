package br.com.wb.worldbooks.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Livro implements Serializable {

    @SerializedName("volumeInfo")
    public Volume volumes;

    @SerializedName("saleInfo")
    public Venda venda;


}
