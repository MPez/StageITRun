package it.unipd.mpezzutt.stageitrun;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marco on 04/04/16.
 */
public class Utente implements Serializable {
    private String id;
    private String nome;
    private String cognome;
    private String email;
    private List<String> stages_start;
    private List<String> stages_end;
    private List<String> trofei;

    public Utente() {
        this.id = null;
        this.nome = null;
        this.cognome = null;
        this.email = null;
        this.stages_start = new ArrayList<String>();
        this.stages_end = new ArrayList<String>();
        this.trofei = new ArrayList<String>();
    }

    public Utente (String id, String nome, String cognome, String email,
                   List<String> stages_start, List<String> stages_end, List<String> trofei) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.stages_start = stages_start;
        this.stages_end = stages_end;
        this.trofei = trofei;
    }

    public static Utente toUtente(JSONObject object) throws JSONException {
        String id = object.getString("_id");
        String nome = object.getString("nome");
        String cognome = object.getString("cognome");
        String email = object.getString("email");
        List<String> stages_start = JSONParser.toList(object.optJSONArray("stage_id_start"));
        List<String> stages_end = JSONParser.toList(object.optJSONArray("stage_id_end"));
        List<String> trofei = JSONParser.toList(object.optJSONArray("trofei_id"));

        return new Utente(id, nome, cognome, email, stages_start, stages_end, trofei);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getStages_start() {
        return stages_start;
    }

    public void setStages_start(List<String> stages_start) {
        this.stages_start = stages_start;
    }

    public List<String> getStages_end() {
        return stages_end;
    }

    public void setStages_end(List<String> stages_end) {
        this.stages_end = stages_end;
    }

    public List<String> getTrofei() {
        return trofei;
    }

    public void setTrofei(List<String> trofei) {
        this.trofei = trofei;
    }
}
