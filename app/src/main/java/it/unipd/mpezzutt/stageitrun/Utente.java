package it.unipd.mpezzutt.stageitrun;

/**
 * Created by marco on 04/04/16.
 */
public class Utente {
    private int id;
    private String nome;
    private String cognome;

    public Utente() {
        this.id = 0;
        this.nome = null;
        this.cognome = null;
    }

    public Utente (String id, String nome, String cognome) {
        this.id = Integer.parseInt(id);
        this.nome = nome;
        this.cognome = cognome;
    }

    public void setId(String id) {
        this.id = Integer.parseInt(id);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }
}
