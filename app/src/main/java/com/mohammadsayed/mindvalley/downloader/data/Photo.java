package com.mohammadsayed.mindvalley.downloader.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mohammad on 7/29/17.
 */

public class Photo {

    private String id;
    private User user;
    private int likes;

    @SerializedName("created_at")
    private String creationDate;

    private PhotoUrls urls;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
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
