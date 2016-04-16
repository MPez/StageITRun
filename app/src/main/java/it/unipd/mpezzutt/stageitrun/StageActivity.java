package it.unipd.mpezzutt.stageitrun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by marco on 16/04/16.
 */
public class StageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage);

        ListView stageListView = (ListView) findViewById(R.id.stage_listView);

        JSONParser parser = new JSONParser();

        try {
            InputStream stageInput = getResources().openRawResource(getResources().getIdentifier("stage", "raw", getPackageName()));
            List stageList = parser.readJSON(stageInput);
            StageListAdapter stageAdapter = new StageListAdapter(this, stageList);
            stageListView.setAdapter(stageAdapter);

            stageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Stage item = (Stage) parent.getItemAtPosition(position);
                    Intent stageSpecIntent = new Intent(parent.getContext(), StageSpecActivity.class);
                    stageSpecIntent.putExtra("stage", item);
                    startActivity(stageSpecIntent);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
