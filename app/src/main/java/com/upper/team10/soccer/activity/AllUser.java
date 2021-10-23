package com.upper.team10.soccer.activity;

/**
 * Created by USER on 11/11/2017.
 */

public  class AllUser {

    public  AllUser(){

    }
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String image;

    public AllUser(String name, String image) {
        this.name = name;
        this.image = image;
    }
}
