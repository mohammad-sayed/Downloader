package com.mohammadsayed.mindvalley.downloader.downloader;

import android.content.Context;
import android.util.Log;

import com.mohammadsayed.mindvalley.downloader.data.DownloadResult;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;

/**
 * Created by mohammad on 7/27/17.
 */

public class FileDownloader implements ObservableOnSubscribe<DownloadResult> {

    private Context mContext;
    //private ArrayList<ObservableEmitter<DownloadResult>> mObservers;
    ObservableEmitter<DownloadResult> mObserver;
    private Observable<DownloadResult> mObservable;
    private String mUrl;
    private long startTime;
    private long endTime;
    private boolean continueDownloading = true;

    public FileDownloader(Context context, String url) {
        this.mContext = context;
        this.mUrl = url;
        //mObservers = new ArrayList<>();
        mObservable = Observable.create(this);
    }

    @Override
    public void subscribe(@NonNull ObservableEmitter<DownloadResult> e) throws Exception {
        mObserver = e;
        //mObservers.add(e);
        downloadFile(mUrl);
    }

    public void downloadFile(String urlString) {
        int count;
        try {
            startTime = System.currentTimeMillis();
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

                Log.i("FileDownloader", "DownloadResult Started");

                while (continueDownloading && (count = input.read(data)) != -1) {
                    // writing data to file
                    output.write(data, 0, count);
                }
                endTime = System.currentTimeMillis();
                Log.i("FileDownloader", "DownloadResult Finished");
                // flushing output
                output.flush();
                // closing streams
                output.close();

                if (continueDownloading) {
                    File outputFile = new File(outputFilePath);
                    DownloadResult downloadResult = new DownloadResult();
                    downloadResult.setFile(outputFile);
                    downloadResult.setDownloadingDuration(getDownloadDuration());
                    notifySubscribersOnNext(downloadResult);
                }
            }
            input.close();
        } catch (
                Exception e)

        {
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


    private void notifySubscribersOnNext(DownloadResult downloadResult) {
        /*for (ObservableEmitter<DownloadResult> observer : mObservers) {
            observer.onNext(downloadResult);
            observer.onComplete();
        }*/
        if (mObserver != null) {
            mObserver.onNext(downloadResult);
            mObserver.onComplete();
        }
    }

    private void notifySubscribersOnError(Throwable e) {
        /*for (ObservableEmitter<DownloadResult> observer : mObservers) {
            observer.onError(e);
        }*/
        if (mObserver != null) {
            mObserver.onError(e);
        }
    }

    private long getDownloadDuration() {
        if (startTime == 0 || endTime == 0) {
            return 0;
        }
        return endTime - startTime;
    }

    public Observable<DownloadResult> getObservable() {
        return mObservable;
    }

    public void cancel(Observer<DownloadResult> observer) {
        continueDownloading = false;
        mObserver = null;
        /*int i;
        for (i = 0; i < mObservers.size(); i++) {
            if (mObservers.get(i).hashCode() == observer.hashCode()) {
                break;
            }
        }
        if (i < mObservers.size()) {
            mObservers.remove(i);
        }

        if (mObservers.isEmpty()) {
            continueDownloading = false;
        }*/
    }
}
