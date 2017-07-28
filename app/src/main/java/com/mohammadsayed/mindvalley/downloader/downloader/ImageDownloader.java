package com.mohammadsayed.mindvalley.downloader.downloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mohammad on 7/28/17.
 */

public class ImageDownloader extends Downloader {

    private Drawable mDrawablePlaceholder;
    private Drawable mDrawableError;
    private ImageView mImageView;
    private String mUrl;
    private Drawable mDrawableImage;
    private FileDownloader mFileDownloader;

    private ImageDownloader(Context context) {
        super(context);
    }


    public static ImageDownloader with(Context context) {
        return new ImageDownloader(context);
    }

    public ImageDownloader from(String url) {
        if (url == null) {
            throw new RuntimeException("ImageView Url can't be null");
        }
        if (url.trim().equals("")) {
            throw new RuntimeException("ImageView Url can't be empty");
        }
        this.mUrl = url;
        return this;
    }

    public ImageDownloader from(int resId) {
        mDrawableImage = getDrawable(resId);
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
        } else if (mUrl != null) {
            mFileDownloader = new FileDownloader(getContext(), mUrl);
            mFileDownloader.getObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this);
        }
    }

    private Drawable getDrawable(int resId) {
        return ContextCompat.getDrawable(getContext(), resId);
    }

    @Override

    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull File file) {
        String filePath = file.getPath();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        mImageView.setImageBitmap(bitmap);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (mDrawableError != null) {
            mImageView.setImageDrawable(mDrawableError);
        }
    }

    @Override
    public void onComplete() {

    }
}
