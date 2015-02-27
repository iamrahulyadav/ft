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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.mallardduckapps.fashiontalks.R;

import com.mallardduckapps.fashiontalks.adapters.CommentListAdapter;
import com.mallardduckapps.fashiontalks.adapters.GlammerListAdapter;
import com.mallardduckapps.fashiontalks.loaders.CommentListLoader;
import com.mallardduckapps.fashiontalks.loaders.GlammerListLoader;
import com.mallardduckapps.fashiontalks.objects.Comment;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;
import com.rockerhieu.emojicon.EmojiconEditText;

import java.util.ArrayList;

public class CommentsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<ArrayList<Comment>> {

    private static final String POST_ID = "POST_ID";
    private String paramPostId;
    CommentListLoader loader;
    Button sendButton;

    private BasicFragment.OnFragmentInteractionListener mListener;

    // TODO: Rename and change types of parameters
    public static CommentsFragment newInstance(String param1) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putString(POST_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CommentsFragment() {
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
                R.layout.comment_layout, container, false);
        final EmojiconEditText editText = (EmojiconEditText) view.findViewById(R.id.textField);
       // editText.setText();//"\ud83d" #0xd83d    \uD83D\uDE04 = 0xd83d0xde04
        editText.setText("U+1F601" + "-" +"\ue32d" + " " + "\ud83d" + "-" + "\udc4d" + "-" + "\uef0f" + " - " + "\u2764");
        sendButton = (Button) view.findViewById(R.id.sendButton);
        sendButton.setTypeface(FTUtils.loadFont(getActivity().getAssets(), getActivity().getString(R.string.font_helvatica_md)));
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
    public Loader<ArrayList<Comment>> onCreateLoader(int id, Bundle args) {
        loader = new CommentListLoader(getActivity().getApplicationContext(), Constants.COMMENTS_LOADER_ID, paramPostId);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Comment>> loader, ArrayList<Comment> data) {
        CommentListAdapter adapter = new CommentListAdapter(getActivity(),data);
        setListAdapter(adapter);
//        setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
//                android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS));
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Comment>> loader) {
    }

    private void useLoader(){
        if(loader == null ) {
            loader = (CommentListLoader) getActivity().getLoaderManager()
                    .restartLoader(Constants.COMMENTS_LOADER_ID, null, this);
            loader.forceLoad();
        }
    }
}
