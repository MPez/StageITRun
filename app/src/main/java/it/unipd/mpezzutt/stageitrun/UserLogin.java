/**
 * StageITRun
 * Progetto per insegnamento Reti Wireless
 * @since Anno accademico 2015/2016
 * @author Pezzutti Marco 1084411
 */
package it.unipd.mpezzutt.stageitrun;

import it.unipd.mpezzutt.stageitrun.model.Utente;

/**
 * Classe singleton che rappresenta l'utente corrente che ha effettuato
 * il login
 */
public class UserLogin {
    private Utente utente;
    private static UserLogin user;

    /**
     * Crea e ritorna l'oggetto userLogin
     * @return oggetto singleton userLogin
     */
    public static UserLogin getInstance() {
        if (user == null) {
            user = new UserLogin();
        }
        return user;
    }

    /**
     * Ritorna l'utente corrente
     * @return
     */
    public Utente getUtente() {
        return utente;
    }

    /**
     * Imposta l'utente corrente
     * @param utente utente corrente
     */
    public void setUtente(Utente utente) {
        this.utente = utente;
    }
}