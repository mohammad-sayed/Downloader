package com.mohammadsayed.mindvalley.downloader.bases;

import android.content.Context;

/**
 * Created by mohammad on 7/29/17.
 */

public abstract class BaseController {

    private Context mContext;

    public BaseController(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return mContext;
    }
}
