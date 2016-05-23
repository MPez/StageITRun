package it.unipd.mpezzutt.stageitrun;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TrophyWinActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophy_win);

        String nomeTrofeo = getIntent().getStringExtra("nome");

        TextView trofeo = (TextView) findViewById(R.id.nomeTrofeo);
        trofeo.setText(nomeTrofeo);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
