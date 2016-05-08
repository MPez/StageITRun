package it.unipd.mpezzutt.stageitrun;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * A simple {@link ListFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnStageFragmentInteraction} interface
 * to handle interaction events.
 * Use the {@link StageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StageFragment extends ListFragment {

    private static final String ARG_PARAM1 = "utente";
    private Utente utente;

    private List<Stage> stageList;

    private OnStageFragmentInteraction mListener;

    private RequestQueueSingleton queue = RequestQueueSingleton.getInstance(getActivity().getApplicationContext());

    public StageFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param utente Utente .
     * @return A new instance of fragment StageFragment.
     */
    public static StageFragment newInstance(Utente utente) {
        StageFragment fragment = new StageFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, utente);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            utente = (Utente) getArguments().getSerializable(ARG_PARAM1);
        }

        final JSONParser parser = new JSONParser();

        String stageUrl = "/stage";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, stageUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        stageList = parser.readJSON(response, "stage");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });


        try {
            InputStream stageInput = getResources().openRawResource(getResources().getIdentifier("stage", "raw",getActivity().getPackageName()));
            stageList = parser.readJSON(stageInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stage_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        StageListAdapter stageListAdapter = new StageListAdapter(getActivity(), stageList);
        getListView().setAdapter(stageListAdapter);
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
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnStageFragmentInteraction {
        void onStageItemSelected(Stage item);
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
