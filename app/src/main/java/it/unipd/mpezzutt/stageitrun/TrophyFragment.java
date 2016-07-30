/**
 * StageITRun
 * Progetto per insegnamento Reti Wireless
 * @since Anno accademico 2015/2016
 * @author Pezzutti Marco 1084411
 */
package it.unipd.mpezzutt.stageitrun;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
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

import it.unipd.mpezzutt.stageitrun.model.Trofeo;

/**
 * Classe che gestisce la griglia di trofei
 */
public class TrophyFragment extends Fragment implements AdapterView.OnItemClickListener {

    private UserLogin userLogin;
    private List<Trofeo> trofeoList;
    TrophyListAdapter trophyListAdapter;
    private OnTrophyFragmentInteraction mListener;

    /**
     * Costruttore
     */
    public TrophyFragment() {
        trofeoList = new ArrayList<>();
        userLogin = UserLogin.getInstance();
    }

    /**
     * Metodo factory che crea e ritorna una nuova istanza della classe
     *
     * @return nuova istanza della classe
     */
    public static TrophyFragment newInstance() {
        TrophyFragment fragment = new TrophyFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        trophyListAdapter = new TrophyListAdapter(getActivity(), trofeoList);
        updateTrophyList();
    }

    /**
     * Aggiorna la lista di trofei effettuando una richiesta al server
     */
    public void updateTrophyList() {
        RequestQueueSingleton queue = RequestQueueSingleton.getInstance(getActivity()
                .getApplicationContext());

        String trofeoUrl = queue.getURL() + "/trophy";

        // effettua la richiesta la server per ricevere la lista dei trofei
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                trofeoUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            trophyListAdapter.clear();
                            for (int i = 0; i < response.length(); i++) {
                                trophyListAdapter.add(Trofeo.toTrofeo(response.getJSONObject(i)));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        trophyListAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Trophy "+ "update trophy list" + error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                });

        queue.addToRequestQueue(jsonArrayRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trophy_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GridView gridView = (GridView) getActivity().findViewById(R.id.trophy_GridView);
        gridView.setAdapter(trophyListAdapter);
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Trofeo item = (Trofeo) parent.getItemAtPosition(position);
        mListener.onTrophyItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTrophyFragmentInteraction) {
            mListener = (OnTrophyFragmentInteraction) context;
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

    /**
     * Ritorna il nome del trofeo che corrisponde all'id
     * @param id id del trofeo
     * @return il nome del trofeo corrispondente all'id
     */
    public String getNomeTrofeo(String id) {
        for (Trofeo trofeo : trofeoList) {
            if (trofeo.getId().equals(id)) {
                return trofeo.getNome();
            }
        }
        return null;
    }

    /**
     * Interfaccia che consente l'interazione tra fragment e activity che lo contiene,
     * deve essere implementata dalle activity.
     */
    public interface OnTrophyFragmentInteraction {
        void onTrophyItemSelected(Trofeo item);
    }

    /**
     * Classe adapter che gestisce la visualizzazione della griglia di trofei
     */
    class TrophyListAdapter extends ArrayAdapter<Trofeo> {
        private final Context context;

        public TrophyListAdapter (Context context, List<Trofeo> trofeoList) {
            super(context, -1, trofeoList);
            this.context = context;
            TrophyFragment.this.trofeoList = trofeoList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.trophy_list_item, parent, false);

            if (trofeoList != null) {
                Trofeo trofeo = trofeoList.get(position);
                ImageView trofeoView = (ImageView) itemView.findViewById(R.id.trophyImage);
                if (userLogin.getUtente() != null) {
                    if (userLogin.getUtente().getTrofei().contains(trofeo.getId())) {
                        trofeoView.setImageResource(R.drawable.trophy_checkmark);
                    }
                }
                TextView nomeView = (TextView) itemView.findViewById(R.id.trophyName);
                nomeView.setText(trofeo.getNome());
            }

            return itemView;
        }

        @Override
        public int getCount() {
            if (trofeoList != null) {
                return trofeoList.size();
            }
            else {
                return 0;
            }
        }

        @Override
        public void add(Trofeo object) {
            trofeoList.add(object);
        }

        @Override
        public Trofeo getItem(int position) {
            return trofeoList.get(position);
        }
    }
}
