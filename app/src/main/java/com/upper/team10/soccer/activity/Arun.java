package com.upper.team10.soccer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.upper.team10.soccer.Model.Video;
import com.upper.team10.soccer.R;
import com.upper.team10.soccer.Testing.Testing;
import com.upper.team10.soccer.Testing.TwoLayoutRecyclerview;
import com.upper.team10.soccer.mainfragment.PageTwo;

import static com.upper.team10.soccer.mainfragment.PageTwo.settingUrl;
import static com.upper.team10.soccer.mainfragment.PageTwo.vidoeUrl;

public class Arun extends YouTubeBaseActivity {
    public static final String API_KEY = "AIzaSyCAOfrgKtF9JmrNWwjw3FkgXNTdiEp0oWA";
    public YouTubePlayerView mYoutube;
    private YouTubePlayer.OnInitializedListener mListener;
    private CardView playbutton;
    private String realurl = "RI_Iz3-88Fw";
    private String u;
    private Toolbar toolbar;
    private CoordinatorLayout coordinator;
    private RecyclerView recycler_video_multiple;
    private String pos;
    YouTubePlayer mPlayer;
    private DatabaseReference mdRef;
private YouTubePlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arun);
        mYoutube = (YouTubePlayerView) findViewById(R.id.youtube);
        settingUrl();
        mdRef= FirebaseDatabase.getInstance().getReference().child("Videos");
        coordinator = (CoordinatorLayout) findViewById(R.id.coordinator);
        recycler_video_multiple= (RecyclerView) findViewById(R.id.recycler_video_multiple);
        realurl = getIntent().getStringExtra("videourl");
        pos=getIntent().getStringExtra("pos");
        recycler_video_multiple.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mYoutube.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(realurl);
                if(!b){
                     mPlayer = youTubePlayer;
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Video,MyVideoHolders> myvideoadapter=new FirebaseRecyclerAdapter<Video, MyVideoHolders>(
                Video.class,R.layout.recycler_layout_for_video,MyVideoHolders.class,mdRef)
        {
            @Override
            protected void populateViewHolder(final MyVideoHolders viewHolder, final Video model, final int position) {
                viewHolder.setUrl(vidoeUrl[Integer.parseInt(model.getImg())]);
                viewHolder.setWeek(model.getWeek());
                viewHolder.setHighlight(model.getHighlight());
                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mPlayer!=null){
                            mPlayer.cueVideo(model.getUrl());
                            givingColor();
                        }
                    }

                    private void givingColor() {

                    }
                });

            }
        };


        myvideoadapter.notifyDataSetChanged();
        recycler_video_multiple.setAdapter(myvideoadapter);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mPlayer.setFullscreen(true);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
        }
    }


    public static class MyVideoHolders extends RecyclerView.ViewHolder {
        View view;
        CardView cardViewbackground;
        public MyVideoHolders(View itemView) {
            super(itemView);
            view=itemView;
            cardViewbackground= (CardView) view.findViewById(R.id.cardViewbackground);

        }




        public void setUrl(final String url){
            final ImageView imageview= (ImageView) view.findViewById(R.id.thumbnailsimage);
            Picasso.with(view.getContext()).load(url).resize(200,150).networkPolicy(NetworkPolicy.OFFLINE).into(imageview, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(view.getContext()).load(url).resize(200,150).into(imageview);
                }
            });
        }
        public void setHighlight(String name){
            TextView textview= (TextView) view.findViewById(R.id.textgoalvideo);
            textview.setText(name);
        }
        public void setWeek(String name){
            TextView textview= (TextView) view.findViewById(R.id.textweekndate);
            textview.setText(name);
        }
        public void setColor(String pos,String position){
            if(pos.equalsIgnoreCase(position)){
                cardViewbackground.setBackgroundColor(view.getContext().getResources().getColor(R.color.mycolor));
            }
            else{
                cardViewbackground.setBackgroundColor(view.getContext().getResources().getColor(R.color.colorSelect));

            }
        }
    }
}
