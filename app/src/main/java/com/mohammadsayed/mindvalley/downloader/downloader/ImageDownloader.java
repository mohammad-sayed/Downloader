package com.mohammadsayed.mindvalley.downloader.downloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import com.mohammadsayed.mindvalley.downloader.data.DownloadResult;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by mohammad on 7/28/17.
 */

public class ImageDownloader extends Downloader {

    private Drawable mDrawablePlaceholder;
    private Drawable mDrawableError;
    private ImageView mImageView;
    private Drawable mDrawableImage;
    private OnDownloadCompletedListener mOnDownloadCompletedListener;
    private Bitmap mBitmap;

    private static LruCache<String, Bitmap> mMemoryCache;
    private boolean cacheEnabled = true;
    private boolean cachedBitmap = false;


    private ImageDownloader(Context context) {
        super(context);
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        if (mMemoryCache == null) {
            synchronized (LruCache.class) {
                if (mMemoryCache == null) {
                    mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                        @Override
                        protected int sizeOf(String key, Bitmap bitmap) {
                            // The cache size will be measured in kilobytes rather than
                            // number of items.
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                                return bitmap.getByteCount() / 1024;
                            }
                            return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
                        }
                    };
                }
            }
        }
    }


    public static ImageDownloader with(Context context) {
        return new ImageDownloader(context);
    }

    @Override
    public ImageDownloader fromUrl(String url) {
        return (ImageDownloader) super.fromUrl(url);
    }

    public ImageDownloader fromDrawable(int resId) {
        mDrawableImage = getDrawable(resId);
        return this;
    }

    public ImageDownloader fromDrawable(Drawable drawable) {
        this.mDrawableImage = drawable;
        return this;
    }

    public ImageDownloader fromBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
        return this;
    }

    public ImageDownloader placeholder(int resId) {
        mDrawablePlaceholder = getDrawable(resId);
        return this;
    }

    public ImageDownloader placeholder(Drawable drawable) {
        mDrawablePlaceholder = drawable;
        return this;
    }

    public ImageDownloader error(int resId) {
        mDrawableError = getDrawable(resId);
        return this;
    }

    public ImageDownloader error(Drawable drawable) {
        mDrawableError = drawable;
        return this;
    }

    public ImageDownloader setOnDownloadCompletedListener(OnDownloadCompletedListener onDownloadCompletedListener) {
        this.mOnDownloadCompletedListener = onDownloadCompletedListener;
        return this;
    }

    public void into(ImageView imageView) {
        if (imageView == null) {
            throw new RuntimeException("ImageView can't be null");
        }
        this.mImageView = imageView;
        if (mDrawablePlaceholder != null) {
            mImageView.setImageDrawable(mDrawablePlaceholder);
        }

        if (mDrawableImage != null) {
            mImageView.setImageDrawable(mDrawableImage);
        } else if (mBitmap != null) {
            mImageView.setImageBitmap(mBitmap);
        } else {
            loadBitmap();
        }
    }

    private Drawable getDrawable(int resId) {
        return ContextCompat.getDrawable(getContext(), resId);
    }

    private void setImageBitmap() {
        if (cacheEnabled) {
            addBitmapToMemoryCache(getUrl(), mBitmap);
        }
        if (mImageView != null) {
            mImageView.setImageBitmap(mBitmap);
        }
    }

    @Override

    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull DownloadResult downloadResult) {
        super.onNext(downloadResult);
        String filePath = downloadResult.getFile().getPath();
        mBitmap = BitmapFactory.decodeFile(filePath);
        setImageBitmap();
    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (mDrawableError != null) {
            Log.e("ImageDownloader", "Error:" + e.getMessage());
            if (mImageView != null) {
                mImageView.setImageDrawable(mDrawableError);
            }
        }
    }

    @Override
    public void onComplete() {
        if (mOnDownloadCompletedListener != null) {
            mOnDownloadCompletedListener.onComplete(mBitmap, getDuration(), cachedBitmap);
        }
    }

    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public static Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }


    public void loadBitmap() {
        if (!isUrlValid()) {
            return;
        }
        final Bitmap bitmap = getBitmapFromMemCache(getUrl());
        if (bitmap != null) {
            mBitmap = bitmap;
            cachedBitmap = true;
            setImageBitmap();
            onComplete();
        } else {
            startDownloading();
        }
    }


    public interface OnDownloadCompletedListener {
        void onComplete(Bitmap bitmap, long duration, boolean cached);
    }
}
