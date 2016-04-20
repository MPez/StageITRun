package it.unipd.mpezzutt.stageitrun;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrophyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrophyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrophyFragment extends Fragment {

    private static final String ARG_PARAM1 = "utente";

    private Utente utente;
    private List<Trofeo> trofeoList;

    private OnFragmentInteractionListener mListener;

    public TrophyFragment() {
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
        if (getArguments() != null) {
            utente = (Utente) getArguments().getSerializable(ARG_PARAM1);
        }

        JSONParser parser = new JSONParser();

        try {
            InputStream trofeoInput = getResources().openRawResource(getResources().getIdentifier("trofei", "raw", getActivity().getPackageName()));
            trofeoList = parser.readJSON(trofeoInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        TrophyListAdapter trophyListAdapter = new TrophyListAdapter(getActivity(), trofeoList);
        GridView gridView = (GridView) getActivity().findViewById(R.id.trophy_GridView);
        gridView.setAdapter(trophyListAdapter);
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
//        if (context instanceof OnStageFragmentInteraction) {
//            mListener = (OnStageFragmentInteraction) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnStageFragmentInteraction");
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
}
