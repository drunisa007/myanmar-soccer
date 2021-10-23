package com.upper.team10.soccer.Model;

/**
 * Created by USER on 10/27/2017.
 */

public class News {
    private String title,date,image,body1,body2,body3;

    public News(){

    }
    public News(String title, String date, String image, String body1, String body2, String body3) {
        this.title = title;
        this.date = date;
        this.image = image;
        this.body1 = body1;
        this.body2 = body2;
        this.body3 = body3;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBody1() {
        return body1;
    }

    public void setBody1(String body1) {
        this.body1 = body1;
    }

    public String getBody2() {
        return body2;
    }

    public void setBody2(String body2) {
        this.body2 = body2;
    }

    public String getBody3() {
        return body3;
    }

    public void setBody3(String body3) {
        this.body3 = body3;
    }







}
