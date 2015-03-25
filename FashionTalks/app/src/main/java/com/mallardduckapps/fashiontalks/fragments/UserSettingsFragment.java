package com.mallardduckapps.fashiontalks.fragments;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.loaders.GetSettingsLoader;
import com.mallardduckapps.fashiontalks.loaders.SearchUserLoader;
import com.mallardduckapps.fashiontalks.objects.Settings;
import com.mallardduckapps.fashiontalks.objects.User;
import com.mallardduckapps.fashiontalks.services.RestClient;
import com.mallardduckapps.fashiontalks.utils.Constants;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 23/02/15.
 */
public class UserSettingsFragment extends BasicFragment implements LoaderManager.LoaderCallbacks<Settings>, ToggleButton.OnCheckedChangeListener {

    ToggleButton talksToggle, newsToggle, newGlamEmailToggle, newGlamNotificationToggle,
            newTalkEmailToggle, newTalkNotificationToggle, newMentionEmailToggle,
            newMentionNotificationToggle, newFollowerEmailToggle, newFollowerNotificationToggle;

    GetSettingsLoader loader;
    final String NOTIFY_GLAM_EMAIL = "notify_glam_email";
    final String NOTIFY_GLAM_PUSH = "notify_glam_push";
    final String NOTIFY_COMMENT_EMAIL = "notify_comment_email";
    final String NOTIFY_COMMENT_PUSH = "notify_comment_push";
    final String NOTIFY_MENTION_EMAIL = "notify_mention_email";
    final String NOTIFY_MENTION_PUSH = "notify_mention_push";
    final String NOTIFY_FOLLOWER_EMAIL = "notify_follower_email";
    final String NOTIFY_FOLLOWER_PUSH = "notify_follower_push";

    boolean settingsReady = false;

    @Override
    public void setTag() {
        TAG = "USER_SETTINGS_FRAGMENT";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsReady = false;
        useLoader();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_user_settings, container, false);
        talksToggle = (ToggleButton) rootView.findViewById(R.id.talksToggle);
        newsToggle = (ToggleButton) rootView.findViewById(R.id.newsToggle);
        newGlamEmailToggle = (ToggleButton) rootView.findViewById(R.id.newGlamEmailToggle);
        newGlamNotificationToggle = (ToggleButton) rootView.findViewById(R.id.newGlamNotificationToggle);
        newTalkEmailToggle = (ToggleButton) rootView.findViewById(R.id.newTalkEmailToggle);
        newTalkNotificationToggle = (ToggleButton) rootView.findViewById(R.id.newTalkNotificationToggle);
        newMentionEmailToggle = (ToggleButton) rootView.findViewById(R.id.newMentionEmailToggle);
        newMentionNotificationToggle = (ToggleButton) rootView.findViewById(R.id.newMentionNotificationToggle);
        newFollowerEmailToggle = (ToggleButton) rootView.findViewById(R.id.newFollowerEmailToggle);
        newFollowerNotificationToggle = (ToggleButton) rootView.findViewById(R.id.newFollowerNotificationToggle);
        newGlamEmailToggle.setOnCheckedChangeListener(this);
        newGlamNotificationToggle.setOnCheckedChangeListener(this);
        newTalkEmailToggle.setOnCheckedChangeListener(this);
        newTalkNotificationToggle.setOnCheckedChangeListener(this);
        newMentionEmailToggle.setOnCheckedChangeListener(this);
        newMentionNotificationToggle.setOnCheckedChangeListener(this);
        newFollowerEmailToggle.setOnCheckedChangeListener(this);
        newFollowerNotificationToggle.setOnCheckedChangeListener(this);
        //TODO NEWS TOGGLE TALKS TOGGLE

        return rootView;
    }

    public void useLoader() {
        Log.d(TAG, "USE LOADER - START");
        loader = (GetSettingsLoader) getActivity().getLoaderManager()
                .restartLoader(Constants.SETTINGS_LOADER_ID, null, this);
        loader.forceLoad();
    }

    @Override
    public Loader<Settings> onCreateLoader(int id, Bundle args) {
        loader = new GetSettingsLoader(getActivity(), Constants.SETTINGS_LOADER_ID);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Settings> loader, Settings data) {
        Log.d(TAG, "SETTINGS LOADED");
        // talksToggle.setChecked(data.get);
        newGlamEmailToggle.setChecked(data.getIsGlamEmailOn() == 1 ? true : false);
        newGlamNotificationToggle.setChecked(data.getIsGlamPushOn() == 1 ? true : false);
        newTalkEmailToggle.setChecked(data.getIsCommentEmailOn() == 1 ? true : false);
        newTalkNotificationToggle.setChecked(data.getIsCommentPushOn() == 1 ? true : false);
        newMentionEmailToggle.setChecked(data.getIsMentionEmailOn() == 1 ? true : false);
        newMentionNotificationToggle.setChecked(data.getIsMentionPushOn() == 1 ? true : false);
        newFollowerEmailToggle.setChecked(data.getIsFollowerEmailOn() == 1 ? true : false);
        newFollowerNotificationToggle.setChecked(data.getIsFollowerPushOn() == 1 ? true : false);
        newsToggle.setChecked(data.getCanReceiveEmail() == 1 ? true : false);
        settingsReady = true;
    }

    @Override
    public void onLoaderReset(Loader<Settings> loader) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!settingsReady){
            return;
        }
        String buttonName = "";
        if(buttonView.equals(newGlamEmailToggle)){
            buttonName = NOTIFY_GLAM_EMAIL;
        }else if(buttonView.equals(newGlamNotificationToggle)){
            buttonName = NOTIFY_GLAM_PUSH;
        }else if(buttonView.equals(newTalkEmailToggle)){
            buttonName = NOTIFY_COMMENT_EMAIL;
        }else if(buttonView.equals(newTalkNotificationToggle)){
            buttonName = NOTIFY_COMMENT_PUSH;
        }else if(buttonView.equals(newMentionEmailToggle)){
            buttonName = NOTIFY_MENTION_EMAIL;
        }else if(buttonView.equals(newMentionNotificationToggle)){
            buttonName = NOTIFY_MENTION_PUSH;
        }else if(buttonView.equals(newFollowerEmailToggle)){
            buttonName = NOTIFY_FOLLOWER_EMAIL;
        }else if(buttonView.equals(newFollowerNotificationToggle)){
            buttonName = NOTIFY_FOLLOWER_PUSH;
        }
        ToggleSettingTask task = new ToggleSettingTask(buttonName);
        task.execute();
    }

    public class ToggleSettingTask extends AsyncTask<String, Void, String> {
        private final String TAG = "ToogleSettingTask";
        String settingName;

        public ToggleSettingTask(final String settingName) {
            this.settingName = settingName;
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            RestClient restClient = new RestClient();
            try {
                String url = new StringBuilder(Constants.SETTINGS_TOGGLE_PREFIX).append(settingName).toString();
                response = restClient.doGetRequest(url, null);
                Log.d(TAG, "TOGGLE RESPONSE: " + response);
            } catch (Exception e) {
                response = "NO_CONNECTION";
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);

        }
    }
}
