package com.jin.catsns.login;

/**
 * Created by rakha on 2017-10-29.
 */

public class User {

    private String uid;
    private String name;
    private String email;
    private String profileImgUrl;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgUrl() {
        return profileImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.profileImgUrl = imgUrl;
    }



}
