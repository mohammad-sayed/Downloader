package com.mohammadsayed.mindvalley.downloader.downloader;

import android.content.Context;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mohammad on 7/27/17.
 */

public abstract class Downloader implements Observer<File> {

    private Context mContext;
    private long startTime;
    private long endTime;
    private FileDownloader mFileDownloader;
    private String mUrl;

    public Downloader(Context context) {
        this.mContext = context;
    }

    public Downloader fromUrl(String url) {
        if (url == null) {
            throw new RuntimeException("Url can't be null");
        }
        if (url.trim().equals("")) {
            throw new RuntimeException("Url can't be empty");
        }
        this.mUrl = url;
        return this;
    }

    public Context getContext() {
        return mContext;
    }

    public void startDownloading() {
        if (mUrl == null) {
            return;
        }
        startTime = System.currentTimeMillis();
        mFileDownloader = new FileDownloader(getContext(), mUrl);
        mFileDownloader.getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    @Override
    public void onComplete() {
        endTime = System.currentTimeMillis();
    }

    public long getDuration() {
        if (startTime == 0 || endTime == 0) {
            return 0;
        }
        return endTime - startTime;
    }
}
