package com.mohammadsayed.mindvalley.downloader.data;

import java.io.File;

/**
 * Created by mohammad on 7/28/17.
 */

public class DownloadResult {

    private File mFile;
    private long downloadingDuration;

    public File getFile() {
        return mFile;
    }

    public void setFile(File file) {
        mFile = file;
    }

    public long getDownloadingDuration() {
        return downloadingDuration;
    }

    public void setDownloadingDuration(long downloadingDuration) {
        this.downloadingDuration = downloadingDuration;
    }
}
