package it.unipd.mpezzutt.stageitrun;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrophyFragment.OnTrophyFragmentInteraction} interface
 * to handle interaction events.
 * Use the {@link TrophyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrophyFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String ARG_PARAM1 = "utente";
    private Utente utente;
    private List<Trofeo> trofeoList;
    TrophyListAdapter trophyListAdapter;
    private OnTrophyFragmentInteraction mListener;

    public TrophyFragment() {
        trofeoList = new ArrayList<>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param utente Utente.
     * @return A new instance of fragment TrophyFragment.
     */
    public static TrophyFragment newInstance(Utente utente) {
        TrophyFragment fragment = new TrophyFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, utente);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RequestQueueSingleton queue = RequestQueueSingleton.getInstance(getActivity().getApplicationContext());

        if (getArguments() != null) {
            utente = (Utente) getArguments().getSerializable(ARG_PARAM1);
        }

        trophyListAdapter = new TrophyListAdapter(getActivity(), trofeoList);


        //JSONParser parser = new JSONParser();

        String trofeoUrl = "/trophy";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, queue.getURL() + trofeoUrl, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                trophyListAdapter.add(Trofeo.toTrofeo(response.getJSONObject(i)));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        trophyListAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

        queue.addToRequestQueue(jsonArrayRequest);

//        try {
//            InputStream trofeoInput = getResources().openRawResource(getResources().getIdentifier("trofei", "raw", getActivity().getPackageName()));
//            trofeoList = parser.readJSON(trofeoInput);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trophy_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
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
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnTrophyFragmentInteraction {
        void onTrophyItemSelected(Trofeo item);
    }

    class TrophyListAdapter extends ArrayAdapter<Trofeo> {
        private final Context context;
        //private final List<Trofeo> trofeoList;

        public TrophyListAdapter (Context context, List<Trofeo> trofeoList) {
            super(context, -1, trofeoList);
            this.context = context;
            //this.trofeoList = trofeoList;
            TrophyFragment.this.trofeoList = trofeoList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.trophy_list_item, parent, false);
            
            Trofeo trofeo = trofeoList.get(position);
            ImageView trofeoView = (ImageView) itemView.findViewById(R.id.trophyImage);
            //if (utente.getTrofei().contains(trofeo)) {
            if (false) {
                trofeoView.setImageResource(R.drawable.trophy_checkmark);
            } else {
                trofeoView.setImageResource(R.drawable.trophy_close);
            }
            TextView nomeView = (TextView) itemView.findViewById(R.id.trophyName);
            nomeView.setText(trofeo.getNome());

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
