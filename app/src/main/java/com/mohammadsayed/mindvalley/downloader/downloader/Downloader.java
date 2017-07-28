package com.mohammadsayed.mindvalley.downloader.downloader;

import android.content.Context;

import com.mohammadsayed.mindvalley.downloader.data.DownloadResult;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mohammad on 7/27/17.
 */

public abstract class Downloader implements Observer<DownloadResult> {

    private Context mContext;
    private FileDownloader mFileDownloader;
    private String mUrl;
    private DownloadResult mDownloadResult;

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
        mFileDownloader = new FileDownloader(getContext(), mUrl);
        mFileDownloader.getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    @Override
    public void onNext(@NonNull DownloadResult downloadResult) {
        mDownloadResult = downloadResult;
    }

    public long getDuration() {
        if (mDownloadResult != null) {
            return mDownloadResult.getDownloadingDuration();
        }
        return 0;
    }
}
