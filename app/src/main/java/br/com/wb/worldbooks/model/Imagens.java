package br.com.wb.worldbooks.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gisomar on 19/05/15.
 */
public class Imagens implements Serializable  {

    @SerializedName("thumbnail")
    public String urlImagem;

}
