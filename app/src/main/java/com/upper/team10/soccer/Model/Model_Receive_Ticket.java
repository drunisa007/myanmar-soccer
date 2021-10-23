package com.upper.team10.soccer.Model;

/**
 * Created by USER on 11/15/2017.
 */

public  class Model_Receive_Ticket {
    private String sender_name;
    private String stdsection;
    private String stdname;
    private String stdadult;
    private String time;
    private String uid;
    private String name;
    private String email;
    private String matchname;
    private String matchaway;
    private String matchtype;
    private String homelogo;
    private String awaylogo;
    private String date;

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getStdsection() {
        return stdsection;
    }

    public void setStdsection(String stdsection) {
        this.stdsection = stdsection;
    }

    public String getStdname() {
        return stdname;
    }

    public void setStdname(String stdname) {
        this.stdname = stdname;
    }

    public String getStdadult() {
        return stdadult;
    }

    public void setStdadult(String stdadult) {
        this.stdadult = stdadult;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

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

    public String getMatchname() {
        return matchname;
    }

    public void setMatchname(String matchname) {
        this.matchname = matchname;
    }

    public String getMatchaway() {
        return matchaway;
    }

    public void setMatchaway(String matchaway) {
        this.matchaway = matchaway;
    }

    public String getMatchtype() {
        return matchtype;
    }

    public void setMatchtype(String matchtype) {
        this.matchtype = matchtype;
    }

    public String getHomelogo() {
        return homelogo;
    }

    public void setHomelogo(String homelogo) {
        this.homelogo = homelogo;
    }

    public String getAwaylogo() {
        return awaylogo;
    }

    public void setAwaylogo(String awaylogo) {
        this.awaylogo = awaylogo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public Model_Receive_Ticket(String sender_name, String stdsection, String stdname, String stdadult, String time, String uid, String name, String email, String matchname, String matchaway, String matchtype, String homelogo, String awaylogo, String date) {
        this.sender_name = sender_name;
        this.stdsection = stdsection;
        this.stdname = stdname;
        this.stdadult = stdadult;
        this.time = time;
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.matchname = matchname;
        this.matchaway = matchaway;
        this.matchtype = matchtype;
        this.homelogo = homelogo;
        this.awaylogo = awaylogo;
        this.date = date;
    }

    public Model_Receive_Ticket(){

    }









}
