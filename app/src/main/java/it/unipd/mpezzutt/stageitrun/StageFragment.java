package it.unipd.mpezzutt.stageitrun;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * A simple {@link ListFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StageFragment extends ListFragment {

    private static final String ARG_PARAM1 = "utente";

    private Utente utente;
    private List<Stage> stageList;

    private OnFragmentInteractionListener mListener;

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

        JSONParser parser = new JSONParser();

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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