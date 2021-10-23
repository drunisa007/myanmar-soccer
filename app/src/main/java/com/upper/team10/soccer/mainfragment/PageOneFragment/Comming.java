package com.upper.team10.soccer.mainfragment.PageOneFragment;


import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import com.squareup.picasso.Picasso;
import com.upper.team10.soccer.Model.Match;
import com.upper.team10.soccer.R;
import com.upper.team10.soccer.activity.SingleTeamActivity;

import static com.upper.team10.soccer.mainfragment.PageOneFragment.Today.urlarray;

/**
 * A simple {@link Fragment} subclass.
 */
public class Comming extends Fragment {
  private FastScrollRecyclerView mRecyclerviewpageone;
    private DatabaseReference mdRef;
    private DatabaseReference mRef;
    private String connection;
   private Intent intent;
    private static String mParent;
    private DatabaseReference mPhotoRef;
    private String homename,awayname;
    protected String finalResult;
    private View v;
  private   String result;
    public Comming() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         v=inflater.inflate(R.layout.fragment_comming, container, false);
        return v;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mParent="ComingMatches";
        mRecyclerviewpageone = (FastScrollRecyclerView) v.findViewById(R.id.recyclerviewpagethree);
        mdRef = FirebaseDatabase.getInstance().getReference().child(mParent);
        mRecyclerviewpageone.setLayoutManager(new LinearLayoutManager(getActivity()));
        FirebaseRecyclerAdapter<Match, Today.MatchHolder> adapter = new FirebaseRecyclerAdapter<Match, Today.MatchHolder>(
                Match.class, R.layout.recyclermatchlayout
                , Today.MatchHolder.class, mdRef
        ) {
            @Override
            protected void populateViewHolder(final Today.MatchHolder viewHolder, final Match model, final int position) {
                viewHolder.setMatchAwayName(model.getMatchawayname());
                viewHolder.setMatchgoal(model.getMatchgoal());
                viewHolder.setMatchHomeName(model.getMatchhomename());
                viewHolder.setMatchtime(model.getMatchtime());
                viewHolder.setMatchColor(model.getMatchtype());
                viewHolder.setMatchType(model.getMatchtype());
                viewHolder.setMatchHomeLogo(urlarray[Integer.parseInt(model.getMatchhomelogo())]);
                viewHolder.setMatchAwayLogo(urlarray[Integer.parseInt(model.getMatchawaylogo())]);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String pos_id = getRef(position).getKey();
                        Pair<View,String> pairone=Pair.create(viewHolder.itemView.findViewById(R.id.teamoneimage),"logohome");
                        Pair<View,String> pairtwo=Pair.create(viewHolder.itemView.findViewById(R.id.teamtwoimage),"logoaway");
                        ActivityOptionsCompat compact =ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),pairone,pairtwo);
                        intent = new Intent(getActivity(), SingleTeamActivity.class);
                        intent.putExtra("hometext", model.getMatchhomename());
                        intent.putExtra("awaytext", model.getMatchawayname());
                        intent.putExtra("homeimage", urlarray[Integer.parseInt(model.getMatchhomelogo())]);
                        intent.putExtra("awayimage", urlarray[Integer.parseInt(model.getMatchawaylogo())]);
                        intent.putExtra("scoretext", model.getMatchgoal());
                        intent.putExtra("position", pos_id);
                        intent.putExtra("matchtype", model.getMatchtype());
                        intent.putExtra("parent", mParent);
                        intent.putExtra("date", model.getMatchtime());
                        startActivity(intent,compact.toBundle());

                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        mRecyclerviewpageone.setAdapter(adapter);

    }



    @Override
    public void onStart() {
        super.onStart();

    }







}
