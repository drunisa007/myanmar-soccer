package com.upper.team10.soccer.mainfragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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
import com.upper.team10.soccer.Model.Video;
import com.upper.team10.soccer.R;
import com.upper.team10.soccer.activity.Arun;

public class PageTwo extends Fragment {
    private RecyclerView mVidoelistpagetwo;
    private DatabaseReference mdRef;
    public static String[] vidoeUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_page_two, container, false);
        mdRef= FirebaseDatabase.getInstance().getReference().child("Videos");
        settingUrl();
        mVidoelistpagetwo= (RecyclerView) v.findViewById(R.id.mVidoelistpagetwo);
        mVidoelistpagetwo.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }

    public  static void settingUrl() {
        vidoeUrl= new String[]{
                "https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/tumbs%2Fchin%26yan.jpg?alt=media&token=05fd6625-08a5-4881-98a5-4396a5611196"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/tumbs%2Fzwe%26aye.jpg?alt=media&token=25c3a082-7f73-491e-aaed-a3b7794b68d6"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/tumbs%2Fyadanarbon%26gfa.jpg?alt=media&token=c87b0b5f-693e-4010-9ceb-ad02eb95e245"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/tumbs%2Fnaypyitaw%26magwe.jpg?alt=media&token=4eee3624-873d-4d4b-98ea-0f6e33502efb"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/tumbs%2Fsouth%26hanthar.jpg?alt=media&token=a68b9b80-3a1d-4d5f-9afd-ea04f4a3bb55"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/tumbs%2Fgfa%26chin.jpg?alt=media&token=456bf7ea-135c-49c9-ab91-12a724cccc15"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/tumbs%2Fmagwe%26zwe.jpg?alt=media&token=d8ed1657-d430-4885-8aef-052b6f1644b5"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/tumbs%2Fchin%26magwe.jpg?alt=media&token=a6c6f27e-ded6-4b09-a6ee-17b270c5772b"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/tumbs%2Fnaypyitaw%26yangon.jpg?alt=media&token=ad4dc1ef-f3a6-4726-8b99-80fcf5157925"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/tumbs%2Fshan%26yadanarbon.jpg?alt=media&token=9c9cf2d3-24b6-4e97-a667-4af6da9fe7c1"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/tumbs%2Fsouthern%26yangon.jpg?alt=media&token=bc1bc24f-db24-4ec3-ae24-23863c41d8f1"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/tumbs%2Fzwe%26yan.jpg?alt=media&token=240934ee-2fb0-4591-8d94-20403a07a665"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/tumbs%2Fyan%26gfa.jpg?alt=media&token=d4b6ec2e-cb1e-4182-8b34-a4ba7ed3863f"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/tumbs%2Fyangon%26rahine.jpg?alt=media&token=529e35bf-ebf5-485d-b99f-21122c6e04d4"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/ThumbsFileforAff%2Fcombodia%20vs%20myanmar.jpg?alt=media&token=4df2d16b-a36c-4016-9641-36d01bd94613"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/ThumbsFileforAff%2Findo%20vs%20philipines.jpg?alt=media&token=c7b158bb-7397-4ea6-b964-6297d9cf49e8"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/ThumbsFileforAff%2Findo%20vs%20thailand.jpg?alt=media&token=87a24326-2595-4dab-b756-214615d67bab"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/ThumbsFileforAff%2Fmalay%20vs%20cambodia.jpg?alt=media&token=2d80d0a8-7896-447b-9894-fe1ddc73208d"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/ThumbsFileforAff%2Fmallay%20vs%20vietnam.jpg?alt=media&token=79640e4f-5c6a-44c4-a5e2-0eed714e3bcf"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/ThumbsFileforAff%2Fmyanmar%20vs%20malay.jpg?alt=media&token=80e5801c-0745-45a3-973e-410e7d2e5244"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/ThumbsFileforAff%2Fmyanmar%20vs%20vietnam.jpg?alt=media&token=0a40e6c9-abf5-4fdf-b2de-4e587770532b"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/ThumbsFileforAff%2Fphilipines%20vs%20singapore.jpg?alt=media&token=590cb183-af98-403d-bc55-d020ab787fb9"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/ThumbsFileforAff%2Fphilipines%20vs%20thai.jpg?alt=media&token=d3908715-b465-42cf-974b-2ae7b813ec3d"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/ThumbsFileforAff%2Fsingapore%20vs%20indonisia.jpg?alt=media&token=98157bcc-0287-404d-976f-282694fdd85e"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/ThumbsFileforAff%2Fthai%20vs%20indo.jpg?alt=media&token=73e9ebe1-9d91-42ef-896f-fe139d31b8bf"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/ThumbsFileforAff%2Fthai%20vs%20indo1.jpg?alt=media&token=363d0fda-c7a9-49c2-b90e-a57a755ded83"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/ThumbsFileforAff%2Fthai%20vs%20myanmar.jpg?alt=media&token=659c1cf9-cf03-46aa-9d0e-ae2f6b095b79"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/ThumbsFileforAff%2Fthai%20vs%20singapore.jpg?alt=media&token=9f250b4c-d029-4421-a317-3041dce3ef8f"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/ThumbsFileforAff%2Fvietnam%20vs%20cambodia.jpg?alt=media&token=f022d62a-95a7-4984-b5b4-ffc626309ef5"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/ThumbsFileforAff%2Fvietnam%20vs%20indo%202.jpg?alt=media&token=3785d7b2-88c2-4ea9-9e09-e7bc8c6e66b3"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/ThumbsFileforAff%2Fvietnam%20vs%20indo.jpg?alt=media&token=9e7c671e-2795-4b09-a577-d18000b35d7f"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/Posting%2F882?alt=media&token=6d6007f0-d865-4f42-a909-70a2f6cc30e8"
        };
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseRecyclerAdapter<Video,MyVideoHolder> myvideoadapter=new FirebaseRecyclerAdapter<Video, MyVideoHolder>(
                Video.class,R.layout.recycler_layout_for_video,MyVideoHolder.class,mdRef)
        {
            @Override
            protected void populateViewHolder(MyVideoHolder viewHolder, final Video model, final int position) {
                viewHolder.setUrl(vidoeUrl[Integer.parseInt(model.getImg())]);
                viewHolder.setWeek(model.getWeek());
                final String pos=getRef(position).getKey();
                viewHolder.setHighlight(model.getHighlight());
                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),Arun.class);
                        intent.putExtra("videourl",model.getUrl());
                        intent.putExtra("pos",pos);
                        startActivity(intent);
                    }
                });
            }
        };
        myvideoadapter.notifyDataSetChanged();
        mVidoelistpagetwo.setAdapter(myvideoadapter);
    }

    public static class MyVideoHolder extends RecyclerView.ViewHolder {
        View view;
        CardView cardViewbackground;
        public MyVideoHolder(View itemView) {
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
            if(pos.equals(position)){
                cardViewbackground.setBackgroundColor(view.getContext().getResources().getColor(R.color.colorSelect));
            }
            else{
                cardViewbackground.setBackgroundColor(view.getContext().getResources().getColor(R.color.mycolor));
            }
        }
    }
}
