package com.mohammadsayed.mindvalley.downloader.downloader;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.mohammadsayed.mindvalley.downloader.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mohammad on 7/28/17.
 */

public class TextDownloader extends Downloader {

    private String mUrl;
    private FileDownloader mFileDownloader;
    private TextView mTextView;
    private String mText;

    private TextDownloader(Context context) {
        super(context);
    }


    public static TextDownloader with(Context context) {
        return new TextDownloader(context);
    }

    public TextDownloader fromUrl(String url) {
        if (url == null) {
            throw new RuntimeException("Url can't be null");
        }
        if (url.trim().equals("")) {
            throw new RuntimeException("Url can't be empty");
        }
        this.mUrl = url;
        return this;
    }

    public TextDownloader fromText(int resId) {
        this.mText = getText(resId);
        return this;
    }

    public TextDownloader fromText(String text) {
        this.mText = text;
        return this;
    }

    public void into(TextView textView) {
        if (textView == null) {
            throw new RuntimeException("TextView can't be null");
        }
        this.mTextView = textView;

        if (mUrl != null) {
            mFileDownloader = new FileDownloader(getContext(), mUrl);
            mFileDownloader.getObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this);
        }
    }

    private String getText(int resId) {
        return getContext().getString(resId);
    }

    @Override

    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull File file) {
        String content = getFileTextContent(file);
        mTextView.setText(content);
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

    @Override
    public void onError(@NonNull Throwable e) {
        String errorMessage = e.getMessage();
        if (TextUtils.isEmpty(errorMessage) || !TextUtils.isEmpty(errorMessage.trim())) {
            errorMessage = getContext().getString(R.string.error_json_download);
        }
        mTextView.setText(errorMessage);
    }

    @Override
    public void onComplete() {

    }
}
