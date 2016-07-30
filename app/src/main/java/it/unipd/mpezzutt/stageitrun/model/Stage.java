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

/**
 * Classe che rappresenta uno stage
 */
public class Stage implements Serializable {
    private String id;
    private String nome;
    private String azienda;
    private String descrizione;
    private int coda;

    /**
     * Costruttore
     * @param id id stage
     * @param nome nome stage
     * @param azienda nome azienda
     * @param descrizione descrizione stage
     * @param coda numero studenti in coda per lo stage
     */
    public Stage (String id, String nome, String azienda, String descrizione, int coda) {
        this.id = id;
        this.nome = nome;
        this.azienda = azienda;
        this.descrizione = descrizione;
        this.coda = coda;
    }

    /**
     * Converte un oggetto JSON in uno stage
     * @param object oggetto JSON da convertire
     * @return nuovo stage
     * @throws JSONException
     */
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
