package com.jin.catsns.comment;

import java.util.Date;

/**
 * Created by Jin on 2017-10-30.
 */

public class Comment {

    private String username, userid, desc, image;
    private Date date;

    public Comment(){

    }

    public Comment(String username, String userid, String desc, String image){
        this.username=username;
        this.userid=userid;
        this.desc=desc;
        this.image=image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
