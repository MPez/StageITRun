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
 * Classe che rappresenta un trofeo
 */
public class Trofeo implements Serializable {
    private String id;
    private String nome;
    private String descrizione;
    private String descrLunga;

    /**
     * Costruttore
     * @param id id trofeo
     * @param nome nome trofeo
     * @param descrizione descrizione breve trofeo
     * @param descrLunga descrizione lunga trofeo
     */
    public Trofeo(String id, String nome, String descrizione, String descrLunga) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.descrLunga = descrLunga;
    }

    /**
     * Converte un oggetto JSON in un trofeo
     * @param object oggetto JSON da convertire
     * @return nuovo trofeo
     * @throws JSONException
     */
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

    public String getDescrLunga() {
        return descrLunga;
    }

}
