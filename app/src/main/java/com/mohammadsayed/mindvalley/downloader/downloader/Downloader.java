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
    private boolean cacheEnabled = true;
    private boolean getCached = true;

    public Downloader(Context context) {
        this.mContext = context;
    }

    public Downloader fromUrl(String url) {
        if (!isUrlValid(url)) {
            return null;
        }
        this.mUrl = url;
        return this;
    }

    protected boolean isUrlValid(String url) {
        if (url == null) {
            throw new RuntimeException("Url can't be null");
        }
        if (url.trim().equals("")) {
            throw new RuntimeException("Url can't be empty");
        }
        return true;
    }

    protected boolean isUrlValid() {
        return isUrlValid(mUrl);
    }

    public Context getContext() {
        return mContext;
    }

    public void startDownloading() {
        if (!isUrlValid(mUrl)) {
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

    public String getUrl() {
        return mUrl;
    }

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    /**
     * Set if downloaded file will be cached or not
     *
     * @param cacheEnabled true for caching; false for not caching
     * @return
     */
    public Downloader setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
        return this;
    }

    public boolean isGetCached() {
        return getCached;
    }

    /**
     * Set if file will be retrieved from cache or not
     *
     * @param getCached
     */
    public Downloader setGetCached(boolean getCached) {
        this.getCached = getCached;
        return this;
    }

    public void cancel() {
        mFileDownloader.cancel(this);
    }
}
