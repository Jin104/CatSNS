package com.jin.catsns.login;

/**
 * Created by rakha on 2017-10-29.
 */

public class User {

    private String uid;
    private String id;
    private String name;
    private String email;
    private String imageUrl;

    public User(){}

    public User(String uid, String id, String name, String email, String imageUrl) {
        this.uid = uid;
        this.id = id;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

//    private String uid;
//    private String name;
//    private String email;
//    private String profileImgUrl;
//
//    public String getUid() {
//        return uid;
//    }
//
//    public void setUid(String uid) {
//        this.uid = uid;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getImgUrl() {
//        return profileImgUrl;
//    }
//
//    public void setImgUrl(String imgUrl) {
//        this.profileImgUrl = imgUrl;
//    }



}
