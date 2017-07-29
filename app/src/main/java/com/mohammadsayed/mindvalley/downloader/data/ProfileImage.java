package com.mohammadsayed.mindvalley.downloader.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mohammad on 7/29/17.
 */

public class ProfileImage implements Parcelable {

    private String small;
    private String medium;
    private String large;

    public ProfileImage() {
    }

    public ProfileImage(Parcel in) {
        setSmall(in.readString());
        setMedium(in.readString());
        setLarge(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getSmall());
        dest.writeString(getMedium());
        dest.writeString(getLarge());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProfileImage> CREATOR
            = new Creator<ProfileImage>() {
        public ProfileImage createFromParcel(Parcel in) {
            return new ProfileImage(in);
        }

        public ProfileImage[] newArray(int size) {
            return new ProfileImage[size];
        }
    };


    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }
}
