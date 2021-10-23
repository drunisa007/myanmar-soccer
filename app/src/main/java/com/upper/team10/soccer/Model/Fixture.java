package com.upper.team10.soccer.Model;

/**
 * Created by Arun on 10/18/2017.
 */

public class Fixture {
    private String pos;
    private String team;
    private String p;
    private String w;
    private String d;
    private String l;
    private String goal;
    private String pts;
    public Fixture() {

    }

    public Fixture(String pos, String team, String p, String w, String d, String l, String goal, String pts) {
        this.pos = pos;
        this.team = team;
        this.p = p;
        this.w = w;
        this.d = d;
        this.l = l;
        this.goal = goal;
        this.pts = pts;
    }

    public String getPos() {
        return pos;
    }

    public String getTeam() {
        return team;
    }

    public String getP() {
        return p;
    }

    public String getW() {
        return w;
    }

    public String getD() {
        return d;
    }

    public String getL() {
        return l;
    }

    public String getGoal() {
        return goal;
    }

    public String getPts() {
        return pts;
    }




}
