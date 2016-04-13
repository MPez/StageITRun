package it.unipd.mpezzutt.stageitrun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
