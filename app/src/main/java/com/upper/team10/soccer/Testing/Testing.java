package com.upper.team10.soccer.Testing;


/**
 * Created by USER on 11/4/2017.
 */

public class Testing {
    private String name;


    public Testing(){

    }

    public Testing(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }



}
/*
recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
@Override
public void onScrolled(RecyclerView recyclerView, int dx, int dy){
        if (dy > 0 ||dy<0 && fab.isShown())
        fab.hide();
        }

@Override
public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

        if (newState == RecyclerView.SCROLL_STATE_IDLE){
        fab.show();
        }
        super.onScrollStateChanged(recyclerView, newState);
        }
        });*/
