package it.unipd.mpezzutt.stageitrun;

import android.app.Application;

/**
 * Created by marco on 20/04/16.
 */
public class UserLogin {
    private Utente utente;
    private static UserLogin userLogin;

    private UserLogin(Utente utente) {
        this.utente = utente;
    }

    public static synchronized UserLogin getInstance(Utente utente) {
        if (userLogin == null) {
            userLogin = new UserLogin(utente);
        }
        return userLogin;
    }

    public Utente getUtente() {
        return utente;
    }
}
