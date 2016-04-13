package it.unipd.mpezzutt.stageitrun;

import java.io.Serializable;

/**
 * Created by marco on 13/04/16.
 */
public class Trofeo implements Serializable {
    private String nome;
    private String descrizione;
    private boolean stato;

    public Trofeo(String nome, String descrizione) {
        this.nome = nome;
        this.descrizione = descrizione;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public boolean getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = Boolean.parseBoolean(stato);
    }
}
