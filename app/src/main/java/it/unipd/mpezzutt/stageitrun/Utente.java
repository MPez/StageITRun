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
    private String password;
    private List<String> stages;
    private List<String> trofei;

    public Utente() {
        this.id = null;
        this.nome = null;
        this.cognome = null;
        this.email = null;
        this.password = null;
        this.stages = new ArrayList<String>();
        this.trofei = new ArrayList<String>();
    }

    public Utente (String id, String nome, String cognome, String email, String password, List<String> stages, List<String> trofei) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.stages = stages;
        this.trofei = trofei;
    }

    public static Utente toUtente(JSONObject object) throws JSONException {
        String id = object.getString("_id");
        String nome = object.getString("nome");
        String cognome = object.getString("cognome");
        String email = object.getString("email");
        String password = object.getString("password");
        List<String> stages = JSONParser.toList(object.optJSONArray("stage_id"));
        List<String> trofei = JSONParser.toList(object.optJSONArray("trofei_id"));

        return new Utente(id, nome, cognome, email, password, stages, trofei);
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getStages() {
        return stages;
    }

    public void setStages(List<String> stages) {
        this.stages = stages;
    }

    public List<String> getTrofei() {
        return trofei;
    }

    public void setTrofei(List<String> trofei) {
        this.trofei = trofei;
    }
}
