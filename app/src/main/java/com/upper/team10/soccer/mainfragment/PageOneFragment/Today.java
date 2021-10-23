package com.upper.team10.soccer.mainfragment.PageOneFragment;


import android.content.Intent;
import android.os.Bundle;
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
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.upper.team10.soccer.Model.Match;
import com.upper.team10.soccer.R;
import com.upper.team10.soccer.activity.SingleTeamActivity;

public class Today extends Fragment {
    private FastScrollRecyclerView mRecyclerviewpageone;
    private DatabaseReference mdRef;
    private Intent intent;
    private final String mParent = "TodayMatches";
    private DatabaseReference databaseRef;
    private String pos_id;
    long mLastClickTime = System.currentTimeMillis();
    static final long CLICK_TIME_INTERVAL = 300;
    public static String[] urlarray;


    public Today() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_today, container, false);
        GettingLogo();
        mRecyclerviewpageone = (FastScrollRecyclerView) v.findViewById(R.id.recyclerviewpageone);
        mdRef = FirebaseDatabase.getInstance().getReference().child(mParent);
        mRecyclerviewpageone.setLayoutManager(new LinearLayoutManager(getActivity()));
        databaseRef = FirebaseDatabase.getInstance().getReference();
        return v;
    }



    private void GettingLogo() {
        urlarray= new String[]{
                "https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/footballlogo%2FAyeyawady_United_logo.png?alt=media&token=8104fee0-0f6c-4933-915f-5bcbe0463533"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/footballlogo%2FChin_United_FC_Logo.png?alt=media&token=5bf538a3-d2cd-4604-8ccd-7d37a7d91f3d"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/footballlogo%2FGospel_for_Asia_FC_Logo.png?alt=media&token=59ab217c-a0df-43da-b324-2e1ec96d2224"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/footballlogo%2FHANTHARWADY-UTD-FC.jpg?alt=media&token=99ca0859-15bf-41de-8e31-753d214c1811"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/footballlogo%2Fmagwe.png?alt=media&token=c590d5ab-5b91-4b45-99df-dd4ac87fdf47"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/footballlogo%2FNay_Pyi_Taw_FC_logo.png?alt=media&token=80c13e29-8c49-486d-a5a8-196765d159e3"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/footballlogo%2FRK.png?alt=media&token=b29e03c6-e24d-4746-946f-d3b0ec1f6b0f"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/footballlogo%2FS.png?alt=media&token=034fd29d-bf61-44eb-9da4-1488515d6803"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/footballlogo%2FSouthernMyanmarFc.png?alt=media&token=2ad4dee9-a1d7-42be-9114-26c6424d97c5"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/footballlogo%2FYadanabon_FC.png?alt=media&token=710f93f6-980d-4794-b7c5-153221a2b10d"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/footballlogo%2Fygn.png?alt=media&token=f83fc779-87ef-4a69-8de0-b21a3d1ca28e"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/footballlogo%2FZwekaPinUtdFc.png?alt=media&token=ded9dc5c-2a0e-477c-86dc-9dba86a759bd"
                ,"default"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/CityYangon.png?alt=media&token=291a204c-608e-48b9-84c2-242f6c0a2375"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/Dagon_FC_logo.png?alt=media&token=c794a077-7322-47fd-ba40-16f0a4aae5a0"
                ,"default"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/Mawyawadi_FC_Logo.png?alt=media&token=3efe05ea-1127-4730-a4fb-2f3080e36c42"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/Myawady_logo.png?alt=media&token=6cbee115-c8bd-4853-afe9-00d833487398"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/Phoenix.png?alt=media&token=98a0340e-1e37-4208-99f8-32b3b9bab20b"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/Pong_Gan_FC_logo.png?alt=media&token=ed7b1a12-110e-432b-a686-c2929d5db7b7"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/Silver_Stars_FC_logo.png?alt=media&token=32cc2c25-fd00-416d-b012-c3a8edb1eff0"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/thanlwin.png?alt=media&token=1acc5200-fb70-45d8-95f5-1528f0d1f3d5"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/Myanmar_University_FC_logo.png?alt=media&token=4aa35733-d4d9-45f4-9d72-c025ec1698e0"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/Flags%2FSingapore.png?alt=media&token=3dccab1a-a7ce-41bf-b35d-acd0c99a9ab6"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/Flags%2FThailand.png?alt=media&token=e85ff633-da13-4f79-a5d0-cf638c4379e0"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/Flags%2FPhilli.png?alt=media&token=7d245efa-fa45-40ae-8862-7248e305d47c"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/Flags%2FIndonesia.jpg?alt=media&token=7dd6bba8-a796-4c9f-82a1-4949b23abe4e"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/Flags%2FVietnam.png?alt=media&token=98589b35-e94a-4c34-a33a-3dedceeef7ab"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/Flags%2FMalaysia.png?alt=media&token=21e816c4-eb35-4fc9-ae09-d6110ca699af"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/Flags%2FCambodia.png?alt=media&token=ed7769ec-7a12-4740-97a4-e51cded47a89"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/Flags%2FMyanmar.png?alt=media&token=20103bbb-cbad-4c17-bc8b-724946979ae2"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/Flags%2FUzbekistan.png?alt=media&token=a4135387-55f7-4edc-9b72-fd29264ee3fe"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/Flags%2FDPR%20Korea.png?alt=media&token=791587d3-3a98-4e64-b9c5-f5443edc61b9"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/Flags%2FTimor.png?alt=media&token=aa495b58-e4c4-4921-aa76-13142900b88d"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/Flags%2FJapan.png?alt=media&token=8e38ee20-e210-4340-90ed-2c327ae99185"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/Flags%2FBrunei.png?alt=media&token=5c7e3e55-3c49-4349-aac0-79870e1bb2e0"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/Flags%2Floasss.png?alt=media&token=9da2bc78-4055-43f1-9f7a-d54ec3016b2d"

        };
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseRecyclerAdapter<Match, MatchHolder> adapter = new FirebaseRecyclerAdapter<Match, MatchHolder>(
                Match.class, R.layout.recyclermatchlayout, MatchHolder.class, mdRef) {
            @Override
            protected void populateViewHolder(final MatchHolder viewHolder, final Match model, final int position) {
                viewHolder.setMatchAwayName(model.getMatchawayname());
                viewHolder.setMatchgoal(model.getMatchgoal());
                viewHolder.setMatchHomeName(model.getMatchhomename());
                viewHolder.setMatchtime(model.getMatchtime());
                viewHolder.setMatchColor(model.getMatchtype());
                viewHolder.setMatchType(model.getMatchtype());
                final String pos = getRef(position).getKey();
                viewHolder.setMatchHomeLogo(urlarray[Integer.parseInt(model.getMatchhomelogo())]);
                viewHolder.setMatchAwayLogo(urlarray[Integer.parseInt(model.getMatchawaylogo())]);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            pos_id = getRef(position).getKey();
                            Pair<View,String> pairone=Pair.create(viewHolder.itemView.findViewById(R.id.teamoneimage),"logohome");
                            Pair<View,String> pairtwo=Pair.create(viewHolder.itemView.findViewById(R.id.teamtwoimage),"logoaway");
                            ActivityOptionsCompat compact =ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),pairone,pairtwo);
                            intent = new Intent(getActivity(), SingleTeamActivity.class);
                            intent.putExtra("hometext", viewHolder.textviewone.getText().toString());
                            intent.putExtra("awaytext", viewHolder.textviewtwo.getText().toString());
                            intent.putExtra("homeimage", urlarray[Integer.parseInt(model.getMatchhomelogo())]);
                            intent.putExtra("awayimage", urlarray[Integer.parseInt(model.getMatchawaylogo())]);
                            intent.putExtra("scoretext", model.getMatchgoal());
                            intent.putExtra("position", pos_id);
                            intent.putExtra("parent", mParent);
                            intent.putExtra("matchtype",model.getMatchtype());
                            intent.putExtra("date",model.getMatchtime());
                            getActivity().startActivity(intent,compact.toBundle());
                        }


                });
            }
        };
        adapter.notifyDataSetChanged();
        mRecyclerviewpageone.setAdapter(adapter);

    }


    public static class MatchHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView textviewone, textviewtwo;
        ImageView recyclermatch_imageview;

        public MatchHolder(final View itemView) {
            super(itemView);
            this.itemView = itemView;
            recyclermatch_imageview= (ImageView) itemView.findViewById(R.id.recyclermatch_imageview);

        }

        public void setMatchHomeName(String homename) {
            textviewone = (TextView) itemView.findViewById(R.id.teamonename);
            textviewone.setText(homename);
        }

        public void setMatchAwayName(String awayname) {
            textviewtwo = (TextView) itemView.findViewById(R.id.teamtwoname);
            textviewtwo.setText(awayname);

        }

        public void setMatchtime(String matchtime) {
            TextView textview = (TextView) itemView.findViewById(R.id.matchtime);
            textview.setText(matchtime);
        }

        public void setMatchgoal(String matchgoal) {
            TextView textview = (TextView) itemView.findViewById(R.id.matchgoal);
            textview.setText(matchgoal);

        }

        public void setMatchType(String matchType) {
            TextView textview = (TextView) itemView.findViewById(R.id.matchtype);
            textview.setText(matchType);


        }
        public void setMatchColor(String matchcolor){
            if(matchcolor.equalsIgnoreCase("MNL Two")){
                Picasso.with(itemView.getContext()).load(R.drawable.mnl2).into(recyclermatch_imageview);
            }
            else if(matchcolor.equalsIgnoreCase("MNL One")){
                   Picasso.with(itemView.getContext()).load(R.drawable.mnl1).into(recyclermatch_imageview);
            }
            else if(matchcolor.equalsIgnoreCase("AFF")){
                Picasso.with(itemView.getContext()).load(R.drawable.aff).into(recyclermatch_imageview);
            }
            else if(matchcolor.equalsIgnoreCase("M-150 Cup")){
                Picasso.with(itemView.getContext()).load(R.drawable.m150).into(recyclermatch_imageview);
            }
            else{
                Picasso.with(itemView.getContext()).load("null").into(recyclermatch_imageview);

            }

        }

        public void setMatchHomeLogo(final String urlhome) {
            final ImageView imageview = (ImageView) itemView.findViewById(R.id.teamoneimage);
            Picasso.with(itemView.getContext()).load(urlhome).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.yadanapone).into(imageview, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(itemView.getContext()).load(urlhome).placeholder(R.drawable.yadanapone).into(imageview);
                }
            });
        }

        public void setMatchAwayLogo(final String urlaway) {
            final ImageView imageview = (ImageView) itemView.findViewById(R.id.teamtwoimage);
            Picasso.with(itemView.getContext()).load(urlaway).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.yadanapone).into(imageview, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(itemView.getContext()).load(urlaway).placeholder(R.drawable.yadanapone).into(imageview);
                }
            });
        }

    }

}