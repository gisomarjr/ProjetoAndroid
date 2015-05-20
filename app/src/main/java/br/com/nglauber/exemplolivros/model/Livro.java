package br.com.nglauber.exemplolivros.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Livro implements Serializable {
   /* public String titulo;
    public String autor;
    public int ano;
    public int paginas;
    public String capa;*/

    @SerializedName("volumeInfo")
    public Volume volumes;
}
