package com.mohammadsayed.mindvalley.downloader.bases;

import android.content.Context;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by mohammad on 7/29/17.
 */

public abstract class BaseService<T> implements ObservableOnSubscribe<T> {

    private Context mContext;
    private Observable<T> mObservable;
    private ArrayList<ObservableEmitter<T>> mObservableEmitters;


    public BaseService(Context context) {
        this.mContext = context;
        mObservableEmitters = new ArrayList<>();
        mObservable = Observable.create(this);
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
        mObservableEmitters.add(e);
    }

    public Observable<T> getObservable() {
        return mObservable;
    }

    public void clearObservers() {
        mObservableEmitters.clear();
    }

    public void notifyObserversNext(T object) {
        for (ObservableEmitter<T> e : mObservableEmitters) {
            e.onNext(object);
        }
    }

    public void notifyObserversComplete() {
        for (ObservableEmitter<T> e : mObservableEmitters) {
            e.onComplete();
        }
    }

    public void notifyObserversError(Throwable error) {
        for (ObservableEmitter<T> e : mObservableEmitters) {
            e.onError(error);
        }
    }
}
