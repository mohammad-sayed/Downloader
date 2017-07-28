package com.mohammadsayed.mindvalley.downloader.downloader;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by mohammad on 7/27/17.
 */

public class FileDownloader implements ObservableOnSubscribe<File> {

    private Context mContext;
    private ArrayList<ObservableEmitter<File>> mObservers;
    private Observable<File> mObservable;
    private String mUrl;

    public FileDownloader(Context context, String url) {
        this.mContext = context;
        this.mUrl = url;
        mObservers = new ArrayList<>();
        mObservable = Observable.create(this);
    }

    @Override
    public void subscribe(@NonNull ObservableEmitter<File> e) throws Exception {
        mObservers.add(e);
        downloadFile(mUrl);
    }

    public void downloadFile(String urlString) {
        int count;
        try {
            URL url = new URL(urlString);
            URLConnection conection = url.openConnection();
            conection.connect();
            // getting file length
            //int lenghtOfFile = conection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write file
            //mContext.getCacheDir();
            String outputFilePath = getFilePath(urlString);
            if (outputFilePath != null) {
                OutputStream output = new FileOutputStream(outputFilePath);

                byte data[] = new byte[1024];

                long total = 0;

                Log.i("FileDownloader", "Download Started");

                while ((count = input.read(data)) != -1) {
                    // writing data to file
                    output.write(data, 0, count);
                }
                Log.i("FileDownloader", "Download Finished");
                // flushing output
                output.flush();
                // closing streams
                output.close();
                File outputFile = new File(outputFilePath);
                notifySubscribersOnNext(outputFile);
            }
            input.close();
        } catch (Exception e) {
            Log.e("FileDownloader", "Error");
            notifySubscribersOnError(e);
        }
    }

    private String getFilePath(String urlString) {
        File directory = mContext.getFilesDir();
        /*if (mContext.getExternalCacheDir() != null && mContext.getExternalCacheDir().exists()) {
            directory = mContext.getExternalCacheDir();
        } else {
            directory = mContext.getCacheDir();
        }
        mContext.getExternalCacheDir().exists()*/
        if (!directory.exists() && !directory.mkdirs()) {
            Log.e("Error: ", "Directory not created");
            notifySubscribersOnError(new RuntimeException("Directory not created"));
            return null;
        }
        return directory.getPath() + "/" + urlString.substring(urlString.lastIndexOf("/") + 1);
    }


    private void notifySubscribersOnNext(File file) {
        for (ObservableEmitter<File> observer : mObservers) {
            observer.onNext(file);
            observer.onComplete();
        }
    }

    private void notifySubscribersOnError(Throwable e) {
        for (ObservableEmitter<File> observer : mObservers) {
            observer.onError(e);
        }
    }

    public Observable<File> getObservable() {
        return mObservable;
    }
}
