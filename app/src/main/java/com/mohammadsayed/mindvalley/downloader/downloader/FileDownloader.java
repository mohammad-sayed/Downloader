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
import io.reactivex.Observer;

/**
 * Created by mohammad on 7/27/17.
 */

public class FileDownloader extends Observable<File> {

    private Context mContext;
    private ArrayList<Observer<? super File>> mObservers;

    public FileDownloader(Context context) {
        this.mContext = context;
        mObservers = new ArrayList<>();
    }


    @Override
    protected void subscribeActual(Observer<? super File> observer) {
        mObservers.add(observer);
    }

    private void downloadFile(String urlString) {
        int count;
        try {
            URL url = new URL(urlString);
            URLConnection conection = url.openConnection();
            conection.connect();
            // getting file length
            int lenghtOfFile = conection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write file
            //mContext.getCacheDir();
            File outputFile = getFile(urlString);
            if (outputFile != null) {
                OutputStream output = new FileOutputStream(outputFile);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    // writing data to file
                    output.write(data, 0, count);
                }
                // flushing output
                output.flush();
                // closing streams
                output.close();
                notifySubscribersOnNext(outputFile);
            }
            input.close();
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
            notifySubscribersOnError(e);
        }
    }

    private File getFile(String urlString) {
        File directory = mContext.getFilesDir();
        /*if (mContext.getExternalCacheDir() != null && mContext.getExternalCacheDir().exists()) {
            directory = mContext.getExternalCacheDir();
        } else {
            directory = mContext.getCacheDir();
        }
        mContext.getExternalCacheDir().exists()*/
        String fileName = urlString.substring(urlString.lastIndexOf("/") + 1);
        File outputFile = new File(directory, fileName);
        if (!outputFile.mkdirs()) {
            Log.e("Error: ", "Directory not created");
            notifySubscribersOnError(new RuntimeException("Directory not created"));
            return null;
        }
        return outputFile;
    }


    private void notifySubscribersOnNext(File file) {
        for (Observer observer : mObservers) {
            observer.onNext(file);
            observer.onComplete();
        }
    }

    private void notifySubscribersOnError(Throwable e) {
        for (Observer observer : mObservers) {
            observer.onError(e);
        }
    }
}
