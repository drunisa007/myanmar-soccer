package com.upper.team10.soccer.viewpagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aung Thu on 10/16/2017.
 */

public class MyPageoneviewpagerAdapter extends FragmentPagerAdapter {
    private List<String> listTitle = new ArrayList<>();
    private List<Fragment> listfragment = new ArrayList<>();

    public MyPageoneviewpagerAdapter(FragmentManager fm, List<String> listTitle, List<Fragment> listfragment) {
        super(fm);
        this.listTitle = listTitle;
        this.listfragment = listfragment;
    }

    @Override
    public Fragment getItem(int position) {
        return listfragment.get(position);
    }

    @Override
    public int getCount() {
        return listfragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listTitle.get(position);
    }
}