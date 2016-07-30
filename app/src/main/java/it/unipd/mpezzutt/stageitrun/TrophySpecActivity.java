/**
 * StageITRun
 * Progetto per insegnamento Reti Wireless
 * @since Anno accademico 2015/2016
 * @author Pezzutti Marco 1084411
 */
package it.unipd.mpezzutt.stageitrun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import it.unipd.mpezzutt.stageitrun.model.Trofeo;

/**
 * Classe che visualizza il dettaglio di un trofeo
 */
public class TrophySpecActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophy_spec);

        Intent intent = getIntent();
        Trofeo item = (Trofeo) intent.getSerializableExtra("trofeo");

        TextView nome = (TextView) findViewById(R.id.trophyNameSpec);
        nome.setText(item.getNome());
        TextView descrizione = (TextView) findViewById(R.id.trophyDescriptionSpec);
        descrizione.setText(item.getDescrLunga());

    }
}
