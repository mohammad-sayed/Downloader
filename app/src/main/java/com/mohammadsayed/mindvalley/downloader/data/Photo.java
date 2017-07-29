package com.mohammadsayed.mindvalley.downloader.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mohammad on 7/29/17.
 */

public class Photo implements Parcelable {

    private String id;
    private long likes;
    private User user;
    @SerializedName("created_at")
    private String creationDate;
    private PhotoUrls urls;

    public Photo() {
    }

    public Photo(Parcel in) {
        setId(in.readString());
        setLikes(in.readInt());
        setUser(new User(in));
        setCreationDate(in.readString());
        setUrls(new PhotoUrls(in));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getId());
        dest.writeLong(getLikes());
        if (getUser() != null) {
            getUser().writeToParcel(dest, flags);
        }
        dest.writeString(getCreationDate());
        if (getUrls() != null) {
            getUrls().writeToParcel(dest, flags);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Photo> CREATOR
            = new Creator<Photo>() {
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public PhotoUrls getUrls() {
        return urls;
    }

    public void setUrls(PhotoUrls urls) {
        this.urls = urls;
    }
}
