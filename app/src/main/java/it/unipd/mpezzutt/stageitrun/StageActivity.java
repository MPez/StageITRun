package it.unipd.mpezzutt.stageitrun;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by marco on 16/04/16.
 */
public class StageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_stage_list);

//        ListView stageListView = (ListView) findViewById(R.id.android_list);
//
//        JSONParser parser = new JSONParser();
//
//        try {
//            InputStream stageInput = getResources().openRawResource(getResources().getIdentifier("stage", "raw", getPackageName()));
//            List stageList = parser.readJSON(stageInput);
//            StageListAdapter stageAdapter = new StageListAdapter(this, stageList);
//            stageListView.setAdapter(stageAdapter);
//
//            stageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Stage item = (Stage) parent.getItemAtPosition(position);
//                    Intent stageSpecIntent = new Intent(parent.getContext(), StageSpecActivity.class);
//                    stageSpecIntent.putExtra("stage", item);
//                    startActivity(stageSpecIntent);
//                }
//            });
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    class StageListAdapter extends ArrayAdapter<List> {
        private final Context context;
        private final List<Stage> stageList;

        public StageListAdapter (Context context, List stageList) {
            super(context, -1, stageList);
            this.context = context;
            this.stageList = stageList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.stage_list_item, parent, false);

            Stage stage = stageList.get(position);
            TextView aziendaView = (TextView) itemView.findViewById(R.id.azienda);
            aziendaView.setText(stage.getAzienda());
            TextView stageView = (TextView) itemView.findViewById(R.id.stage);
            stageView.setText(stage.getNome());


            return itemView;
        }
    }
}
