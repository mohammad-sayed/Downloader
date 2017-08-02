package com.mohammadsayed.mindvalley.downloader.photos;

import android.content.Context;

import com.mohammadsayed.mindvalley.downloader.bases.BaseController;
import com.mohammadsayed.mindvalley.downloader.bases.BaseObserver;
import com.mohammadsayed.mindvalley.downloader.data.Photo;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mohammad on 7/29/17.
 */

public class PhotosController extends BaseController {

    public PhotosController(Context context) {
        super(context);
    }

    public void getPhotos(final Observer<ArrayList<Photo>> observer, int currentPhotosSize) {
        GetPhotosService getPhotosService = new GetPhotosService(getContext(), currentPhotosSize);
        getPhotosService.getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<ArrayList<Photo>>() {
                    @Override
                    public void onNext(@NonNull ArrayList<Photo> photos) {
                        observer.onNext(photos);
                    }
                });
    }
}
