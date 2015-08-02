package com.mallardduckapps.fashiontalks.fragments;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.mallardduckapps.fashiontalks.FashionTalksApp;
import com.mallardduckapps.fashiontalks.ProfileActivity;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.adapters.CommentListAdapter;
import com.mallardduckapps.fashiontalks.loaders.CommentListLoader;
import com.mallardduckapps.fashiontalks.loaders.Exclude;
import com.mallardduckapps.fashiontalks.objects.Comment;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.swipelistview.BaseSwipeListViewListener;
import com.mallardduckapps.fashiontalks.swipelistview.SwipeListView;
import com.mallardduckapps.fashiontalks.utils.Constants;
import com.mallardduckapps.fashiontalks.utils.FTUtils;
import com.mallardduckapps.fashiontalks.utils.SwipeListViewSettingsManager;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommentsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<ArrayList<Comment>>, CommentListAdapter.CommentAction {

    private static final String POST_ID = "POST_ID";
    private String paramPostId;
    private int postLoaderId;
    private int postIndex = -1;
    CommentListLoader loader;
    Button sendButton;
    RelativeLayout sendMessageLayout;
    RelativeLayout progressBarLayout;
    ProgressBar progressBar;
    CommentListAdapter adapter;
    ArrayList<Comment> dataList;
    EditText editText;
    boolean sendingMessage = false;
    boolean deletingMessage = false;
    FashionTalksApp app;
    CommentIsMade callback;
    boolean ownPost;

    private BasicFragment.OnFragmentInteractionListener mListener;

    // TODO: Rename and change types of parameters
    public static CommentsFragment newInstance(String postId, int loaderId, int postIndex, boolean ownPost) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putString(POST_ID, postId);
        args.putInt("POST_LOADER_ID", loaderId);
        args.putInt("POST_INDEX", postIndex);
        args.putBoolean("OWN_POST", ownPost);
        fragment.setArguments(args);
        return fragment;
    }

    public CommentsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (FashionTalksApp) getActivity().getApplication();
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            paramPostId = getArguments().getString(POST_ID);
            postLoaderId = getArguments().getInt("POST_LOADER_ID");
            postIndex = getArguments().getInt("POST_INDEX");
            ownPost = getArguments().getBoolean("OWN_POST");
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
        progressBarLayout = (RelativeLayout) view.findViewById(R.id.progressBarLayout);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
       // editText.setText();//"\ud83d" #0xd83d    \uD83D\uDE04 = 0xd83d0xde04
        //editText.setText("U+1F601" + "-" +"\ue32d" + " " + "\ud83d" + "-" + "\udc4d" + "-" + "\uef0f" + " - " + "\u2764");
        sendButton = (Button) view.findViewById(R.id.sendButton);
        sendButton.setTypeface(FTUtils.loadFont(getActivity().getAssets(), getActivity().getString(R.string.font_helvatica_md)));
        sendingMessage = false;
        deletingMessage = false;
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sendingMessage || deletingMessage){
                    return;
                }
                BasicNameValuePair pair1 = new BasicNameValuePair("post_id", paramPostId);
                BasicNameValuePair pair2 = new BasicNameValuePair("comment", editText.getText().toString().trim());
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
            callback = (CommentIsMade)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        hideKeyboard();
        super.onDetach();
        mListener = null;
        callback = null;
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

    private void reloadSwipeListView(SwipeListView swipeListView) {
        SwipeListViewSettingsManager settings = SwipeListViewSettingsManager.getInstance();
        int[] size = FTUtils.getScreenSize(getActivity());
        int offsetSize = 80;
        if(ownPost){
            offsetSize = 160;
        }

        settings.setSwipeOffsetLeft(size[0]- (int)FTUtils.pxFromDp(offsetSize, getActivity()));
        //swipeListView.setSwipeMode(settings.getSwipeMode());
        //swipeListView.setSwipeActionLeft(settings.getSwipeActionLeft());
        //swipeListView.setSwipeActionRight(settings.getSwipeActionRight());
        swipeListView.setOffsetLeft(settings.getSwipeOffsetLeft());
        //swipeListView.setOffsetRight(convertDpToPixel(settings.getSwipeOffsetRight()));
        //swipeListView.setAnimationTime(settings.getSwipeAnimationTime());
        //swipeListView.setSwipeOpenOnLongPress(settings.isSwipeOpenOnLongPress());
    }

    public void setListViewProperties(SwipeListView listView){
        //listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        reloadSwipeListView(listView);
        listView.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {
            }

            @Override
            public void onClosed(int position, boolean fromRight) {
            }

            @Override
            public void onListChanged() {
            }

            @Override
            public void onMove(int position, float x) {
            }

            @Override
            public void onStartOpen(int position, int action, boolean right) {
                Log.d("swipe", String.format("onStartOpen %d - action %d", position, action));
            }

            @Override
            public void onStartClose(int position, boolean right) {
                Log.d("swipe", String.format("onStartClose %d", position));
            }

            @Override
            public void onClickFrontView(int position) {
                Log.d("swipe", String.format("onClickFrontView %d", position));
            }

            @Override
            public void onClickBackView(int position) {
                Log.d("swipe", String.format("onClickBackView %d", position));
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {
                Log.d("swipe", String.format("onDismiss %d"));
                for (int position : reverseSortedPositions) {
                    dataList.remove(position);
                }
                adapter.notifyDataSetChanged();
            }

        });
    }

    @Override
    public Loader<ArrayList<Comment>> onCreateLoader(int id, Bundle args) {
        loader = new CommentListLoader(getActivity().getApplicationContext(), Constants.COMMENTS_LOADER_ID, paramPostId);
        return loader;
    }

    SwipeListView listView;
    @Override
    public void onLoadFinished(Loader<ArrayList<Comment>> loader, ArrayList<Comment> data) {

        if(data == null){
            Log.d("Comments Fragment", "DATA IS NULL");
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    app.openOKDialog(CommentsFragment.this.getActivity(), CommentsFragment.this, "no_connection");
                }
            });
            return;
        }

        if(adapter == null){
            dataList = data;
            adapter = new CommentListAdapter(getActivity(),this,dataList, ownPost);
            //setListAdapter(adapter);
            listView = (SwipeListView)getListView();
            listView.setAdapter(adapter);

            setListViewProperties(listView);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d("COMMENTS FRAGMENT", "ON OPTIONS " + item.getItemId() + "- home: " + android.R.id.home);
        if(item.getItemId() == android.R.id.home){
            Log.d("COMMENTS FRAGMENT", "ON DETACH HIDE KEYBOARD");
            hideKeyboard();
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            Log.d("TAG", "HIDE KEYBOARD");
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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

    @Override
    public void doCommentAction(String commentAction, Comment comment, int position) {
        if(commentAction.equals("delete")){
            Log.d("COMMENT","DELETE");
            if(deletingMessage || sendingMessage){
                return;
            }
            DeleteCommentTask commentTask = new DeleteCommentTask(comment.getId(), position);
            commentTask.execute();
            listView.closeAnimate(position);
        }else if(commentAction.equals("reply")){
            String userName = dataList.get(position).getUser().getUserName();
            Log.d("COMMENT", "REPLY to "+ userName);
            editText.setText(editText.getText().toString().concat("@").concat(userName));
            editText.setSelection(editText.length());
            listView.closeAnimate(position);
        }
    }

    @Override
    public void openUserProfile(String commentAction, User user, int position) {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
//        User user = dataList.get(position);
        app.setOther(user);
        intent.putExtra("PROFILE_ID", user.getId());
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public interface CommentIsMade{
         void onNewComment(int postLoaderId, int postId, int postIndex, boolean increment);
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
            progressBarLayout.setVisibility(View.VISIBLE);
            //progressBarMain.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            sendingMessage = false;
            progressBarLayout.setVisibility(View.GONE);
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
                JsonArray dataObjects = new JsonParser().parse(text).getAsJsonObject().getAsJsonArray("data");
                Exclude ex = new Exclude();
                Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(ex).addSerializationExclusionStrategy(ex).create();
                try{
                    Comment comment = gson.fromJson(dataObjects.get(0), Comment.class);
                    //Log.d(TAG, "ADD COMMENT SUCCESS: old data size: " + dataList.size());
                    dataList.add(comment);
                    if(adapter != null){
                        adapter.notifyDataSetChanged();
                        callback.onNewComment(postLoaderId, Integer.parseInt(paramPostId), postIndex, true);
                        status = -1;
                        Log.d(TAG, "ADD COMMENT SUCCESS: notified data size: " + dataList.size() );
                    }
                }catch(IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
            }else{
                app.openOKDialog(CommentsFragment.this.getActivity(), CommentsFragment.this, "no_connection");
            }
            progressBarLayout.setVisibility(View.GONE);

        }
    }

    public class DeleteCommentTask extends AsyncTask<BasicNameValuePair, Void, String> {

        private final String TAG = "DeleteCommentTask";
        private int status = -1;
        private int commentId;
        private int position;

        public DeleteCommentTask(int commentId, int position){
            this.commentId = commentId;
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            deletingMessage = true;
            progressBarLayout.setVisibility(View.VISIBLE);
            //progressBarMain.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            deletingMessage = false;
            progressBarLayout.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(BasicNameValuePair... params) {
            String response = "";
            RestClient restClient = new RestClient();
            try {
                String url = new StringBuilder(Constants.DELETE_COMMENT).append(commentId).toString();
                response = restClient.doGetRequest(url, null);
                Log.d(TAG, "DELETE COMMENT REQUEST RESPONSE: " + response);
                JSONObject object = new JSONObject(response);
                status = object.getInt("status");
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
                deletingMessage = false;
                editText.setText("");
                //JsonArray dataObjects = new JsonParser().parse(text).getAsJsonObject().getAsJsonArray("data");
                //Gson gson = new Gson();
                //Comment comment = gson.fromJson(dataObjects.get(0), Comment.class);
                //Log.d(TAG, "ADD COMMENT SUCCESS: old data size: " + dataList.size());
               // dataList.add(comment);
                if(adapter != null){
                   // adapter.notifyDataSetChanged();
                    //callback.onNewComment(postLoaderId, Integer.parseInt(paramPostId), postIndex);
                    dataList.remove(position);
                    if(adapter != null){
                        adapter.notifyDataSetChanged();
                        status = -1;
                        callback.onNewComment(postLoaderId, Integer.parseInt(paramPostId), postIndex, false);
                    Log.d(TAG, "DELETE COMMENT SUCCESS: notified data size: " + dataList.size() );
                }

            }else{
                app.openOKDialog(CommentsFragment.this.getActivity(), CommentsFragment.this, "no_connection");
            }
                progressBarLayout.setVisibility(View.GONE);
        }
    }
}}
