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

    private int mCurrentPhotosSize;

    public GetPhotosService(Context context, int currentPhotosSize) {
        super(context);
        mCurrentPhotosSize = currentPhotosSize;
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
        ArrayList<Photo> outputArrayList = getSubArray(arrayList);
        notifyObserversNext(outputArrayList);
        notifyObserversComplete();
    }

    /**
     * Simulation for pagination
     *
     * @param arrayList
     * @return
     */
    private ArrayList<Photo> getSubArray(ArrayList<Photo> arrayList) {
        int wholePhotosSize = arrayList.size();
        if (arrayList.isEmpty() || wholePhotosSize <= mCurrentPhotosSize) {
            return new ArrayList<>();
        }

        int currentChunks = mCurrentPhotosSize / Constants.PHOTOS_LOAD_MORE_ITEMS_SIZE;

        int chunks = wholePhotosSize / Constants.PHOTOS_LOAD_MORE_ITEMS_SIZE;
        int remaining = wholePhotosSize % Constants.PHOTOS_LOAD_MORE_ITEMS_SIZE;

        int startIndex = mCurrentPhotosSize;
        int endIndex;
        if (currentChunks < chunks) {
            endIndex = startIndex + Constants.PHOTOS_LOAD_MORE_ITEMS_SIZE;
        } else {
            endIndex = startIndex + remaining;
        }
        return new ArrayList<>(arrayList.subList(startIndex, endIndex));
    }
}
