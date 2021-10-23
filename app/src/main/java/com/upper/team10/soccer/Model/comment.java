package com.upper.team10.soccer.Model;

/**
 * Created by USER on 11/7/2017.
 */

public class comment {
    private String url,name,comment;
    public comment(){

    }
    public comment(String url, String name, String comment) {
        this.url = url;
        this.name = name;
        this.comment = comment;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
