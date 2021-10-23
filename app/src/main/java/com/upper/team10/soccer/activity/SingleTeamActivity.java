package com.upper.team10.soccer.activity;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.upper.team10.soccer.R;
import com.upper.team10.soccer.pageonefragment.LineUp;
import com.upper.team10.soccer.pageonefragment.Ticket;
import com.upper.team10.soccer.pageonefragment.chat;
import com.upper.team10.soccer.viewpagerAdapter.MySinglePagerAdatper;

import java.util.ArrayList;
import java.util.List;

public class SingleTeamActivity extends AppCompatActivity {
    private TabLayout mTablayout;
    private ViewPager mViewpager;
    private String hometext, awaytext, homeimage, awayimage, scoretext, matchtype, date;
    private TextView textviewhome, textviewaway, scoreid;
    private ImageView imageviewhome, imageviewaway;
    private String parent, position;
    private ImageView singleimageview;
    private CoordinatorLayout coor;
    private Toolbar singleteamtoolbar;
    private DatabaseReference mdRef;
    FloatingActionButton fab;
    private List<String> arunList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_team);
        initCollapsingToolbar();
        coor = (CoordinatorLayout) findViewById(R.id.coor);
        fab = (FloatingActionButton) findViewById(R.id.fab1);

        Snackbar snackbar = Snackbar.make(coor, "No Connection", Snackbar.LENGTH_INDEFINITE).setAction("RELOAD", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
        if (!haveNetworkConnection()) {
            snackbar.show();
        }
        givingId();
        mdRef = FirebaseDatabase.getInstance().getReference().child(parent).child(position);
        mdRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String testing_name = dataSnapshot.child("matchhomename").getValue(String.class);
                int x = chanGingLayout(testing_name);
                sending(x);
            }

            private void sending(int x) {
                List<String> listTitle = new ArrayList<>();
                List<Fragment> listFragment = new ArrayList<>();
                mTablayout = (TabLayout) findViewById(R.id.singletablayout);
                mViewpager = (ViewPager) findViewById(R.id.singleviewpager);
                listTitle.add("Ticket");
                listTitle.add("Lineup");
                listTitle.add("Predict");
                Fragment fragmentticket = new Ticket();
                Fragment fragmentlineup = new LineUp();
                Fragment fragmentchat = new chat();
                Bundle bd = new Bundle();
                bd.putString("parent", parent);
                bd.putString("postuid", hometext + position + parent + awaytext);
                bd.putString("position", position);
                bd.putString("hometext", hometext);
                bd.putString("awaytext", awaytext);
                bd.putString("date", date);
                bd.putString("homeimage", homeimage);
                bd.putString("awayimage", awayimage);
                bd.putString("matchtype", matchtype);
                bd.putString("data", String.valueOf(x));
                fragmentticket.setArguments(bd);
                fragmentlineup.setArguments(bd);
                fragmentchat.setArguments(bd);
                listFragment.add(fragmentticket);
                listFragment.add(fragmentlineup);
                listFragment.add(fragmentchat);

                mViewpager.setAdapter(new MySinglePagerAdatper(getSupportFragmentManager(), listTitle, listFragment));

                mViewpager.setOffscreenPageLimit(3);
                mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        Log.e("Position : ", position + "");
                        if (position == 0 || position == 1) {
                            fab.hide();
                        } else {
                            fab.show();
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                mTablayout.setupWithViewPager(mViewpager);
                mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        if (tab.getPosition() == 0) {
                            fab.hide();
                        } else if (tab.getPosition() == 1) {
                            fab.hide();

                        } else if (tab.getPosition() == 2) {
                            fab.show();
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void givingId() {
        singleteamtoolbar = (Toolbar) findViewById(R.id.singleteamtoolbar);
        setSupportActionBar(singleteamtoolbar);
        ActionBar actionbar = getSupportActionBar();
        doingCustomToolbar(actionbar);
        textviewhome = (TextView) findViewById(R.id.hometextid);
        textviewaway = (TextView) findViewById(R.id.awaytextid);
        imageviewhome = (ImageView) findViewById(R.id.homeimageid);
        imageviewaway = (ImageView) findViewById(R.id.awayimageid);
        singleimageview = (ImageView) findViewById(R.id.singleimageview);
        final String singelimage = "https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/advertisementpic%2Ffoot.jpg?alt=media&token=b7527012-69c2-4e70-9c3d-bb25f15e6196";
        Picasso.with(SingleTeamActivity.this).load(singelimage).networkPolicy(NetworkPolicy.OFFLINE).into(singleimageview, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(SingleTeamActivity.this).load(singelimage).into(singleimageview);
            }
        });
        scoreid = (TextView) findViewById(R.id.scoreid);
        hometext = getIntent().getStringExtra("hometext");
        awaytext = getIntent().getStringExtra("awaytext");
        homeimage = getIntent().getStringExtra("homeimage");
        awayimage = getIntent().getStringExtra("awayimage");
        scoretext = getIntent().getStringExtra("scoretext");
        matchtype = getIntent().getStringExtra("matchtype");
        date = getIntent().getStringExtra("date");
        parent = getIntent().getStringExtra("parent");
        position = getIntent().getStringExtra("position");
        textviewhome.setText(hometext);
        textviewaway.setText(awaytext);
        scoreid.setText(scoretext);
        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1.setVisibility(View.GONE);
        Picasso.with(SingleTeamActivity.this).load(homeimage).networkPolicy(NetworkPolicy.OFFLINE).into(imageviewhome, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(SingleTeamActivity.this).load(homeimage).into(imageviewhome);
            }
        });
        Picasso.with(SingleTeamActivity.this).load(awayimage).networkPolicy(NetworkPolicy.OFFLINE).into(imageviewaway, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(SingleTeamActivity.this).load(awayimage).into(imageviewaway);

            }
        });

    }

    private void doingCustomToolbar(ActionBar actionbar) {
        actionbar.setTitle("Yeah It is Working");
        actionbar.setDisplayHomeAsUpEnabled(true);
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout sectioncollapsing = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        sectioncollapsing.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.singleteamappbar);
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
                    sectioncollapsing.setTitle(hometext + " " + scoretext + " " + awaytext);
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
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (mViewpager.getCurrentItem() == 1) {
            mViewpager.setCurrentItem(0);
        } else if (mViewpager.getCurrentItem() == 2) {
            mViewpager.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public int chanGingLayout(String matchhomename) {
        int result = 0;
        int aye, chin, gfa, hanthar, magwe, naypyi, rakhine, shan, southern, yadanarbon, yangon, zwekapin, ctstar, ctyan, dag, mah, mawya,
                mya, phoe, pong, sil, than, uni, singa, thai, phil, indo, viet, malay, cam, myan, uzbe, dpr, timor, jap, bru, loa;
        String home = matchhomename.replaceAll("\\s", "").toLowerCase();
        aye = home.indexOf("aye");
        chin = home.indexOf("chin");
        gfa = home.indexOf("gfa");
        hanthar = home.indexOf("han");
        magwe = home.indexOf("mag");
        naypyi = home.indexOf("naypyi");
        rakhine = home.indexOf("rak");
        shan = home.indexOf("shan");
        southern = home.indexOf("south");
        yadanarbon = home.indexOf("yadanar");
        yangon = home.indexOf("yang");
        zwekapin = home.indexOf("zweka");
        ctstar = home.indexOf("cityst");
        ctyan = home.indexOf("cityyan");
        dag = home.indexOf("dag");
        mah = home.indexOf("mah");
        mawya = home.indexOf("maw");
        mya = home.indexOf("mya");
        phoe = home.indexOf("phoe");
        pong = home.indexOf("pong");
        sil = home.indexOf("sil");
        than = home.indexOf("than");
        uni = home.indexOf("uni");
        singa = home.indexOf("singa");
        thai = home.indexOf("thai");
        phil = home.indexOf("phil");
        indo = home.indexOf("indo");
        viet = home.indexOf("viet");
        malay = home.indexOf("malay");
        cam = home.indexOf("cam");
        myan = home.indexOf("myan");
        uzbe = home.indexOf("uzbe");
        dpr = home.indexOf("dpr");
        timor = home.indexOf("timor");
        jap = home.indexOf("jap");
        bru = home.indexOf("bru");
        loa = home.indexOf("loa");
        if (aye == 0) {
            result = 0;
        } else if (chin == 0) {
            result = 1;
        } else if (hanthar == 0) {
            result = 2;
        } else if (gfa == 0) {
            result = 3;
        } else if (magwe == 0) {
            result = 4;

        } else if (naypyi == 0) {
            result = 0;

        } else if (rakhine == 0) {
            result = 1;

        } else if (shan == 0) {
            result = 2;

        } else if (southern == 0) {
            result = 3;
        } else if (yadanarbon == 0) {
            result = 4;
        } else if (yangon == 0) {
            result = 0;
        } else if (zwekapin == 0) {
            result = 1;
        } else if (ctstar == 0) {
            result = 2;
        } else if (ctyan == 0) {
            result = 3;
        } else if (dag == 0) {
            result = 4;
        } else if (mah == 0) {
            result = 0;
        } else if (mawya == 0) {
            result = 1;
        } else if (mya == 0) {
            result = 4;
        } else if (phoe == 0) {
            result = 3;
        } else if (pong == 0) {
            result = 4;
        } else if (sil == 0) {
            result = 0;
        } else if (than == 0) {
            result = 1;
        } else if (uni == 0) {
            result = 2;
        } else if (singa == 0) {
            result = 3;

        } else if (thai == 0) {
            result = 4;
        } else if (phil == 0) {
            result = 0;
        } else if (indo == 0) {
            result = 1;
        } else if (viet == 0) {
            result = 2;
        } else if (malay == 0) {
            result = 1;
        } else if (cam == 0) {
            result = 4;
        } else if (myan == 0) {
            result = 4;
        } else if (uzbe == 0) {
            result = 1;
        } else if (dpr == 0) {
            result = 2;
        } else if (timor == 0) {
            result = 3;
        } else if (jap == 0) {
            result = 4;
        } else if (bru == 0) {
            result = 0;
        } else if (loa == 0) {
            result = 1;
        }
        return result;
    }


}
/*
    getWindow().setNavigationBarColor(getResources().getColor(R.color.myrandomcolor1));
        getWindow().setStatusBarColor(getResources().getColor(R.color.myrandomcolor2));*/
