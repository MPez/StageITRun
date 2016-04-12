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

        TextView title = (TextView) findViewById(R.id.nomeAzienda);
        title.setText(item.getAzienda());
        TextView subtitle = (TextView) findViewById(R.id.nomeStage);
        subtitle.setText(item.getNome());
        TextView body = (TextView) findViewById(R.id.stageSpec);
        body.setText(item.getDescrizione());
    }
}
