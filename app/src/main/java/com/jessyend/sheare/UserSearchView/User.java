package com.jessyend.sheare.UserSearchView;


public class User {
    private String spotifyId;
    private String pictureUrl;
    private String userNum;

    public User(String spotifyId, String pictureUrl, String userNum) {
        this.spotifyId = spotifyId;
        this.pictureUrl = pictureUrl;
        this.userNum = userNum;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getUserNum() {
        return userNum;
    }
}
