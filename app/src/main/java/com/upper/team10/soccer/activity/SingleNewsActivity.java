package com.upper.team10.soccer.activity;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.upper.team10.soccer.R;
import com.upper.team10.soccer.viewpagerAdapter.ImageViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SingleNewsActivity extends AppCompatActivity {
    private String image,body1,body2,body3,title,date;
    private ImageView imageviewnews;
    private TextView textviewnews1,textviewnews2,textviewnews3,newstitle;
    private CollapsingToolbarLayout sectioncollapsing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_news);
        Toolbar newstoolbar= (Toolbar) findViewById(R.id.newstoolbar);
        setSupportActionBar(newstoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initCollapsingToolbar();
        Intent intent=getIntent();
        if(intent!=null){
            image=intent.getStringExtra("image");
            body1=intent.getStringExtra("body1");
            body2=intent.getStringExtra("body2");
            body3=intent.getStringExtra("body3");
            title=intent.getStringExtra("title");
            date=intent.getStringExtra("date");
        }
        imageviewnews= (ImageView) findViewById(R.id.imageviewnews);
        textviewnews1= (TextView) findViewById(R.id.textviewnews1);
        textviewnews2= (TextView) findViewById(R.id.textviewnews2);
        textviewnews3= (TextView) findViewById(R.id.textviewnews3);
        newstitle= (TextView) findViewById(R.id.newstitle);
        Picasso.with(getApplicationContext()).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(imageviewnews, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(getApplicationContext()).load(image).into(imageviewnews);
            }
        });
        textviewnews1.setText(Html.fromHtml(body1));
        textviewnews2.setText(Html.fromHtml(body2));
        textviewnews3.setText(Html.fromHtml(body3));
        newstitle.setText(title+" ("+date+")");


    }
    private void initCollapsingToolbar() {
        sectioncollapsing = (CollapsingToolbarLayout) findViewById(R.id.collpasetoolbar);
        sectioncollapsing.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.singlenews_appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    sectioncollapsing.setTitle(title);
                    isShow = true;
                } else if (isShow) {
                    sectioncollapsing.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
