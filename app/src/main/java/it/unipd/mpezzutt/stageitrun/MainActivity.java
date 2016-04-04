package it.unipd.mpezzutt.stageitrun;

import android.app.LoaderManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView stageList = (ListView) findViewById(R.id.stage_listView);




        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
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

    /**
     * Load a JSON file available in the asset folder, if the file doesn't exist it throw an exception.
     * @param asset name of the JSON file with extension
     * @return string containing the JSON file
     */
    public String loadJSONFromAsset (String asset) {
        String json = null;

        try {
            InputStream input = getAssets().open(asset);
            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return json;
        }

        return json;
    }

    public void setJSONArray (String asset, String objName) {
        try {
            JSONObject jObj = new JSONObject(loadJSONFromAsset(asset));
            JSONArray jArray = jObj.getJSONArray(objName);
            HashMap<String, String> jMap;
            ArrayList<HashMap<String, String>> jList = new ArrayList<HashMap<String, String>> ();

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jArrayObj = jArray.getJSONObject(i);
                Log.d("Stage ->> ", jArrayObj.getString("nome") + jArrayObj.getString("azienda"));
                String nomeStage = jArrayObj.getString("nome");
                String nomeAzienda = jArrayObj.getString("azienda");
                String descrAzienda = jArrayObj.getString("descrizione");

                //Add your values in your `ArrayList` as below:
                jMap = new HashMap<String, String>();
                jMap.put("formule", nomeStage);
                jMap.put("url", nomeAzienda);

                jList.add(jMap);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
