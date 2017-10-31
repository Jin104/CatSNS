package com.jin.catsns.post;

import com.jin.catsns.login.User;

/**
 * Created by rakha on 2017-10-27.
 */

public class Post {

    private User user;
    private String postText;
    private String postImageUrl;
    private String postId;
    private long numLikes;
    private long numComments;
    private long timeCreated;

    public Post(){}

    public Post(User user, String postText, String postImageUrl, String postId, long numLikes, long numComments, long timeCreated) {
        this.user = user;
        this.postText = postText;
        this.postImageUrl = postImageUrl;
        this.postId = postId;
        this.numLikes = numLikes;
        this.numComments = numComments;
        this.timeCreated = timeCreated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public long getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(long numLikes) {
        this.numLikes = numLikes;
    }

    public long getNumComments() {
        return numComments;
    }

    public void setNumComments(long numComments) {
        this.numComments = numComments;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }


    //    private String title, desc, image, username;
//
//    public Post() {
//
//    }
//
//    public Post(String title, String desc, String image, String username) {
//        this.title = title;
//        this.desc = desc;
//        this.image = image;
//        this.username = username;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getDesc() {
//        return desc;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public void setDesc(String desc) {
//        this.desc = desc;
//    }
//
//    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }

}