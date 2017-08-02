package com.mohammadsayed.mindvalley.downloader.cachemanagers;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.util.LruCache;

/**
 * Created by mohammad on 8/2/17.
 */

public class BitmapCacheManager extends CacheManager<Bitmap> {

    private static BitmapCacheManager sBitmapCacheManager;

    private BitmapCacheManager() {
    }

    public static BitmapCacheManager getInstance() {
        if (sBitmapCacheManager == null) {
            synchronized (CacheManager.class) {
                if (sBitmapCacheManager == null) {
                    sBitmapCacheManager = new BitmapCacheManager();
                }
            }
        }
        return sBitmapCacheManager;
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
                    setMemoryCache(new LruCache<String, Bitmap>(cacheSize) {
                        @Override
                        protected int sizeOf(String key, Bitmap bitmap) {
                            // The cache size will be measured in kilobytes rather than
                            // number of items.
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                                return bitmap.getByteCount() / 1024;
                            }
                            return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
                        }
                    });
                }
            }
        }
    }


    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getMemoryCache() == null || key == null || bitmap == null) {
            return;
        }
        if (getBitmapFromMemCache(key) == null) {
            getMemoryCache().put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        if (getMemoryCache() == null || key == null) {
            return null;
        }
        return getMemoryCache().get(key);
    }
}
