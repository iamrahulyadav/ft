package com.mallardduckapps.fashiontalks.fragments;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mallardduckapps.fashiontalks.R;

import com.mallardduckapps.fashiontalks.adapters.GlammerListAdapter;
import com.mallardduckapps.fashiontalks.loaders.GlammerListLoader;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.utils.Constants;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class GlammersFragment extends ListFragment implements LoaderManager.LoaderCallbacks<ArrayList<User>> {

    private static final String POST_ID = "POST_ID";
    private String paramPostId;
    GlammerListLoader loader;

    private BasicFragment.OnFragmentInteractionListener mListener;

    // TODO: Rename and change types of parameters
    public static GlammersFragment newInstance(String param1) {
        GlammersFragment fragment = new GlammersFragment();
        Bundle args = new Bundle();
        args.putString(POST_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GlammersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            paramPostId = getArguments().getString(POST_ID);
        }

        useLoader();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.list_layout, container, false);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (BasicFragment.OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(Uri.EMPTY);
        }
    }

    @Override
    public Loader<ArrayList<User>> onCreateLoader(int id, Bundle args) {
        loader = new GlammerListLoader(getActivity().getApplicationContext(), Constants.GLAMMERS_LOADER_ID, paramPostId);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<User>> loader, ArrayList<User> data) {
        GlammerListAdapter adapter = new GlammerListAdapter(getActivity(),data);
        setListAdapter(adapter);
//        setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
//                android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS));
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<User>> loader) {

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
        public void onFragmentInteraction(String id);
    }

    private void useLoader(){
        if(loader == null ) {
            loader = (GlammerListLoader) getActivity().getLoaderManager()
                    .initLoader(Constants.GLAMMERS_LOADER_ID, null, this);
            loader.forceLoad();
        }
    }

}
