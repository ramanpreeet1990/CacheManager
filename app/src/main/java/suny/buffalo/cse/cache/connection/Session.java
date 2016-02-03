package suny.buffalo.cse.cache.connection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import suny.buffalo.cse.cache.model.Cache;
import suny.buffalo.cse.cache.utility.Globals;
import suny.buffalo.cse.cache.utility.Utility;

public class Session {
    private Activity context;
    private Cache mCache;
    private String gcmRegistrationId;
    public static Session mySessionObject = null;
    private GoogleCloudMessaging gcmInstance;
    private AtomicInteger msgId = new AtomicInteger();

    public Session(Activity context) {
        this.context = context;
        mCache = Cache.getCurrentCache();
    }

    public static Session getInstance(Activity context) {
        if (mySessionObject == null) {
            mySessionObject = new Session(context);
        }
        return mySessionObject;
    }

    public static Session getInstance() {
        return mySessionObject;
    }

    public boolean isDeviceRegistered() {
        return null != getRegistrationId(context);
    }

    public void registerDevice() {
        if (checkPlayServices()) {
            if (gcmInstance == null) {
                gcmInstance = GoogleCloudMessaging.getInstance(context);
            }

            if (isDeviceRegistered()) {
                sendMessage(Globals.REGISTER_LISTENER, Globals.ALREADY_REGISTERED, "");
            } else {
                registerInBackground();
            }
        } else {
            sendMessage(Globals.REGISTER_LISTENER, Globals.GOOGLE_PLAY_SERVICE_NOT_VALID,"");
        }
    }

    private void registerInBackground() {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String msg;
                try {
                    gcmRegistrationId = gcmInstance.register(Globals.GCM_SENDER_ID);
                    msg = "Device registered, registration ID=" + gcmRegistrationId;
                    sendRegistrationIdToBackend(gcmRegistrationId);
                } catch (Exception ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.v(Globals.TAG, "Error: " + msg);
                }

                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.v(Globals.TAG, msg);
            }
        }.execute();
    }


    private String gcmRegId;

    public void sendRegistrationIdToBackend(String gcmRegistrationId) {
        gcmRegId = gcmRegistrationId;
        new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                boolean success = false;

                try {
                    Bundle data = new Bundle();
                    data.putString(Globals.EXTRA_ACTION, Globals.ACTION_REGISTER);
                    data.putString(Globals.EXTRA_USER_DEVICE_IMEI, Utility.getDeviceImei(context));
                    data.putString(Globals.EXTRA_GCM_REG_ID, params[0]);
                    data.putString(Globals.EXTRA_TIME_OF_REQUEST, Utility.getCurrentDateTime());

                    String id = Integer.toString(msgId.incrementAndGet());
                    gcmInstance.send(Globals.GCM_SENDER_ID + "@gcm.googleapis.com", id, Globals.GCM_TIME_TO_LIVE, data);
                    success = true;
                } catch (IOException ex) {
                    success = false;
                    Log.v(Globals.TAG, "Error :" + ex.getMessage());
                }
                return success;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    Log.v(Globals.TAG, "Registration request sent");
                    storeRegistrationId(gcmRegId);
                }
            }
        }.execute(gcmRegistrationId);
    }

    public void requestGlobalCacheLock() {
        new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                boolean success = false;

                try {
                    Bundle data = new Bundle();
                    data.putString(Globals.EXTRA_ACTION, Globals.ACTION_REQUEST_GLOBAL_LOCK);
                    data.putString(Globals.EXTRA_USER_DEVICE_IMEI, Utility.getDeviceImei(context));
                    data.putString(Globals.EXTRA_TIME_OF_REQUEST, Utility.getCurrentDateTime());

                    String id = Integer.toString(msgId.incrementAndGet());
                    gcmInstance.send(Globals.GCM_SENDER_ID + "@gcm.googleapis.com", id, Globals.GCM_TIME_TO_LIVE, data);
                    success = true;

                } catch (IOException ex) {
                    success = false;
                    Log.v(Globals.TAG, "Error :" + ex.getMessage());
                }

                return success;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success)
                    Log.v(Globals.TAG, "Request for Global Cache Lock sent");
            }
        }.execute();
    }

    public void writeDataToGlobalCache(String data) {
        new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                boolean success = false;

                try {
                    Bundle data = new Bundle();
                    data.putString(Globals.EXTRA_ACTION, Globals.ACTION_WRITE_DATA);
                    data.putString(Globals.EXTRA_USER_DEVICE_IMEI, Utility.getDeviceImei(context));
                    data.putString(Globals.EXTRA_TIME_OF_REQUEST, Utility.getCurrentDateTime());
                    data.putString(Globals.EXTRA_DATA, params[0]);


                    String id = Integer.toString(msgId.incrementAndGet());
                    gcmInstance.send(Globals.GCM_SENDER_ID + "@gcm.googleapis.com", id, Globals.GCM_TIME_TO_LIVE, data);
                    success = true;

                } catch (IOException ex) {
                    success = false;
                    Log.v(Globals.TAG, "Error :" + ex.getMessage());
                }

                return success;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success)
                    Log.v(Globals.TAG, "Write data to Global Cache request sent");
            }
        }.execute(data);
    }

    /**
     * Gets the current registration ID for application on GCM service, if there
     * is one.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(Globals.PREFS_PROPERTY_REG_ID, "");

        if (registrationId == null || registrationId.equals("")) {
            Log.v(Globals.TAG, "Registration not found");
            return null;
        }

        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(Globals.APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion();
        if (registeredVersion != currentVersion) {
            Log.v(Globals.TAG, "App version changed");
            return null;
        }

        return registrationId;
    }

    private void storeRegistrationId(String gcmRegId) {
        int appVersion = getAppVersion();
        Log.v(Globals.TAG, "Saving Gcm Registration id on app version " + appVersion);

        final SharedPreferences prefs = getGcmPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(Globals.PREFS_PROPERTY_REG_ID, gcmRegId);
        editor.putInt(Globals.APP_VERSION, appVersion);
        editor.commit();
    }

    private void deleteStoredRegistrationId() {
        int appVersion = getAppVersion();
        Log.v(Globals.TAG, "Deleting Gcm Registration id because the device could not register succesfully");

        final SharedPreferences prefs = getGcmPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(Globals.PREFS_PROPERTY_REG_ID, null);
        editor.putInt(Globals.APP_VERSION, appVersion);
        editor.commit();
    }

    private SharedPreferences getGcmPreferences(Context context) {
        return context.getSharedPreferences(Globals.PREFS_NAME, Context.MODE_PRIVATE);
    }

    private int getAppVersion() {
        try {
            PackageInfo packageInfo;
            packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.v(Globals.TAG, "I never expected this! Going down, going down!" + e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If it
     * doesn't, display a dialog that allows users to download the APK from the
     * Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, context, Globals.PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.v(Globals.TAG, "This device is not supported");
            }
            return false;
        }
        return true;
    }

    public void sendMessage(String action, String msg, String data) {
        Intent intent = new Intent(action);
        intent.putExtra(Globals.EXTRA_MESSAGE, msg);
        intent.putExtra(Globals.EXTRA_DATA, data);

        if (action.contains(Globals.ACTION_REGISTER) && !msg.contains(Globals.REGISTERED_SUCCESSFULLY)) {
            deleteStoredRegistrationId();
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}