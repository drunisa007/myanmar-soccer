package com.upper.team10.soccer.mainfragment.PageThreeFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.upper.team10.soccer.Model.Fixture;
import com.upper.team10.soccer.R;


public class MNLTwo extends Fragment {
    private RecyclerView mrecyclerview;
    private DatabaseReference mdRef;
    public MNLTwo() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_mnltwo, container, false);
        mrecyclerview = (RecyclerView) v.findViewById(R.id.fixturemnltwo);
        mdRef = FirebaseDatabase.getInstance().getReference().child("Fixmnltwo");
        mrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();


    }


    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseRecyclerAdapter<Fixture, MNLOne.FixtureHolder> fireadapter = new FirebaseRecyclerAdapter<Fixture, MNLOne.FixtureHolder>(
                Fixture.class, R.layout.fixture_recyclerview, MNLOne.FixtureHolder.class, mdRef) {
            @Override
            protected void populateViewHolder(MNLOne.FixtureHolder viewHolder, Fixture model, int position) {
                viewHolder.setGoal(model.getGoal());
                viewHolder.setPts(model.getPts());
                viewHolder.setP(model.getP());
                String pos=getRef(position).getKey().toString();
                viewHolder.colorChange(pos);
                viewHolder.setTeam(model.getTeam());
                viewHolder.setL(model.getL());
                viewHolder.setD(model.getD());
                viewHolder.setPos(model.getPos());
                viewHolder.setW(model.getW());
            }
        };
        fireadapter.notifyDataSetChanged();
        mrecyclerview.setAdapter(fireadapter);

    }
}
