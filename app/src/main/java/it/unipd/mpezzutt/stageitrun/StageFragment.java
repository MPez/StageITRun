/**
 * StageITRun
 * Progetto per insegnamento Reti Wireless
 * @since Anno accademico 2015/2016
 * @author Pezzutti Marco 1084411
 */
package it.unipd.mpezzutt.stageitrun;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import it.unipd.mpezzutt.stageitrun.model.Stage;

/**
 * Classe che gestisce la lista di stage
 */
public class StageFragment extends ListFragment {

    private UserLogin userLogin;
    private List<Stage> stageList;
    private StageListAdapter stageListAdapter;
    private OnStageFragmentInteraction mListener;

    /**
     * Costruttore
     */
    public StageFragment() {
        stageList = new ArrayList<>();
        userLogin = UserLogin.getInstance();
    }

    /**
     * Metodo factory che crea e ritorna una nuova istanza della classe
     * @return nuova istanza della classe
     */
    public static StageFragment newInstance() {
        StageFragment fragment = new StageFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stageListAdapter = new StageListAdapter(getActivity(), stageList);
        this.setListAdapter(stageListAdapter);
    }

    /**
     * Aggiorna la lista degli stage effettuando una richiesta al server
     * @param order tipo di ordinamento richiesto
     */
    public void updateStageList(String order) {
        RequestQueueSingleton queue = RequestQueueSingleton.getInstance(getActivity()
                .getApplicationContext());
        String url = queue.getURL() + "/stage/";

        if (userLogin.getUtente() != null) {
            url += userLogin.getUtente().getEmail() + "/" + order;
        }

        // effettua la richiesta al server per ricevere la lista degli stage ordinati
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            stageListAdapter.clear();
                            for (int i = 0; i < response.length(); i++) {
                                stageListAdapter.add(Stage.toStage(response.getJSONObject(i)));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        stageListAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "update stage list" + error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                });
        queue.addToRequestQueue(jsonArrayRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stage_list, container, false);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStageFragmentInteraction) {
            mListener = (OnStageFragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStageFragmentInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Stage item = (Stage) l.getItemAtPosition(position);
        mListener.onStageItemSelected(item);
    }


    /**
     * Interfaccia che consente l'interazione tra frgment e activity che lo contiene,
     * deve essere implementata dalle activity.
     */
    public interface OnStageFragmentInteraction {
        void onStageItemSelected(Stage item);
    }

    /**
     * Classe adapter che gestisce la visualizzazione della lista di stage
     */
    class StageListAdapter extends ArrayAdapter<Stage> {
        private final Context context;

        public StageListAdapter (Context context, List<Stage> stageList) {
            super(context, -1, stageList);
            this.context = context;
            StageFragment.this.stageList = stageList;
        }

        public void clear() {
            StageFragment.this.stageList.clear();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.stage_list_item, parent, false);

            if (stageList != null) {
                Stage stage = stageList.get(position);
                TextView aziendaView = (TextView) itemView.findViewById(R.id.azienda);
                aziendaView.setText(stage.getAzienda());
                TextView stageView = (TextView) itemView.findViewById(R.id.stage);
                stageView.setText(stage.getNome());
                TextView codaView = (TextView) itemView.findViewById(R.id.numeroCoda);
                codaView.setText(Integer.toString(stage.getCoda()));

                ImageView image = (ImageView) itemView.findViewById(R.id.stageTick);
                if (userLogin.getUtente() != null) {
                    if (userLogin.getUtente().getStages_end().containsKey(stage.getId())) {
                        image.setImageResource(R.drawable.ic_done_green_24dp);
                    }
                } else {
                    image.setImageResource(R.drawable.ic_clear_red_24dp);
                }
            }

            return itemView;
        }

        @Override
        public int getCount() {
            return stageList.size();
        }

        @Override
        public void add(Stage object) {
            stageList.add(object);
        }

        @Override
        public Stage getItem(int position) {
            return stageList.get(position);
        }
    }
}
