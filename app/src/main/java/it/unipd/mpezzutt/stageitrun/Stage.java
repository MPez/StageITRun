package it.unipd.mpezzutt.stageitrun;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by marco on 04/04/16.
 */
public class Stage implements Serializable {
    private String id;
    private String nome;
    private String azienda;
    private String descrizione;
    private int coda;

    public Stage() {
        this.id = null;
        this.nome = null;
        this.azienda = null;
        this.descrizione = null;
        this.coda = 0;
    }

    public Stage (String id, String nome, String azienda, String descrizione, int coda) {
        this.id = id;
        this.nome = nome;
        this.azienda = azienda;
        this.descrizione = descrizione;
        this.coda = coda;
    }

    public static Stage toStage(JSONObject object) throws JSONException {
        String id = object.getString("_id");
        String nome = object.getString("nome");
        String azienda = object.getString("azienda");
        String descrizione = object.getString("descrizione");
        int coda = object.getInt("coda");

        return new Stage(id, nome, azienda, descrizione, coda);
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getAzienda() {
        return azienda;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public int getCoda() {
        return coda;
    }
}
