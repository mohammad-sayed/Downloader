package com.mohammadsayed.mindvalley.downloader.downloader;

import android.content.Context;

import java.io.File;

import io.reactivex.Observer;

/**
 * Created by mohammad on 7/27/17.
 */

public abstract class Downloader implements Observer<File> {

    private Context mContext;

    public Downloader(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return mContext;
    }
}
