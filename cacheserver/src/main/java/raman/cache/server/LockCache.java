package raman.cache.server;

/**
 * Created by raman on 2/3/16.
 * <p/>
 * Reference : http://tutorials.jenkov.com/java-concurrency/locks.html
 */
public class LockCache {
    private boolean isLocked = false;
    private static LockCache mLockCache;
    private String cacheData;

    public static LockCache getInstance() {
        if (mLockCache == null) {
            mLockCache = new LockCache();
        }
        return mLockCache;
    }

    public synchronized void lock() throws InterruptedException {
        while (isLocked) {
            wait();
        }
        isLocked = true;
    }

    public synchronized void unlock() {
        isLocked = false;
        notify();
    }

    public void setCacheData(String cacheData) {
        this.cacheData = cacheData;
    }

    public String getCacheData() {
        return cacheData;
    }

    public boolean isDataLocked() {
        return isLocked;
    }
}
