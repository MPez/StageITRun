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

import it.unipd.mpezzutt.stageitrun.model.Utente;

/**
 * Classe che gestisce la classifica degli utenti
 */
public class UserRankFragment extends ListFragment {

    private UserLogin userLogin;
    private RequestQueueSingleton queue;
    private List<Utente> userList;
    private UserListAdapter userListAdapter;
    private OnUserRankFragmentInteraction mListener;

    /**
     * Costruttore
     */
    public UserRankFragment() {
        userList = new ArrayList<>();
        userLogin = UserLogin.getInstance();
    }

    /**
     * Metodo factory che crea e ritorna una nuova istanza della classe
     * @return nuova istanza della classe
     */
    public static UserRankFragment newInstance() {
        UserRankFragment fragment = new UserRankFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userListAdapter = new UserListAdapter(getActivity(), userList);
        this.setListAdapter(userListAdapter);
        updateRankList();
//        if (getArguments() != null) {
//        }
    }

    /**
     * Aggiorna la classifica effettuando una richiesta al server
     */
    public void updateRankList() {
        queue = RequestQueueSingleton.getInstance(getActivity().getApplicationContext());
        String url = queue.getURL() + "/user/rank";

        // effettua la richiesta al server per ricevere la classifica aggiornata
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            userListAdapter.clear();
                            for (int i = 0; i < response.length(); i++) {
                                userListAdapter.add(Utente.toLightUtente(response.getJSONObject(i)));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        userListAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "update rank list" + error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                });
        queue.addToRequestQueue(jsonArrayRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserRankFragmentInteraction) {
            mListener = (OnUserRankFragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUserRankFragmentInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Interfaccia che consente l'interazione tra frgment e activity che lo contiene,
     * deve essere implementata dalle activity.
     */
    public interface OnUserRankFragmentInteraction {
        void onUserItemSelected(Utente item);
    }

    /**
     * Classe adapter che gestisce la visualizzazione della classifica
     */
    class UserListAdapter extends ArrayAdapter<Utente> {
        private final Context context;

        /**
         * Costruttore
         * @param context contesto applicazione
         * @param userList lista degli utenti
         */
        public UserListAdapter (Context context, List<Utente> userList) {
            super(context, -1, userList);
            this.context = context;
            UserRankFragment.this.userList = userList;
        }

        /**
         * Cancella gli elementi della lista utenti
         */
        public void clear() {
            UserRankFragment.this.userList.clear();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.user_list_item, parent, false);

            if (userList != null) {
                Utente utente = userList.get(position);
                TextView stageNum = (TextView) itemView.findViewById(R.id.userStage);
                stageNum.setText(Integer.toString(utente.getStages_end().size()));
                String nome = utente.getNome() + " " + utente.getCognome();
                TextView nomeCognome = (TextView) itemView.findViewById(R.id.userName);
                nomeCognome.setText(nome);
                if (userLogin.getUtente() != null) {
                    if (utente.getEmail().equals(userLogin.getUtente().getEmail())) {
                        stageNum.setTextColor(getResources().getColor(R.color.colorAccent));
                        nomeCognome.setTextColor(getResources().getColor(R.color.colorAccent));
                    }
                }
            }

            return itemView;
        }

        @Override
        public int getCount() {
            return userList.size();
        }

        @Override
        public void add(Utente object) {
            userList.add(object);
        }

        @Override
        public Utente getItem(int position) {
            return userList.get(position);
        }
    }
}
