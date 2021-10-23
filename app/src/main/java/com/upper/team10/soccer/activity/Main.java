package com.upper.team10.soccer.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.upper.team10.soccer.R;
import com.upper.team10.soccer.Testing.BottomNavigationViewHelper;
import com.upper.team10.soccer.Testing.ConnectivityReceiver;
import com.upper.team10.soccer.firebase.Fireapp;
import com.upper.team10.soccer.mainfragment.PageFive;
import com.upper.team10.soccer.mainfragment.PageFour;
import com.upper.team10.soccer.mainfragment.PageOne;
import com.upper.team10.soccer.mainfragment.PageThree;
import com.upper.team10.soccer.mainfragment.PageTwo;
import com.upper.team10.soccer.viewpagerAdapter.CustomViewPager;
import com.upper.team10.soccer.viewpagerAdapter.MyViewPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.data;
import static android.R.attr.switchMinWidth;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,ConnectivityReceiver.ConnectivityReceiverListener {
    public static Toolbar mToolbar;
    private NavigationView mNavView;
    private DrawerLayout mDrawerLayout;
    private CustomViewPager mViewPager;
    private ActionBarDrawerToggle mToggle;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseAuth mAuth;
    private CoordinatorLayout activity_tablayout;
    private DatabaseReference mdRef;
    private String name,url;
    private DatabaseReference mNotiRef;
    private DatabaseReference mChangeNotiRef;
    private DatabaseReference  mdRefNoti;
    private BottomNavigationView bottom_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        Intent intent=getIntent();
        if(intent!=null){
            name=intent.getStringExtra("name");
            url=intent.getStringExtra("url");
            if(TextUtils.isEmpty(name)){

            }
            else{
                settingNameForGoogleUser();

            }
        }

        activity_tablayout= (CoordinatorLayout) findViewById(R.id.activity_tablayout);
        checkConnection();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    finish();
                }

            }
        };
        mNavView = (NavigationView) findViewById(R.id.navigationview);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerSetUp();
        bottom_navigation= (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mViewPager = (CustomViewPager) findViewById(R.id.viewpager);
        List<Fragment> list = new ArrayList<>();
        list.add(new PageOne());
        list.add(new PageTwo());
        list.add(new PageThree());
        list.add(new PageFive());
        list.add(new PageFour());
        mViewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), list));
        mViewPager.setPagingEnabled(false);
       mViewPager.setOffscreenPageLimit(5);
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menumatches) {
                    mViewPager.setCurrentItem(0);
                    getSupportActionBar().setTitle("Matches");
                }
                else if (id == R.id.menuvideos) {
                    mViewPager.setCurrentItem(1);
                    getSupportActionBar().setTitle("Videos");
                } else if (id == R.id.menufixtures) {
                      getSupportActionBar().setTitle("Fixtures");
                    mViewPager.setCurrentItem(2);
                }
                else if(id==R.id.menunews){
                    getSupportActionBar().setTitle("News");
                    mViewPager.setCurrentItem(3);
                }
                else if(id==R.id.menutickets){
                    getSupportActionBar().setTitle("Tickets");
                    mViewPager.setCurrentItem(4);
                }
                return true;
            }
        });
        BottomNavigationViewHelper.disableShiftMode(bottom_navigation);

    }

    private void settingNameForGoogleUser() {
        if(mAuth.getCurrentUser()!=null) {
            mdRef = FirebaseDatabase.getInstance().getReference().child("Profiles");
            mdRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())) {
                    }
                    else {
                        final DatabaseReference mRef = mdRef.child(mAuth.getCurrentUser().getUid());
                        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Map<String, String> hashmap = new HashMap<>();
                                hashmap.put("name", name);
                                hashmap.put("balance", "0");
                                hashmap.put("image",url);
                                //hashmap.put("image", "https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/advertisementpic%2Fdefault-avatar.png?alt=media&token=4142d167-5df7-4a9c-baad-895e1d8736ca");

                                mRef.setValue(hashmap);
                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
            }


    private void drawerSetUp() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Matches");
        mToggle = new ActionBarDrawerToggle(Main.this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mToggle.syncState();
        mDrawerLayout.addDrawerListener(mToggle);
        mNavView.setNavigationItemSelectedListener(this);

    }


    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (mViewPager.getCurrentItem() == 1) {
            mViewPager.setCurrentItem(0);
            bottom_navigation.getMenu().getItem(0).setChecked(true);
           // bottom_navigation.getMenu().set
        } else if (mViewPager.getCurrentItem() == 2) {
            mViewPager.setCurrentItem(0);
            bottom_navigation.getMenu().getItem(0).setChecked(true);

        } else if (mViewPager.getCurrentItem() == 3) {
            mViewPager.setCurrentItem(0);
            bottom_navigation.getMenu().getItem(0).setChecked(true);

        }
        else if(mViewPager.getCurrentItem()==4){
            mViewPager.setCurrentItem(0);
            bottom_navigation.getMenu().getItem(0).setChecked(true);

        }

        else {
            super.onBackPressed();
            finishAffinity();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Fireapp.getInstance().setConnectivityListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
         if(id==R.id.about){
             startActivity(new Intent(Main.this,about_activity.class));
        }
        else if(id==R.id.receive_ticket){
             startActivity(new Intent(Main.this,Receive_Ticket.class));

        }
        else if(id==R.id.wallet){
            startActivity(new Intent(Main.this,Setting.class));
        }

        else if(id==R.id.logout){
             new MaterialDialog.Builder(Main.this)
                     .title("Log Out")
                     .content("Are you sure you want to log out?")
                     .positiveText("Ok")
                     .negativeText("Cancel")
                     .onPositive(new MaterialDialog.SingleButtonCallback() {
                         @Override
                         public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                             sigOut();

                         }
                     }).show();
        }
        else if(id==R.id.exit){
            new  MaterialDialog.Builder(Main.this)
                    .title("Exit")
                    .content("Are u sure want to exit")
                    .positiveText("Yes")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finishAffinity();
                        }
                    })
                    .negativeText("Cancel")
                    .show();
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void sigOut() {
      mAuth.signOut();


    }
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {

        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
            CoordinatorLayout coor= (CoordinatorLayout) findViewById(R.id.testingcoordinatorlayout);

            Snackbar snackbar = Snackbar.make(coor, message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
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

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
            showSnack(isConnected);

    }

}
