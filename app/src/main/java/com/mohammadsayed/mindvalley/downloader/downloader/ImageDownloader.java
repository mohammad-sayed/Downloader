package com.mohammadsayed.mindvalley.downloader.downloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;

import com.mohammadsayed.mindvalley.downloader.cachemanagers.BitmapCacheManager;
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
    private BitmapCacheManager mBitmapCacheManager;

    private boolean cachedBitmap = false;


    private ImageDownloader(Context context) {
        super(context);
        mBitmapCacheManager = BitmapCacheManager.getInstance();
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

    @Override
    public ImageDownloader setCacheEnabled(boolean cacheEnabled) {
        return (ImageDownloader) super.setCacheEnabled(cacheEnabled);
    }

    @Override
    public ImageDownloader setGetCached(boolean getCached) {
        return (ImageDownloader) super.setGetCached(getCached);
    }

    @Override
    public void cancel() {
        super.cancel();
        if (mDrawableError != null) {
            mImageView.setImageDrawable(mDrawableError);
        }
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
        if (isCacheEnabled()) {
            mBitmapCacheManager.addBitmapToMemoryCache(getUrl(), mBitmap);
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
        clearFile();
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


    public void loadBitmap() {
        if (!isUrlValid()) {
            return;
        }
        Bitmap bitmap = null;
        if (isGetCached()) {
            bitmap = mBitmapCacheManager.getBitmapFromMemCache(getUrl());
        }
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
