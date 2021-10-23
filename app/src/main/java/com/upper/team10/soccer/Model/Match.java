package com.upper.team10.soccer.Model;

/**
 * Created by AUNG THU on 10/13/2017.
 */

public class Match{
    private String matchhomename,matchawayname,matchtime,matchgoal,matchtype, matchhomelogo,matchawaylogo;

    public  Match(){

    }

    public Match(String matchhomename, String matchawayname, String matchtime, String matchgoal, String matchtype, String matchhomelogo, String matchawaylogo) {
        this.matchhomename = matchhomename;
        this.matchawayname = matchawayname;
        this.matchtime = matchtime;
        this.matchgoal = matchgoal;
        this.matchtype = matchtype;
        this.matchhomelogo = matchhomelogo ;
        this.matchawaylogo = matchawaylogo;
    }

    public String getMatchhomelogo() {
        return matchhomelogo;
    }

    public String getMatchawaylogo() {
        return matchawaylogo;
    }




    public String getMatchhomename() {
        return matchhomename;
    }

    public String getMatchawayname() {
        return matchawayname;
    }

    public String getMatchtime() {
        return matchtime;
    }

    public String getMatchgoal() {
        return matchgoal;
    }

    public String getMatchtype() {
        return matchtype;
    }



}
