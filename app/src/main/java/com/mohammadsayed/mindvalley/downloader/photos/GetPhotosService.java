package com.mohammadsayed.mindvalley.downloader.photos;

import android.content.Context;

import com.google.gson.Gson;
import com.mohammadsayed.mindvalley.downloader.Constants;
import com.mohammadsayed.mindvalley.downloader.bases.BaseService;
import com.mohammadsayed.mindvalley.downloader.data.Photo;
import com.mohammadsayed.mindvalley.downloader.downloader.TextDownloader;

import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.ObservableEmitter;
import io.reactivex.annotations.NonNull;

/**
 * Created by mohammad on 7/29/17.
 */

public class GetPhotosService extends BaseService<ArrayList<Photo>>
        implements TextDownloader.OnDownloadCompletedListener {

    public GetPhotosService(Context context) {
        super(context);
    }

    private void getPhotos() {
        TextDownloader.with(getContext())
                .fromUrl(Constants.PHOTOS_URL)
                .setOnDownloadCompletedListener(this)
                .startDownloading();
    }

    @Override
    public void subscribe(@NonNull ObservableEmitter<ArrayList<Photo>> e) throws Exception {
        super.subscribe(e);
        getPhotos();
    }

    @Override
    public void onComplete(String text, long duration) {
        Gson gson = new Gson();
        Photo[] photosArray = gson.fromJson(text, Photo[].class);
        ArrayList<Photo> arrayList = new ArrayList<>(Arrays.asList(photosArray));
        notifyObserversNext(arrayList);
        notifyObserversComplete();
    }
}
