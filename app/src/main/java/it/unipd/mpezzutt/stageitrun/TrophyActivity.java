package it.unipd.mpezzutt.stageitrun;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TrophyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophy);

        GridView trophyListView = (GridView) findViewById(R.id.trophy_GridView);

        JSONParser parser = new JSONParser();

        try {
            InputStream trophyInput = getResources().openRawResource(getResources().getIdentifier("trofeo", "raw", getPackageName()));
            List trophyList = parser.readJSON(trophyInput);
            TrophyListAdapter trophyAdapter = new TrophyListAdapter(this, trophyList);
            trophyListView.setAdapter(trophyAdapter);



        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
