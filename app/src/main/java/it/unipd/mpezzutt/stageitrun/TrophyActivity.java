package it.unipd.mpezzutt.stageitrun;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TrophyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_trophy_list);

        GridView trophyListView = (GridView) findViewById(R.id.trophy_GridView);

        JSONParser parser = new JSONParser();

        try {
            InputStream trophyInput = getResources().openRawResource(getResources().getIdentifier("trofei", "raw", getPackageName()));
            List trophyList = parser.readJSON(trophyInput);
            TrophyListAdapter trophyAdapter = new TrophyListAdapter(this, trophyList);
            trophyListView.setAdapter(trophyAdapter);



        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public class TrophyListAdapter extends ArrayAdapter<List> {
        private final Context context;
        private final List<Trofeo> trofeoList;

        public TrophyListAdapter (Context context, List trofeoList) {
            super(context, -1, trofeoList);
            this.context = context;
            this.trofeoList = trofeoList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.trophy_list_item, parent, false);

            Trofeo trofeo = trofeoList.get(position);
            ImageView trofeoView = (ImageView) itemView.findViewById(R.id.trophy_image);
            if (trofeo.getStato() == true) {
                trofeoView.setImageResource(R.drawable.trophy_checkmark);
            } else {
                trofeoView.setImageResource(R.drawable.trophy_close);
            }
            TextView nomeView = (TextView) itemView.findViewById(R.id.trophy_name);
            nomeView.setText(trofeo.getNome());

            return itemView;
        }
    }
}
