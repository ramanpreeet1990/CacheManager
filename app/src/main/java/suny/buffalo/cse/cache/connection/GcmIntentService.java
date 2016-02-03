package suny.buffalo.cse.cache.connection;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import suny.buffalo.cse.cache.utility.Globals;

/***************************************************************************************************
 * Developer : Ramanpreet Singh Khinda
 * <p/>
 * This class does the actual handling of the GCM message.
 * <p/>
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work.
 * <p/>
 * When the service is finished, it calls {@code completeWakefulIntent()} to release the wake lock.
 ***************************************************************************************************/
public class GcmIntentService extends IntentService {
    private String action, msg, data;
    private Session sessionObject;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        // The getMessageType() intent parameter must be the intent you receive in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle

            /*
            * Filter messages based on message type.Since it is likely that GCM will be
            * extended in the future with new message types, just ignore any message types you 're
            * not interested in, or that you don 't recognize.
            */

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Log.v(Globals.TAG, "Error while receiving gcm notification");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                Log.v(Globals.TAG, "Deleted gcm notification");

                // If it's a regular GCM message, do some work
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // Post notification of received message
                String message = extras.toString();
                pushNotification(extras);

                Log.v(Globals.TAG, "Received: " + message);
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    public void pushNotification(Bundle bundle) {
        Log.v(Globals.TAG, "GcmIntentService Broadcasting Notification");

        if (bundle == null)
            return;

        sessionObject = Session.getInstance();

        if (null == sessionObject)
            return;

        action = bundle.getString(Globals.EXTRA_ACTION);
        msg = bundle.getString(Globals.EXTRA_MESSAGE);
        data = bundle.getString(Globals.EXTRA_DATA);

        if (action.contains(Globals.ACTION_REGISTER)) {
            sessionObject.sendMessage(Globals.REGISTER_LISTENER, msg, data);

        } else if (action.contains(Globals.ACTION_REQUEST_GLOBAL_LOCK) || action.contains(Globals.ACTION_WRITE_DATA)) {
            sessionObject.sendMessage(Globals.LOCAL_LOCK_LISTENER, msg, data);

        } else if(action.contains(Globals.ACTION_NEW_DATA_AVAILABLE)){
            sessionObject.sendMessage(Globals.GLOBAL_CACHE_LISTENER, msg, data);

        } else if(action.contains(Globals.ACTION_GLOBAL_LOCK_ACQUIRED) || action.contains(Globals.GLOBAL_LOCK_RELEASED)){
            sessionObject.sendMessage(Globals.GLOBAL_LOCK_LISTENER, msg, data);
        }

    }
}