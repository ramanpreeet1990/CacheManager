package suny.buffalo.cse.cache.cachemanager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import suny.buffalo.cse.cache.connection.Session;
import suny.buffalo.cse.cache.utility.Globals;
import suny.buffalo.cse.cache.utility.Utility;

/*
* * Comment on my files : Testing
* */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Session sessionObject;
    private Button btnLock;
    private ProgressDialog ringProgressDialog;
    private EditText editCache;
    private int currentRequestState;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        res = getResources();
        sessionObject = Session.getInstance(this);
        editCache = (EditText) findViewById(R.id.edit_cache);
        btnLock = (Button) findViewById(R.id.btn_lock);

        editCache.setEnabled(false);
        btnLock.setEnabled(true);

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegisterReceiver, new IntentFilter(Globals.REGISTER_LISTENER));
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalLockReceiver, new IntentFilter(Globals.LOCAL_LOCK_LISTENER));
        LocalBroadcastManager.getInstance(this).registerReceiver(mGlobalLockReceiver, new IntentFilter(Globals.GLOBAL_LOCK_LISTENER));
        LocalBroadcastManager.getInstance(this).registerReceiver(mCacheReceiver, new IntentFilter(Globals.GLOBAL_CACHE_LISTENER));
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnLock.setOnClickListener(this);

        if (!Utility.isNetworkAvailable(this)) {
            showErrorDialog("Connection Error", "Network not available. Please check your internet connection and try again");
        }


        if (!sessionObject.isDeviceRegistered()) {
            launchRingDialog("Registering your device");
            sessionObject.registerDevice();
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onClick(View v) {
        if (!Utility.isNetworkAvailable(this)) {
            showErrorDialog("Connection Error", "Network not available. Please check your internet connection and try again");
        }

        switch (v.getId()) {
            case R.id.btn_lock:
                editCache.setEnabled(false);

                if (btnLock.getText().toString().contains(Globals.BUTTON_TEXT_REQUEST_LOCK)) {
                    currentRequestState = Globals.GLOBAL_LOCK_REQUESTED;
                    launchRingDialog("Requesting Lock");
                    sessionObject.requestGlobalCacheLock();
                } else {
                    currentRequestState = Globals.WRITE_DATA_REQUESTED;
                    launchRingDialog("Writing Data to Global Cache");
                    sessionObject.writeDataToGlobalCache(editCache.getText().toString());
                }
                break;

        }
    }

    private BroadcastReceiver mRegisterReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra(Globals.EXTRA_MESSAGE);
            String data = intent.getStringExtra(Globals.EXTRA_DATA);

            dismissProgressDialog();

            Log.v(Globals.TAG, "mRegisterReceiver received broadcasted message : Message : " + msg + ", data : " + data);


            if (null == msg) {
                showErrorDialog("Registration Error", "Sorry ! Device could not be registered. Seems problem connecting to Server. Please check your internet connection and try again");
            } else if (msg.contains(Globals.ALREADY_REGISTERED)) {
                showToast("Your device already registered. Kindly use the service :-)", Toast.LENGTH_SHORT);
            } else if (msg.contains(Globals.GOOGLE_PLAY_SERVICE_NOT_VALID)) {
                showErrorDialog("Device not supported", "Your device does not seems to support our service. Either upgrade Google Play Services apk or Android version");
            } else {
                editCache.setText(data);
                showToast("Registration Successful ! Kindly use the service :-)", Toast.LENGTH_SHORT);
            }
        }
    };

    private BroadcastReceiver mLocalLockReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra(Globals.EXTRA_MESSAGE);
            dismissProgressDialog();

            Log.v(Globals.TAG, "mLockReceiver received broadcasted message : Message : " + msg);

            if (null == msg) {
                return;
            }

            if (currentRequestState == Globals.GLOBAL_LOCK_REQUESTED) {
                if (msg.contains(Globals.GLOBAL_LOCK_ACQUIRED)) {
                    btnLock.setText(Globals.BUTTON_TEXT_WRITE_DATA);
                    showToast("Lock Request Approved !!!", Toast.LENGTH_SHORT);
                    editCache.setBackground(res.getDrawable(R.drawable.border_yellow));
                    editCache.setEnabled(true);
                } else {
                    btnLock.setText(Globals.BUTTON_TEXT_REQUEST_LOCK);
                    showToast("Sorry ! Lock Request NOT Approved", Toast.LENGTH_SHORT);
                    editCache.setBackground(res.getDrawable(R.drawable.border_red));
                    editCache.setEnabled(false);
                }
            } else if (currentRequestState == Globals.WRITE_DATA_REQUESTED) {
                editCache.setBackground(res.getDrawable(R.drawable.border_green));
                editCache.setEnabled(false);

                btnLock.setText(Globals.BUTTON_TEXT_REQUEST_LOCK);
                if (msg.contains("Success")) {
                    showToast("Data Written Successfully !!!", Toast.LENGTH_SHORT);
                } else {
                    showToast("Sorry ! Data Could NOT be written", Toast.LENGTH_SHORT);
                }

            }
        }
    };

    private BroadcastReceiver mGlobalLockReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra(Globals.EXTRA_MESSAGE);
            dismissProgressDialog();

            Log.v(Globals.TAG, "mGlobalLockReceiver received broadcasted message : Message : " + msg);


            btnLock.setText(Globals.BUTTON_TEXT_REQUEST_LOCK);
            editCache.setEnabled(false);

            if (null == msg) {
                return;
            }

            if (msg.contains(Globals.GLOBAL_LOCK_ACQUIRED)) {
                showToast("Cache has been locked", Toast.LENGTH_SHORT);
                editCache.setBackground(res.getDrawable(R.drawable.border_red));
                btnLock.setEnabled(false);
                btnLock.setBackground(res.getDrawable(R.drawable.button_background_disabled));

            } else if (msg.contains(Globals.GLOBAL_LOCK_RELEASED)) {
                showToast("Cache Lock has been released", Toast.LENGTH_SHORT);
                editCache.setBackground(res.getDrawable(R.drawable.border_green));
                btnLock.setEnabled(true);
                btnLock.setBackground(res.getDrawable(R.drawable.button_background_enabled));
            }

        }
    };

    private BroadcastReceiver mCacheReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra(Globals.EXTRA_DATA);
            dismissProgressDialog();
            editCache.setEnabled(false);

            Log.v(Globals.TAG, "mCacheReceiver received broadcasted message : Message : " + data);

            showToast("Cache Lock has been released", Toast.LENGTH_SHORT);

            showDialog("Global Cache Modified", "The data you are reading has become stale. Kindly tap on OK to refresh your data", data);
        }
    };

    public void showToast(String msg, int time) {
        Toast.makeText(this, msg, time).show();
    }

    private void showErrorDialog(String title, String msg) {
        if (!this.isFinishing()) {
            final Dialog errorDialog = new Dialog(this);
            errorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            errorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            errorDialog.setContentView(R.layout.dialog_error);
            errorDialog.setCancelable(false);
            errorDialog.show();

            Button btnOK = (Button) errorDialog.findViewById(R.id.btn_ok);
            TextView dialogTitle = (TextView) errorDialog.findViewById(R.id.dialog_toolbar_title);
            TextView dialogMessage = (TextView) errorDialog.findViewById(R.id.dialog_msg);

            dialogTitle.setText(title);
            dialogMessage.setText(msg);

            btnOK.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             errorDialog.dismiss();
                                             finish();
                                         }

                                     }

            );
        }
    }

    private void showDialog(String title, String msg, final String data) {
        if (!this.isFinishing()) {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.setContentView(R.layout.dialog_error);
            dialog.setCancelable(false);
            dialog.show();

            final Button btnOK = (Button) dialog.findViewById(R.id.btn_ok);
            TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialog_toolbar_title);
            TextView dialogMessage = (TextView) dialog.findViewById(R.id.dialog_msg);

            dialogTitle.setText(title);
            dialogMessage.setText(msg);

            btnOK.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             dialog.dismiss();
                                             editCache.setBackground(res.getDrawable(R.drawable.border_green));
                                             editCache.setText(data);
                                             btnLock.setEnabled(true);
                                             btnLock.setBackground(res.getDrawable(R.drawable.button_background_enabled));
                                         }

                                     }

            );
        }
    }

    private void launchRingDialog(String displayMessage) {
        ringProgressDialog = ProgressDialog.show(this, "Please wait ...", displayMessage, true);
        ringProgressDialog.setCancelable(false);
    }

    public void dismissProgressDialog() {
        if (ringProgressDialog != null && ringProgressDialog.isShowing()) {
            ringProgressDialog.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        dismissProgressDialog();
    }

}
