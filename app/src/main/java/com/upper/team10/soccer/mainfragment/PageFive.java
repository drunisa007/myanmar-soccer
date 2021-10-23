package com.upper.team10.soccer.mainfragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.upper.team10.soccer.Model.News;
import com.upper.team10.soccer.R;
import com.upper.team10.soccer.activity.SingleNewsActivity;

public class PageFive extends Fragment {
    private RecyclerView pagefiverecyclerview;
    private DatabaseReference mdRef;
    private ImageView one,two,three,four,five,six,seven,eight;
   private String[] url;
    private ImageView[] id;

    public PageFive() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_page_five, container, false);

        mdRef= FirebaseDatabase.getInstance().getReference().child("News");
        pagefiverecyclerview= (RecyclerView) v.findViewById(R.id.pagefiverecyclerview);
        pagefiverecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseRecyclerAdapter<News,MyNewViewHolder> newsadapter=new FirebaseRecyclerAdapter<News, MyNewViewHolder>(
                News.class,R.layout.pagefive_news_layout,MyNewViewHolder.class,mdRef
        )
        {
            @Override
            protected void populateViewHolder(final MyNewViewHolder viewHolder, final News model, int position) {
                viewHolder.setNewsDate(model.getDate());
                viewHolder.setNewsTitle(model.getTitle());
                viewHolder.setNewsTitleImage(model.getImage());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),SingleNewsActivity.class);
                        intent.putExtra("image",model.getImage());
                        intent.putExtra("body1",model.getBody1());
                        intent.putExtra("body2",model.getBody2());
                        intent.putExtra("body3",model.getBody3());
                        intent.putExtra("title",model.getTitle());
                        intent.putExtra("date",model.getDate());
                        startActivity(intent);
                    }
                });
            }
        };
        newsadapter.notifyDataSetChanged();
        pagefiverecyclerview.setAdapter(newsadapter);
    }



    public static class MyNewViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        public MyNewViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
        }
        public void setNewsTitle(String name){
            TextView textview= (TextView) itemView.findViewById(R.id.newstitle);
            textview.setText(name);
        }
        public void setNewsTitleImage(final String name){
            final ImageView imageveiw= (ImageView) itemView.findViewById(R.id.newstitleimage);
            Picasso.with(itemView.getContext()).load(name).networkPolicy(NetworkPolicy.OFFLINE).into(imageveiw, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(itemView.getContext()).load(name).into(imageveiw);
                }
            });
        }
        public void setNewsDate(String name){
            TextView textview= (TextView) itemView.findViewById(R.id.newsdate);
            textview.setText(name);
        }
    }
}
