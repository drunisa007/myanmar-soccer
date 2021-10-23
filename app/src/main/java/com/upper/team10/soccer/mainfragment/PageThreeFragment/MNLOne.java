package com.upper.team10.soccer.mainfragment.PageThreeFragment;


import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.upper.team10.soccer.Model.Fixture;
import com.upper.team10.soccer.R;

public class MNLOne extends Fragment {
    private RecyclerView mRecyclerview;
    private DatabaseReference mFixture;

    public MNLOne() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mnlone, container, false);
        mRecyclerview = (RecyclerView) v.findViewById(R.id.fixturemnlone);
        mFixture = FirebaseDatabase.getInstance().getReference().child("Fixmnlone");
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();


    }


    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseRecyclerAdapter<Fixture, FixtureHolder> fireadapter = new FirebaseRecyclerAdapter<Fixture, FixtureHolder>(
                Fixture.class, R.layout.fixture_recyclerview, FixtureHolder.class, mFixture) {
            @Override
            protected void populateViewHolder(FixtureHolder viewHolder, Fixture model, int position) {
                viewHolder.setGoal(model.getGoal());
                String pos=getRef(position).getKey().toString();
                viewHolder.setPts(model.getPts());
                viewHolder.setP(model.getP());
                 viewHolder.colorChange(pos);
                viewHolder.setTeam(model.getTeam());
                viewHolder.setL(model.getL());
                viewHolder.setD(model.getD());
                viewHolder.setPos(model.getPos());
                viewHolder.setW(model.getW());
            }
        };
        fireadapter.notifyDataSetChanged();
        mRecyclerview.setAdapter(fireadapter);

    }


    public static class FixtureHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView textview1,textview2,textview3,textview4,textview5,textview6,textview7,textview8;
        public FixtureHolder(final View itemView) {
            super(itemView);
            this.itemView = itemView;
            textview1 = (TextView) itemView.findViewById(R.id.pos);
            textview2 = (TextView) itemView.findViewById(R.id.team);
            textview3= (TextView) itemView.findViewById(R.id.p);
            textview4 = (TextView) itemView.findViewById(R.id.w);
            textview5 = (TextView) itemView.findViewById(R.id.d);
            textview6 = (TextView) itemView.findViewById(R.id.l);
            textview7= (TextView) itemView.findViewById(R.id.goal);
            textview8 = (TextView) itemView.findViewById(R.id.pts);


        }
        public void colorChange(String pos){
            LinearLayout cardviewcolorchange= (LinearLayout) itemView.findViewById(R.id.cardviewcolorchange);
            if(pos.equals("position001")||pos.equals("position002")||pos.equals("position003")||pos.equals("position004")){
                cardviewcolorchange.setBackgroundColor(Color.parseColor("#62b3d7"));
                textview1.setTextColor(itemView.getContext().getResources().getColor(R.color.mycolor));
                textview2.setTextColor(itemView.getContext().getResources().getColor(R.color.mycolor));
                textview3.setTextColor(itemView.getContext().getResources().getColor(R.color.mycolor));
                textview4.setTextColor(itemView.getContext().getResources().getColor(R.color.mycolor));
                textview5.setTextColor(itemView.getContext().getResources().getColor(R.color.mycolor));
                textview6.setTextColor(itemView.getContext().getResources().getColor(R.color.mycolor));
                textview7.setTextColor(itemView.getContext().getResources().getColor(R.color.mycolor));
                textview8.setTextColor(itemView.getContext().getResources().getColor(R.color.mycolor));


            }
            else{
                cardviewcolorchange.setBackgroundColor(Color.parseColor("#ffffff"));
                textview1.setTextColor(itemView.getContext().getResources().getColor(R.color.colorAccent));
                textview2.setTextColor(itemView.getContext().getResources().getColor(R.color.colorAccent));
                textview3.setTextColor(itemView.getContext().getResources().getColor(R.color.colorAccent));
                textview4.setTextColor(itemView.getContext().getResources().getColor(R.color.colorAccent));
                textview5.setTextColor(itemView.getContext().getResources().getColor(R.color.colorAccent));
                textview6.setTextColor(itemView.getContext().getResources().getColor(R.color.colorAccent));
                textview7.setTextColor(itemView.getContext().getResources().getColor(R.color.colorAccent));
                textview8.setTextColor(itemView.getContext().getResources().getColor(R.color.colorAccent));

            }
        }

        public void setPos(String string) {

            textview1.setText(string);
        }

        public void setTeam(String string) {
            textview2.setText(string);
        }

        public void setP(String string) {
            textview3.setText(string);
        }

        public void setW(String string) {
            textview4.setText(string);
        }

        public void setD(String string) {
            textview5.setText(string);
        }

        public void setL(String string) {
            textview6.setText(string);
        }

        public void setGoal(String string) {
            textview7.setText(string);
        }

        public void setPts(String string) {
            textview8.setText(string);
        }

    }
}