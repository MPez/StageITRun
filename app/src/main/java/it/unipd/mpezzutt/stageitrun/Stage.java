package it.unipd.mpezzutt.stageitrun;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by marco on 04/04/16.
 */
public class Stage implements Serializable {
    private String id;
    private String nome;
    private String azienda;
    private String descrizione;

    public Stage() {
        this.id = null;
        this.nome = null;
        this.azienda = null;
        this.descrizione = null;
    }

    public Stage (String id, String nome, String azienda, String descrizione) {
        this.id = id;
        this.nome = nome;
        this.azienda = azienda;
        this.descrizione = descrizione;
    }

    public static Stage toStage(JSONObject object) throws JSONException {
        String id = object.getString("_id");
        String nome = object.getString("nome");
        String azienda = object.getString("azienda");
        String descrizione = object.getString("descrizione");

        return new Stage(id, nome, azienda, descrizione);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setAzienda(String azienda) {
        this.azienda = azienda;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
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


}
