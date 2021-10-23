package com.upper.team10.soccer.Model;

/**
 * Created by Aung Thu on 10/22/2017.
 */

public class Post {

    private String postgoal,posttext,postusername;

    private String imageone;
    private String imagetwo;
    private String comment;
    private String url;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String image;

    public Post(){

}
    public Post(String postgoal, String posttext, String postusername,String imageone,String imagetwo,String url,String comment,String image) {
        this.postgoal = postgoal;
        this.posttext = posttext;
        this.postusername = postusername;
        this.imageone=imageone;
        this.imagetwo=imagetwo;
        this.url=url;
        this.comment=comment;
        this.image=image;
    }
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUrl(){
    return url;
}
    public String getPostgoal() {
        return postgoal;
    }

    public String getPosttext() {
        return posttext;
    }
    public String getPostusername() {
        return postusername;
    }

    public String getImageone() {
        return imageone;
    }

    public String getImagetwo() {
        return imagetwo;
    }

}
