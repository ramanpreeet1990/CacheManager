package suny.buffalo.cse.cache.utility;

public class Globals {
    public static final String TAG = "Raman_Cache_Manager";

    public static final String GCM_SENDER_ID = "67907436896";
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final long GCM_TIME_TO_LIVE = 60L * 60L * 24L * 7L; // 1 Week

    public static final String PREFS_NAME = "Cache_Manager";
    public static final String PREFS_PROPERTY_REG_ID = "registration_id";
    public static final String APP_VERSION = "appVersion";

    public static String GOOGLE_PLAY_SERVICE_NOT_VALID = "No valid Google Play Services APK found";

    public static final String REGISTER_LISTENER = "register_listener";
    public static final String LOCAL_LOCK_LISTENER = "local_lock_listener";
    public static final String GLOBAL_LOCK_LISTENER = "global_lock_listener";
    public static final String GLOBAL_CACHE_LISTENER = "global_cache_listener";


    public static final String RECEIVED_DATA_LISTENER = "received_data_listener";

    public static String REGISTERED_SUCCESSFULLY = "has been registered with Raman Cache Server Successfully";
    public static String ALREADY_REGISTERED = "is already registered with Raman Cache Server";
    public static String ERROR_REGISTRATION = "could not be registered with Raman Cache Server";

    public static String ERROR_AUTHENCITY_NOT_VERIFIED = "error_authencity_not_verified";
    public static String AUTHENCITY_VERIFIED = "authencity_verified";


    public static final String EXTRA_USER_DEVICE_IMEI = "extra_user_device_imei";
    public static final String EXTRA_GCM_REG_ID = "extra_gcm_reg_id";
    public static final String EXTRA_TIME_OF_REQUEST = "extra_time_of_request";
    public static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_MESSAGE = "extra_message";
    public static final String EXTRA_DATA = "extra_data";

    public static final String BUTTON_TEXT_WRITE_DATA = "Write Data";
    public static final String BUTTON_TEXT_REQUEST_LOCK = "Request Lock";

    // Current Request State
    public static final int GLOBAL_LOCK_REQUESTED = 0;
    public static final int WRITE_DATA_REQUESTED = 1;

    public static final String GLOBAL_LOCK_ACQUIRED = "global_lock_acquired";
    public static final String GLOBAL_LOCK_RELEASED = "global_lock_released";
    public static final String ERROR_ACQUIRING_GLOBAL_LOCK = "error_acquiring_global_lock";

    // Cache State
    public static final boolean CACHE_IS_LOCKED = false;
    public static final boolean CACHE_IS_STALE = false;

    // actions for GCM
    public static final String ACTION_REGISTER = "suny.buffalo.cse.cache.utility.connection.REGISTER";
    public static final String ACTION_REQUEST_GLOBAL_LOCK = "suny.buffalo.cse.cache.utility.connection.REQUEST_GLOBAL_LOCK";
    public static final String ACTION_WRITE_DATA = "suny.buffalo.cse.cache.utility.connection.WRITE_DATA";
    public static final String ACTION_NEW_DATA_AVAILABLE = "suny.buffalo.cse.cache.utility.connection.NEW_DATA_AVAILABLE";
    public static final String ACTION_GLOBAL_LOCK_ACQUIRED = "suny.buffalo.cse.cache.utility.connection.GLOBAL_LOCK_ACQUIRED";
    public static final String ACTION_GLOBAL_LOCK_RELEASED = "suny.buffalo.cse.cache.utility.connection.GLOBAL_LOCK_RELEASED";
}