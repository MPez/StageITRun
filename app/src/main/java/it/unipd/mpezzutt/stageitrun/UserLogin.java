package it.unipd.mpezzutt.stageitrun;

import android.app.Application;

/**
 * Created by marco on 20/04/16.
 */

public class UserLogin {
    private Utente utente;
    private static UserLogin user;

    public static UserLogin getInstance() {
        if (user == null) {
            user = new UserLogin();
        }
        return user;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }
}