/**
 * StageITRun
 * Progetto per insegnamento Reti Wireless
 * @since Anno accademico 2015/2016
 * @author Pezzutti Marco 1084411
 */
package it.unipd.mpezzutt.stageitrun;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import it.unipd.mpezzutt.stageitrun.model.Utente;

/**
 * Classe che gestisce il profilo utente
 */
public class UserProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        UserLogin userLogin = UserLogin.getInstance();
        Utente utente = userLogin.getUtente();

        TextView nome = (TextView) findViewById(R.id.dialog_user_name);
        nome.setText(utente.getNome() + " " + utente.getCognome());
        TextView email = (TextView) findViewById(R.id.dialog_user_email);
        email.setText(utente.getEmail());
        TextView numeroStage = (TextView) findViewById(R.id.dialog_stage_number);
        numeroStage.setText(Integer.toString(utente.getStages_end().size()));
        TextView numeroTrofei = (TextView) findViewById(R.id.dialog_trofei_number);
        numeroTrofei.setText(Integer.toString(utente.getTrofei().size()));
    }

    /**
     * Gestisce la pressione del pulsante negativo e imposta il risultato
     * per effettuare il logout
     * @param view vista a cui appartiene il pulsante
     */
    public void negativeButton(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * Gestisce la pressione del pulsante positivo e imposta il risultato
     * che non comporta modifiche
     * @param view
     */
    public void positiveButton(View view) {
        setResult(RESULT_OK);
        finish();
    }
}
