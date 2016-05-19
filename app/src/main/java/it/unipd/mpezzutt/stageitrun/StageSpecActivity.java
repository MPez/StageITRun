package it.unipd.mpezzutt.stageitrun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class StageSpecActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_spec);

        Intent intent = getIntent();
        Stage item = (Stage) intent.getSerializableExtra("stage");

        Utente utente = UserLogin.getInstance().getUtente();

        TextView title = (TextView) findViewById(R.id.nomeAziendaSpec);
        title.setText(item.getAzienda());
        TextView subtitle = (TextView) findViewById(R.id.nomeStageSpec);
        subtitle.setText(item.getNome());
        TextView body = (TextView) findViewById(R.id.stageDescriptionSpec);
        body.setText(item.getDescrizione());
        TextView start = (TextView) findViewById(R.id.startTime);
        String time = utente.getStages_start().get(item.getId());
        if (time != null) {
            start.setText(time);
        }
        TextView end = (TextView) findViewById(R.id.endTime);
        time = utente.getStages_end().get(item.getId());
        if (time != null) {
            end.setText(time);
        }
    }
}
