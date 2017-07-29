package com.mohammadsayed.mindvalley.downloader.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mohammad on 7/29/17.
 */

public class User {

    private String userName;
    private String name;

    @SerializedName("profile_image")
    private ProfileImage profileImage;

    private UserLinks links;

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
