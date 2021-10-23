package com.upper.team10.soccer.viewpagerAdapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by USER on 10/14/2017.
 */

public class CustomViewPager extends ViewPager {
    private boolean enabled;
    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled=true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(this.enabled){
        return super.onTouchEvent(ev);}
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(this.enabled) {
            return super.onInterceptTouchEvent(ev);
        }
        return false;

    }
    public void setPagingEnabled(boolean enabled){
        this.enabled=enabled;
    }
}
