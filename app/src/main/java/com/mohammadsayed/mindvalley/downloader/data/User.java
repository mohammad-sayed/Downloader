package com.mohammadsayed.mindvalley.downloader.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mohammad on 7/29/17.
 */

public class User implements Parcelable {

    private String userName;
    private String name;

    @SerializedName("profile_image")
    private ProfileImage profileImage;

    private UserLinks links;

    public User() {
    }

    public User(Parcel in) {
        setUserName(in.readString());
        setName(in.readString());
        setProfileImage(new ProfileImage(in));
        setLinks(new UserLinks(in));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR
            = new Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getUserName());
        dest.writeString(getName());
        if (getLinks() != null) {
            getLinks().writeToParcel(dest, flags);
        }
        if (getProfileImage() != null) {
            getProfileImage().writeToParcel(dest, flags);
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProfileImage getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
    }

    public UserLinks getLinks() {
        return links;
    }

    public void setLinks(UserLinks links) {
        this.links = links;
    }
}
