package com.mohammadsayed.mindvalley.downloader.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mohammad on 7/29/17.
 */

public class UserLinks implements Parcelable {

    private String self;
    private String html;
    private String photos;
    private String likes;

    public UserLinks() {
    }

    public UserLinks(Parcel in) {
        setSelf(in.readString());
        setHtml(in.readString());
        setPhotos(in.readString());
        setLikes(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getSelf());
        dest.writeString(getHtml());
        dest.writeString(getPhotos());
        dest.writeString(getLikes());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserLinks> CREATOR
            = new Creator<UserLinks>() {
        public UserLinks createFromParcel(Parcel in) {
            return new UserLinks(in);
        }

        public UserLinks[] newArray(int size) {
            return new UserLinks[size];
        }
    };

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }
}
