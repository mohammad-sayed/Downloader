package com.mohammadsayed.mindvalley.downloader.bases;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by mohammad on 7/29/17.
 */

public abstract class BaseFragment<T> extends Fragment {

    private T mController;

    protected abstract T createController();

    protected T getController() {
        return mController;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mController = createController();
    }
}
