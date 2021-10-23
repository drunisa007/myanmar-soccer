package com.upper.team10.soccer.Model;

/**
 * Created by USER on 10/20/2017.
 */

public class Video {
    private String url,highlight,week,img;

    public Video(){

    }



    public Video(String url, String highlight, String week,String img) {
        this.url = url;
        this.highlight = highlight;
        this.week = week;
        this.img=img;
    }

    public String getUrl() {
        return url;
    }

    public String getHighlight() {
        return highlight;
    }

    public String getWeek() {
        return week;
    }
    public String getImg(){
        return img;
    }
}
