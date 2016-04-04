package it.unipd.mpezzutt.stageitrun;

/**
 * Created by marco on 04/04/16.
 */
public class Stage {
    private int id;
    private String nome;
    private String azienda;
    private String descrizione;

    public Stage() {
        this.id = 0;
        this.nome = null;
        this.azienda = null;
        this.descrizione = null;
    }

    public Stage (String id, String nome, String azienda, String descrizione) {
        this.id = Integer.parseInt(id);
        this.nome = nome;
        this.azienda = azienda;
        this.descrizione = descrizione;
    }

    public void setId(String id) {
        this.id = Integer.parseInt(id);
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

    public int getId() {
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
