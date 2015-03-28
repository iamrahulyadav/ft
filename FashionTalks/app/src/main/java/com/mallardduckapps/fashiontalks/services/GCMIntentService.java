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
import com.mallardduckapps.fashiontalks.MainActivity;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.objects.Notification;

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

    public GCMIntentService() {
        super("GcmIntentService");
        Log.d(TAG, "GCM INTENT SERVICE");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
//                for (int i=0; i<5; i++) {
//                    Log.i(TAG, "Working... " + (i+1)
//                            + "/5 @ " + SystemClock.elapsedRealtime());
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                    }
//                }
                //Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.

                //Received: Bundle[{custom={"notification":{"type":"x","target_id":1}},
                // from=337533113430,
                // message=This is a test message from FashionTalks,
                // android.support.content.wakelockid=1, collapse_key=do_not_collapse}]

                sendNotification(parseMessage(extras));
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

    private String parseMessage(Bundle bundle){
        return bundle.getString("message");
        //Log.d(TAG, "")
    }

    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.app_logo)
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
//TODO android:launchMode="singleTop" try this for opening in the same instance
        //TODO id sets a new notification rather than putting in notification stack
        mNotificationManager.notify("FASHION_TALKS", NOTIFICATION_ID, mBuilder.build());
        NOTIFICATION_ID++;
    }
}
