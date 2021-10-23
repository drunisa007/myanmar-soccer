package com.upper.team10.soccer.Model;

/**
 * Created by Arun on 10/24/2017.
 */

public class TicketModel {

    private String stdsection,stdname,stdadult,time,uid,name,email,matchname,matchaway,matchtype,homelogo,awaylogo,date;
    public TicketModel(){

    }
    public TicketModel(String stdsection, String stdname, String stdadult, String time, String uid, String name, String email, String matchname, String matchaway, String matchtype, String homelogo, String awaylogo,String date) {
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
        this.date=date;
}

    public String getStdsection() {
        return stdsection;
    }

    public String getStdname() {
        return stdname;
    }

    public String getStdadult() {
        return stdadult;
    }

    public String getTime() {
        return time;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMatchname() {
        return matchname;
    }

    public String getMatchaway() {
        return matchaway;
    }

    public String getMatchtype() {
        return matchtype;
    }

    public String getHomelogo() {
        return homelogo;
    }

    public String getAwaylogo() {
        return awaylogo;
    }
    public String getDate(){
        return date;
    }




}
