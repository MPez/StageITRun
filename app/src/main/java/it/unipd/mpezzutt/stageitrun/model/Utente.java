/**
 * StageITRun
 * Progetto per insegnamento Reti Wireless
 * @since Anno accademico 2015/2016
 * @author Pezzutti Marco 1084411
 */
package it.unipd.mpezzutt.stageitrun.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unipd.mpezzutt.stageitrun.JSONParser;

/**
 * Classe che rappresenta un utente
 */
public class Utente implements Serializable {
    private String nome;
    private String cognome;
    private String email;
    private Map<String, String> stages_start;
    private Map<String, String> stages_end;
    private List<String> trofei;

    /**
     * Costruttore
     * @param nome nome utente
     * @param cognome cognome utente
     * @param email email utente
     * @param stages_start mappa tempo inizio stage
     * @param stages_end mappa tempo fine stage
     * @param trofei lista trofei conquistati
     */
    public Utente (String nome, String cognome, String email,
                   Map<String, String> stages_start, Map<String, String> stages_end,
                   List<String> trofei) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.stages_start = stages_start;
        this.stages_end = stages_end;
        this.trofei = trofei;
    }

    /**
     * Costruttore
     * @param nome nome utente
     * @param cognome cognome utente
     * @param email email utente
     * @param stages_end mappa tempo fine stage
     */
    public Utente (String nome, String cognome, String email, Map<String, String> stages_end) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.stages_start = new HashMap<>();
        this.stages_end = stages_end;
        this.trofei = new ArrayList<>();
    }

    /**
     * Converte un oggetto JSON in utente
     * @param object oggetto JSON da convertire
     * @return nuovo utente
     * @throws JSONException
     */
    public static Utente toUtente(JSONObject object) throws JSONException {
        String nome = object.getString("nome");
        String cognome = object.getString("cognome");
        String email = object.getString("email");
        Map<String, String> stages_start = JSONParser.toMap(object.optJSONArray("stage_id_start"));
        Map<String, String> stages_end = JSONParser.toMap(object.optJSONArray("stage_id_end"));
        List<String> trofei = JSONParser.toList(object.optJSONArray("trofei_id"));

        return new Utente(nome, cognome, email, stages_start, stages_end, trofei);
    }

    /**
     * Converte un oggetto JSON in utente
     * @param object oggetto JSON da convertire
     * @return nuovo utente
     * @throws JSONException
     */
    public static Utente toLightUtente(JSONObject object) throws JSONException {
        String nome = object.getString("nome");
        String cognome = object.getString("cognome");
        String email = object.getString("email");
        Map<String, String> stages_end = JSONParser.toMap(object.optJSONArray("stage_id_end"));

        return new Utente(nome, cognome, email, stages_end);
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

    public Map<String, String> getStages_start() {
        return stages_start;
    }

    public Map<String, String> getStages_end() {
        return stages_end;
    }

    public List<String> getTrofei() {
        return trofei;
    }
}
