package com.mohammadsayed.mindvalley.downloader.cachemanagers;

import android.support.v4.util.LruCache;

/**
 * Created by mohammad on 8/2/17.
 */

public class TextCacheManager extends CacheManager<String> {

    private static TextCacheManager sTextCacheManager;

    private TextCacheManager() {
    }

    public static TextCacheManager getInstance() {
        if (sTextCacheManager == null) {
            synchronized (CacheManager.class) {
                if (sTextCacheManager == null) {
                    sTextCacheManager = new TextCacheManager();
                }
            }
        }
        return sTextCacheManager;
    }


    @Override
    protected void initializeMemoryCache() {
        if (getMemoryCache() == null) {
            synchronized (LruCache.class) {
                if (getMemoryCache() == null) {
                    // Get max available VM memory, exceeding this amount will throw an
                    // OutOfMemory exception. Stored in kilobytes as LruCache takes an
                    // int in its constructor.
                    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
                    // Use 1/8th of the available memory for this memory cache.
                    final int cacheSize = maxMemory / 8;
                    setMemoryCache(new LruCache<String, String>(cacheSize));
                }
            }
        }
    }


    public void addStringToMemoryCache(String key, String string) {
        if (getMemoryCache() == null || key == null || string == null) {
            return;
        }
        if (getStringFromMemCache(key) == null) {
            getMemoryCache().put(key, string);
        }
    }

    public String getStringFromMemCache(String key) {
        if (getMemoryCache() == null || key == null) {
            return null;
        }
        return getMemoryCache().get(key);
    }
}
