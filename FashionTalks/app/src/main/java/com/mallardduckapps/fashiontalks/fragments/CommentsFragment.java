package com.mallardduckapps.fashiontalks.fragments;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.adapters.CommentListAdapter;
import com.mallardduckapps.fashiontalks.loaders.CommentListLoader;
import com.mallardduckapps.fashiontalks.objects.Comment;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommentsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<ArrayList<Comment>> {

    private static final String POST_ID = "POST_ID";
    private String paramPostId;
    CommentListLoader loader;
    Button sendButton;
    RelativeLayout sendMessageLayout;
    ProgressBar progressBar;
    CommentListAdapter adapter;
    ArrayList<Comment> dataList;
    EditText editText;
    boolean sendingMessage;

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
        dataList = new ArrayList<>();
        useLoader();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.comment_layout, container, false);
        editText = (EditText) view.findViewById(R.id.textField);
        sendMessageLayout = (RelativeLayout) view.findViewById(R.id.sendMessageLayout);
        sendMessageLayout.setVisibility(View.INVISIBLE);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
       // editText.setText();//"\ud83d" #0xd83d    \uD83D\uDE04 = 0xd83d0xde04
        //editText.setText("U+1F601" + "-" +"\ue32d" + " " + "\ud83d" + "-" + "\udc4d" + "-" + "\uef0f" + " - " + "\u2764");
        sendButton = (Button) view.findViewById(R.id.sendButton);
        sendButton.setTypeface(FTUtils.loadFont(getActivity().getAssets(), getActivity().getString(R.string.font_helvatica_md)));
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sendingMessage){
                    return;
                }
                BasicNameValuePair pair1 = new BasicNameValuePair("post_id", paramPostId);
                BasicNameValuePair pair2 = new BasicNameValuePair("comment", editText.getText().toString());
                SendCommentTask task = new SendCommentTask();
                task.execute(pair1, pair2);
            }
        });
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
            mListener.onFragmentInteraction("");
        }
    }

    @Override
    public Loader<ArrayList<Comment>> onCreateLoader(int id, Bundle args) {
        loader = new CommentListLoader(getActivity().getApplicationContext(), Constants.COMMENTS_LOADER_ID, paramPostId);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Comment>> loader, ArrayList<Comment> data) {
        if(adapter == null){
            dataList = data;
            adapter = new CommentListAdapter(getActivity(),dataList);
            setListAdapter(adapter);
        }else{

        }

        sendMessageLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                toggleMessageLayout();
            }
        }, 200);
        progressBar.setVisibility(View.INVISIBLE);
        if(data.size() == 0){

        }

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

    public void toggleMessageLayout(){

        Animation slide = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
        Animation.AnimationListener animationListener = new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                sendMessageLayout.setVisibility(View.VISIBLE);
                sendMessageLayout.invalidate();
                Log.d("ANIM", "ANIM VISIBLE");
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        if(slide != null){
            slide.reset();
            if(sendMessageLayout != null){
                Log.d("ANIM","ON CLICK - start animation");
                sendMessageLayout.clearAnimation();
                slide.setAnimationListener(animationListener);
                slide.setFillAfter(true);
                sendMessageLayout.startAnimation(slide);
                sendMessageLayout.invalidate();
            }
        }
    }

    public class SendCommentTask extends AsyncTask<BasicNameValuePair, Void, String> {

        private final String TAG = "SendCommentTask";
        private int status = -1;

        public SendCommentTask(){
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sendingMessage = true;
            //progressBarMain.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(BasicNameValuePair... params) {
            String response = "";
            RestClient restClient = new RestClient();
            try {
                String url = new StringBuilder(Constants.POST_COMMENT).toString();
                response = restClient.doPostRequestWithJSON(url,null, params[0], params[1]);
                Log.d(TAG, "User REQUEST RESPONSE: " + response);
                JSONObject object = new JSONObject(response);
                status = object.getInt("status");
             /*   if(status == 0){
                    JsonArray dataObjects = new JsonParser().parse(response).getAsJsonObject().getAsJsonArray("data");
                    Gson gson = new Gson();
                    Comment comment = gson.fromJson(dataObjects.get(0), Comment.class);
                    Log.d(TAG, "ADD COMMENT SUCCESS: old data size: " + dataList.size());
                    dataList.add(comment);
                    if(adapter != null){
                        adapter.notifyDataSetChanged();

                        Log.d(TAG, "ADD COMMENT SUCCESS: notified data size: " + dataList.size() );
                    }
                    //useLoader();
                }*/

            } catch (Exception e) {
                response = "NO_CONNECTION";
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            if(status == 0){
                sendingMessage = false;
                editText.setText("");
                status = -1;
                JsonArray dataObjects = new JsonParser().parse(text).getAsJsonObject().getAsJsonArray("data");
                Gson gson = new Gson();
                Comment comment = gson.fromJson(dataObjects.get(0), Comment.class);
                Log.d(TAG, "ADD COMMENT SUCCESS: old data size: " + dataList.size());
                dataList.add(comment);
                if(adapter != null){
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "ADD COMMENT SUCCESS: notified data size: " + dataList.size() );
                }
            }

        }
    }
}
