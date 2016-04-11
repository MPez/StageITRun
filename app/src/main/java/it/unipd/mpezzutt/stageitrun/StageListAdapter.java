package it.unipd.mpezzutt.stageitrun;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by marco on 06/04/16.
 */
public class StageListAdapter extends ArrayAdapter<List> {
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
