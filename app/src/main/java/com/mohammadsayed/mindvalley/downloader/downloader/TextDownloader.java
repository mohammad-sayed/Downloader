package com.mohammadsayed.mindvalley.downloader.downloader;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.mohammadsayed.mindvalley.downloader.R;
import com.mohammadsayed.mindvalley.downloader.cachemanagers.TextCacheManager;
import com.mohammadsayed.mindvalley.downloader.data.DownloadResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by mohammad on 7/28/17.
 */

public class TextDownloader extends Downloader {

    private TextView mTextView;
    private String mText;
    private OnDownloadCompletedListener mOnDownloadCompletedListener;
    private TextCacheManager mTextCacheManager;
    private boolean mCachedString = false;

    private TextDownloader(Context context) {
        super(context);
        mTextCacheManager = TextCacheManager.getInstance();
    }


    public static TextDownloader with(Context context) {
        return new TextDownloader(context);
    }

    @Override
    public TextDownloader fromUrl(String url) {
        return (TextDownloader) super.fromUrl(url);
    }

    public TextDownloader fromText(int resId) {
        this.mText = getText(resId);
        return this;
    }

    public TextDownloader fromText(String text) {
        this.mText = text;
        return this;
    }

    @Override
    public TextDownloader setCacheEnabled(boolean cacheEnabled) {
        return (TextDownloader) super.setCacheEnabled(cacheEnabled);
    }

    @Override
    public TextDownloader setGetCached(boolean getCached) {
        return (TextDownloader) super.setGetCached(getCached);
    }


    public TextDownloader setOnDownloadCompletedListener(OnDownloadCompletedListener onDownloadCompletedListener) {
        this.mOnDownloadCompletedListener = onDownloadCompletedListener;
        return this;
    }

    public void into(TextView textView) {
        if (textView == null) {
            throw new RuntimeException("TextView can't be null");
        }
        this.mTextView = textView;

        if (mText != null) {
            mTextView.setText(mText);
        } else {
            loadText();
        }
    }

    private void loadText() {
        if (!isUrlValid()) {
            return;
        }
        String string = null;
        if (isGetCached()) {
            string = mTextCacheManager.getStringFromMemCache(getUrl());
        }
        if (string != null) {
            mText = string;
            mCachedString = true;
            setTextViewString();
            onComplete();
        } else {
            startDownloading();
        }
    }

    private void setTextViewString() {
        if (isCacheEnabled()) {
            mTextCacheManager.addStringToMemoryCache(getUrl(), mText);
        }
        if (mTextView != null) {
            mTextView.setText(mText);
        }
    }

    private String getText(int resId) {
        return getContext().getString(resId);
    }

    @Override

    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull DownloadResult downloadResult) {
        super.onNext(downloadResult);
        mText = getFileTextContent(downloadResult.getFile());
        clearFile();
        setTextViewString();
    }

    @Override
    public void onError(@NonNull Throwable e) {
        String errorMessage = e.getMessage();
        if (TextUtils.isEmpty(errorMessage) || !TextUtils.isEmpty(errorMessage.trim())) {
            errorMessage = getContext().getString(R.string.error_json_download);
        }
        if (mTextView != null) {
            mTextView.setText(errorMessage);
        }
    }

    @Override
    public void onComplete() {
        if (mOnDownloadCompletedListener != null) {
            mOnDownloadCompletedListener.onComplete(mText, getDuration(), mCachedString);
        }
    }


    private String getFileTextContent(File file) {
        //Read mText fromUrl file
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
            return text.toString();
        } catch (IOException e) {
            onError(e);
            //You'll need to add proper error handling here
        }
        return null;
    }

    public interface OnDownloadCompletedListener {
        void onComplete(String text, long duration, boolean cached);
    }
}
