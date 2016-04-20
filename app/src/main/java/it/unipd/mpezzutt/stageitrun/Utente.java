package it.unipd.mpezzutt.stageitrun;

import java.io.Serializable;

/**
 * Created by marco on 04/04/16.
 */
public class Utente implements Serializable {
    private int id;
    private String nome;
    private String cognome;
    private String email;
    private String password;

    public Utente() {
        this.id = 0;
        this.nome = null;
        this.cognome = null;
        this.email = null;
        this.password = null;
    }

    public Utente (String id, String nome, String cognome, String email, String password) {
        this.id = Integer.parseInt(id);
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
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
}
