package com.mohammadsayed.mindvalley.downloader.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mohammad on 7/29/17.
 */

public class PhotoUrls implements Parcelable {

    private String raw;
    private String full;
    private String regular;
    private String small;
    private String thumb;

    public PhotoUrls() {
    }

    public PhotoUrls(Parcel in) {
        setRaw(in.readString());
        setFull(in.readString());
        setRegular(in.readString());
        setSmall(in.readString());
        setThumb(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getRaw());
        dest.writeString(getFull());
        dest.writeString(getRegular());
        dest.writeString(getSmall());
        dest.writeString(getThumb());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PhotoUrls> CREATOR
            = new Creator<PhotoUrls>() {
        public PhotoUrls createFromParcel(Parcel in) {
            return new PhotoUrls(in);
        }

        public PhotoUrls[] newArray(int size) {
            return new PhotoUrls[size];
        }
    };

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
