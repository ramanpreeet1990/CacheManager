package raman.cache.server;

/**
 * **************************************************************************************************************************
 * Developer : Ramanpreet Singh Khinda
 * <p/>
 * This class contains all the Global fields
 ***************************************************************************************************************************/
public class Globals {
    public static final String TAG =  "Raman_Cache_Server";

    public static final String GCM_SENDER_ID =  "67907436896";
    public static final String GCM_SERVER = "gcm.googleapis.com";
    public static final String GCM_SERVER_KEY = "AIzaSyBxKmFutsp-bs7TjBe2pRseKyXJuMmGi6E";
    public static final String GCM_ELEMENT_NAME  = "gcm";
    public static final String GCM_NAMESPACE = "google:mobile:data";

    public static final int GCM_PORT = 5235;
    public static final long GCM_TIME_TO_LIVE = 60L * 60L * 24L * 7L; // 1 Week

    //intents extra values
    public static final String BUNDLE = "bundle";
    public static final String EXTRA_USER_DEVICE_IMEI = "extra_user_device_imei";
    public static final String EXTRA_GCM_REG_ID = "extra_gcm_reg_id";
    public static final String EXTRA_TIME_OF_REQUEST = "extra_time_of_request";
    public static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_MESSAGE = "extra_message";
    public static final String EXTRA_DATA = "extra_data";

    public static final String GLOBAL_LOCK_ACQUIRED = "global_lock_acquired";
    public static final String ERROR_ACQUIRING_GLOBAL_LOCK = "error_acquiring_global_lock";
    public static final String GLOBAL_LOCK_RELEASED = "global_lock_released";


    // actions for GCM
    public static final String ACTION_REGISTER = "suny.buffalo.cse.cache.utility.connection.REGISTER";
    public static final String ACTION_REQUEST_GLOBAL_LOCK = "suny.buffalo.cse.cache.utility.connection.REQUEST_GLOBAL_LOCK";
    public static final String ACTION_WRITE_DATA = "suny.buffalo.cse.cache.utility.connection.WRITE_DATA";
    public static final String ACTION_NEW_DATA_AVAILABLE = "suny.buffalo.cse.cache.utility.connection.NEW_DATA_AVAILABLE";
    public static final String ACTION_GLOBAL_LOCK_ACQUIRED = "suny.buffalo.cse.cache.utility.connection.GLOBAL_LOCK_ACQUIRED";
    public static final String ACTION_GLOBAL_LOCK_RELEASED = "suny.buffalo.cse.cache.utility.connection.GLOBAL_LOCK_RELEASED";
}

