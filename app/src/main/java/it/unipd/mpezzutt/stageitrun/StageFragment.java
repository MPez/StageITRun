package it.unipd.mpezzutt.stageitrun;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
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
import java.util.Collection;
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

    private UserLogin userLogin;
    private List<Stage> stageList;
    private StageListAdapter stageListAdapter;
    private OnStageFragmentInteraction mListener;

    public StageFragment() {
        stageList = new ArrayList<>();
        userLogin = UserLogin.getInstance();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StageFragment.
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

    public void updateStageList(String order) {
        RequestQueueSingleton queue = RequestQueueSingleton.getInstance(getActivity().getApplicationContext());
        String url = queue.getURL() + "/stage/";
        if (userLogin.getUtente() != null) {
            url += userLogin.getUtente().getEmail() + "/" + order;
        }

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
