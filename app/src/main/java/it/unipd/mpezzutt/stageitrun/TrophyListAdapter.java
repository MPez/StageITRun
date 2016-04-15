package it.unipd.mpezzutt.stageitrun;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by marco on 13/04/16.
 */
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