package com.mohammadsayed.mindvalley.downloader.cachemanagers;

import android.support.v4.util.LruCache;

/**
 * Created by mohammad on 8/2/17.
 */

public abstract class CacheManager<T> {

    //private static CacheManager sCacheManager;
    private LruCache<String, T> mMemoryCache;

    public CacheManager() {
        initializeMemoryCache();
    }

    /*public static CacheManager getInstance() {
        if (sCacheManager == null) {
            synchronized (CacheManager.class) {
                if (sCacheManager == null) {
                    sCacheManager = new CacheManager();
                }
            }
        }
        return sCacheManager;
    }*/

    protected abstract void initializeMemoryCache();

    public LruCache<String, T> getMemoryCache() {
        return mMemoryCache;
    }

    public void setMemoryCache(LruCache<String, T> memoryCache) {
        mMemoryCache = memoryCache;
    }
}
