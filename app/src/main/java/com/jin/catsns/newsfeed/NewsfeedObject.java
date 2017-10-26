package com.jin.catsns.newsfeed;

public class NewsfeedObject {

    private String postImage;
    private String postDate;
    private String postAuthor;

    public NewsfeedObject(String postImage, String postAuthor, String postDate) {
        this.postImage = postImage;
        this.postAuthor = postAuthor;
        this.postDate = postDate;
    }

    public String getPostImage() {
        return postImage;
    }

    public String getPostAuthor() {
        return postAuthor;
    }

    public String getPostTitle() {
        return postDate;
    }
}
