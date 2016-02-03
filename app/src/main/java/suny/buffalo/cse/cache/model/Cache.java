package suny.buffalo.cse.cache.model;

/**
 * Created by raman on 2/2/16.
 */
public class Cache {

    private static Cache mCache;

    public static Cache getCurrentCache() {
        if (null == mCache) {
            mCache = new Cache();
        }

        return mCache;
    }
}
