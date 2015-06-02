package br.com.wb.worldbooks.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gisomar on 19/05/15.
 */
public class Volume implements Serializable {

    @SerializedName("title")
    public String titulo;
    @SerializedName("publishedDate")
    public String dataPublicacao;
    @SerializedName("infoLink")
    public String informacaoLink;
    @SerializedName("description")
    public String descricao;
    @SerializedName("imageLinks")
    public Imagens urlImagens;

}
