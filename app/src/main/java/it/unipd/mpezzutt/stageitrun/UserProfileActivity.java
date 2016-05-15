package it.unipd.mpezzutt.stageitrun;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class UserProfileActivity extends Activity {

    UserLogin userLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userLogin = UserLogin.getInstance();
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

    public void negativeButton(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void positiveButton(View view) {
        setResult(RESULT_OK);
        finish();
    }

}
