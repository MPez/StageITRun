package it.unipd.mpezzutt.stageitrun;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by marco on 13/04/16.
 */
public class Trofeo implements Serializable {
    private String id;
    private String nome;
    private String descrizione;
    private String descrLunga;

    public Trofeo(String id, String nome, String descrizione, String descrLunga) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.descrLunga = descrLunga;
    }

    public static Trofeo toTrofeo(JSONObject object) throws JSONException {
        String id = object.getString("_id");
        String nome = object.getString("nome");
        String descrizione = object.getString("descrizione");
        String descLunga = object.getString("descrizione_lunga");

        return new Trofeo(id, nome, descrizione, descLunga);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDescrLunga() {
        return descrLunga;
    }

    public void setDescrLunga(String descrLunga) {
        this.descrLunga = descrLunga;
    }
}
