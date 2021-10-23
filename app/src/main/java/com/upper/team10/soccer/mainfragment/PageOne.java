package com.upper.team10.soccer.mainfragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.upper.team10.soccer.R;
import com.upper.team10.soccer.mainfragment.PageOneFragment.Comming;
import com.upper.team10.soccer.mainfragment.PageOneFragment.Finished;
import com.upper.team10.soccer.mainfragment.PageOneFragment.Today;
import com.upper.team10.soccer.viewpagerAdapter.MyPageoneviewpagerAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.upper.team10.soccer.activity.Main.mToolbar;


public class PageOne extends Fragment {
    private TabLayout mTablayout;
    private ViewPager mViewPager;

    public PageOne() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_page_one, container, false);
        mTablayout= (TabLayout) v.findViewById(R.id.pageonetablayout);
        mViewPager= (ViewPager) v.findViewById(R.id.pageoneviewpager);
        List<Fragment> listfragment=new ArrayList<>();
        List<String> listString=new ArrayList<>();
        listString.add("Finished");
        listString.add("Today");
        listString.add("Coming");
        listfragment.add(new Finished());
        listfragment.add(new Today());
        listfragment.add(new Comming());
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new MyPageoneviewpagerAdapter(getFragmentManager(), listString, listfragment));
        mViewPager.setCurrentItem(1);
        mTablayout.setupWithViewPager(mViewPager);
        return v;
    }
}



