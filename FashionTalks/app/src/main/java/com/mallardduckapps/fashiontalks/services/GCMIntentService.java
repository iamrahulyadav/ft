package com.mallardduckapps.fashiontalks.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mallardduckapps.fashiontalks.FashionTalksApp;
import com.mallardduckapps.fashiontalks.LoginActivity;
import com.mallardduckapps.fashiontalks.MainActivity;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.objects.Notification;
import com.mallardduckapps.fashiontalks.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

//import android.support.v4.app.NotificationCompat;
//import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GCMIntentService extends IntentService {

    public static int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
   // NotificationCompat.Builder builder;
    final static String TAG = "GCMIntentService";
    FashionTalksApp app;

    public GCMIntentService() {
        super("GcmIntentService");
        Log.d(TAG, "GCM INTENT SERVICE");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        app = (FashionTalksApp)this.getApplication();
        app.newNotification = true;
        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle

            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString(), null);
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString(), null);
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                //Received: Bundle[{custom={"notification":{"type":"x","target_id":1}},
                // from=337533113430,
                // message=This is a test message from FashionTalks,
                // android.support.content.wakelockid=1, collapse_key=do_not_collapse}]
                Log.i(TAG, "Received: " + extras.toString());
                sendNotification(parseMessage(extras), parseNotification(extras.getString("custom")));
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

    private Notification parseNotification(String message){
        try {
            JSONObject json = new JSONObject(message);
            JSONObject notif = json.getJSONObject("notification");
            String type = notif.getString("type");
            String targetId = notif.getString("target_id");
            //Log.d(TAG, "NOTIF type: " + type + " - targetId: " + targetId);
            Notification not = new Notification();
            not.setTargetAction(type);
            not.setTargetId(Integer.parseInt(targetId));
            not.setContent(message);
            return not;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String parseMessage(Bundle bundle){
        return bundle.getString("message");
    }

    private Intent goToTarget(boolean myPost, Notification notification){
        Intent intent = null;
        Bundle bundle = new Bundle();
        if(notification == null){
            intent = new Intent(this, MainActivity.class);
            //app.direction = "MAIN";
            Log.d(TAG, "NOTIF NULL!");
        }else if(notification.getTargetAction().equals(Constants.TARGET_PROFILE)){
            Log.d(TAG, "NOTIF PROFILE!: " + notification.getTargetAction() + "- id: " + notification.getTargetId());
           // intent = new Intent(this, ProfileActivity.class);
            intent = new Intent(this, LoginActivity.class);//NotificationActivity
            bundle.putInt("PROFILE_ID", notification.getTargetId());
            bundle.putString("DIRECTION", "NOTIFICATION");
            //app.direction = "NOTIFICATION";
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            //startActivity(intent);
            //BaseActivity.setTranslateAnimation(getActivity());
        }else if(notification.getTargetAction().equals(Constants.TARGET_POST)){
            //intent = new Intent(this, PostsActivity.class);
            intent = new Intent(this, LoginActivity.class);//NotificationActivity.class
            bundle.putInt("LOADER_ID", myPost ? Constants.NOTIFICATION_MY_POST_LOADER_ID :  Constants.NOTIFICATION_OTHER_POST_LOADER_ID);
            bundle.putInt("POST_ID", notification.getTargetId());
            bundle.putString("DIRECTION", "NOTIFICATION");
            //app.direction = "NOTIFICATION";
            //intent.putExtra("POST_INDEX", -1);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            //this.startActivity(intent);
           // BaseActivity.setTranslateAnimation(getActivity());
        }else if(notification.getTargetAction().equals(Constants.TARGET_COMMENT)){
            //intent = new Intent(this, PostsActivity.class);
            intent = new Intent(this, LoginActivity.class);
            intent.putExtra("LOADER_ID", myPost ? Constants.NOTIFICATION_MY_POST_LOADER_ID :  Constants.NOTIFICATION_OTHER_POST_LOADER_ID);
            intent.putExtra("POST_ID", notification.getTargetId());
            intent.putExtra("OPEN_COMMENT", true);
            bundle.putString("DIRECTION", "NOTIFICATION");
            //app.direction = "NOTIFICATION";
            //intent.putExtra("POST_INDEX", -1);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            //this.startActivity(intent);
            //BaseActivity.setTranslateAnimation(getActivity());
        }else{
            intent = new Intent(this, LoginActivity.class);//MainActivity
            Log.d(TAG, "NOTIFICATION SOURCELESS!");
            //app.direction = "MAIN";
            //intent.putExtra("DIRECTION", "MAINACTIVITY");
        } //|Intent.FLAG_ACTIVITY_NEW_TASK

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP );
        //intent.setAction(Intent.ACTION_MAIN);
        //intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.putExtras(bundle);

        return intent;
    }

    private void sendNotification(String msg, Notification notification) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        //notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                goToTarget(true,notification),PendingIntent.FLAG_UPDATE_CURRENT );//PendingIntent.FLAG_CANCEL_CURRENT

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.applogo)
                        .setContentTitle("FashionTalks")
                        .setAutoCancel(true)
                        .setDefaults(android.app.Notification.DEFAULT_SOUND | android.app.Notification.DEFAULT_VIBRATE)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);

        try{
            mNotificationManager.cancel(NOTIFICATION_ID - 1);
        }catch(Exception e){
            Log.e(TAG, "Exception on cancelling previous notification");
        }
        android.app.Notification not = mBuilder.build();
        not.defaults |= android.app.Notification.DEFAULT_SOUND;
        not.defaults |= android.app.Notification.DEFAULT_VIBRATE;
        not.defaults |= android.app.Notification.DEFAULT_LIGHTS;
        not.flags =   android.app.Notification.FLAG_AUTO_CANCEL; // android.app.Notification.FLAG_ONGOING_EVENT |

//TODO android:launchMode="singleTop" try this for opening in the same instance
        //TODO id sets a new notification rather than putting in notification stack
        mNotificationManager.notify("FASHION_TALKS", NOTIFICATION_ID, not);
        NOTIFICATION_ID++;
    }
}
