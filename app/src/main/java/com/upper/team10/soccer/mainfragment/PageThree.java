package com.upper.team10.soccer.mainfragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.upper.team10.soccer.Model.Fixture;
import com.upper.team10.soccer.Model.Match;
import com.upper.team10.soccer.R;
import com.upper.team10.soccer.activity.SingleTeamActivity;
import com.upper.team10.soccer.mainfragment.PageOneFragment.Today;
import com.upper.team10.soccer.mainfragment.PageThreeFragment.MNLOne;
import com.upper.team10.soccer.mainfragment.PageThreeFragment.MNLTwo;
import com.upper.team10.soccer.mainfragment.PageThreeFragment.U_18;
import com.upper.team10.soccer.mainfragment.PageThreeFragment.U_20;
import com.upper.team10.soccer.viewpagerAdapter.MyPageoneviewpagerAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.upper.team10.soccer.activity.Main.mToolbar;

public class PageThree extends Fragment {
    private TabLayout mTablayout;
    private ViewPager mViewpager;
    private List<Fragment> listfragment;
    private List<String> listtitle;

    public PageThree() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_page_three, container, false);
        listfragment=new ArrayList<>();
        listtitle=new ArrayList<>();
        listfragment.add(new MNLOne());
        listfragment.add(new MNLTwo());
        listfragment.add(new U_18());
        listfragment.add(new U_20());
        listtitle.add("MNL I");
        listtitle.add("MNL II");
        listtitle.add("U-18");
        listtitle.add("U-20");
        mTablayout= (TabLayout) v.findViewById(R.id.pagethreetablayout);
        mViewpager= (ViewPager) v.findViewById(R.id.pagethreeviewpager);
        //mViewpager.setOffscreenPageLimit(4);
        mViewpager.setAdapter(new MyPageoneviewpagerAdapter(getFragmentManager(), listtitle, listfragment));
        mTablayout.setupWithViewPager(mViewpager);
        return v;
}
}

