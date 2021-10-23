package com.upper.team10.soccer.pageonefragment;


import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.upper.team10.soccer.Model.Post;
import com.upper.team10.soccer.R;
import com.upper.team10.soccer.activity.CommentActivity;
import com.upper.team10.soccer.activity.PostingActivity;
import com.upper.team10.soccer.activity.Setting;


public class chat extends Fragment implements View.OnClickListener {
    private RecyclerView mrecyclerviewposting;
    private DatabaseReference mdataRef;
    private String post_uid;
    private String homeimage, awayimage;
    private String hometext, awaytext;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
   private DatabaseReference databaseUsername;
    private String parent;
    FloatingActionButton fab1;
    public chat() {
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        mAuth=FirebaseAuth.getInstance();
        Bundle bundle = getArguments();
        if (bundle != null) {
            post_uid = bundle.getString("postuid");
            homeimage = bundle.getString("homeimage");
            awayimage = bundle.getString("awayimage");
            hometext = bundle.getString("hometext");
            awaytext = bundle.getString("awaytext");
            parent=bundle.getString("parent");
            mrecyclerviewposting = (RecyclerView) v.findViewById(R.id.recyclerviewposting);
            fab1= (FloatingActionButton) getActivity().findViewById(R.id.fab1);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setReverseLayout(true);
            layoutManager.setStackFromEnd(true);
            fab1.setOnClickListener(this);
            mrecyclerviewposting.setLayoutManager(layoutManager);
            mdataRef = FirebaseDatabase.getInstance().getReference().child(post_uid);
            mdataRef.keepSynced(true);
        } else {
            Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
        }



            return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseRecyclerAdapter<Post, PostViewHolder> myfirebaseadapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(
                Post.class, R.layout.recyclerviewpostingpic, PostViewHolder.class, mdataRef) {
            @Override
            protected void populateViewHolder(final PostViewHolder viewHolder, final Post model, int position) {
                final String pos_id=getRef(position).getKey();
                viewHolder.setPostGoal(model.getPostgoal());
                viewHolder.setPostText(model.getPosttext());
                viewHolder.setPostusername(model.getPostusername());
                viewHolder.setPosthomeimage(homeimage);
                viewHolder.setUserImage(model.getImage());
                viewHolder.setPostawayimage(awayimage);
                viewHolder.setPostImage(model.getUrl());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAuth= FirebaseAuth.getInstance();
                        mUser=mAuth.getCurrentUser();
                        databaseUsername=FirebaseDatabase.getInstance().getReference().child("Profiles");
                        databaseUsername.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild(mUser.getUid())){
                                    Pair<View,String> pairone=Pair.create(viewHolder.itemView.findViewById(R.id.posthomeimage),"home");
                                    Pair<View,String> pairtwo=Pair.create(viewHolder.itemView.findViewById(R.id.postawayimage),"away");
                                    ActivityOptionsCompat compact =ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),pairone,pairtwo);
                                    Intent intent1=new Intent(viewHolder.itemView.getContext(), CommentActivity.class);
                                    intent1.putExtra("pos_id",pos_id);
                                    intent1.putExtra("post_uid",post_uid);
                                    intent1.putExtra("post_username",model.getPostusername());
                                    intent1.putExtra("homeimage",homeimage);
                                    intent1.putExtra("awayimage",awayimage);
                                    intent1.putExtra("matchgoal",model.getPostgoal());
                                    startActivity(intent1,compact.toBundle());

                                }
                                else{
                                    new MaterialDialog.Builder(getActivity())
                                            .title("Alert")
                                            .content("Please change your name first")
                                            .positiveText("Change")
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    Intent intent=new Intent(getActivity(),Setting.class);
                                                    startActivity(intent);

                                                }
                                            })
                                            .cancelable(false)
                                            .canceledOnTouchOutside(false)
                                            .negativeText("Cancel")
                                            .show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });
            }
        };
        myfirebaseadapter.notifyDataSetChanged();
        mrecyclerviewposting.setAdapter(myfirebaseadapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        boolean result = haveNetworkConnection();
        switch (id){
            case R.id.fab1:
                if (result) {
                    if(!parent.equals("FinishedMatches"))
                    {
                        Intent intent = new Intent(getActivity(), PostingActivity.class);
                        intent.putExtra("homename", hometext);
                        intent.putExtra("awayname", awaytext);
                        intent.putExtra("post_uid", post_uid);
                        startActivityForResult(intent,100);
                    }
                    else{
                        Toast.makeText(getActivity(), "Prediction room is closed yet", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getActivity(), "No Connection", Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        View itemView;

        public PostViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        public void setPostGoal(String goal) {
            TextView textview = (TextView) itemView.findViewById(R.id.postgoal);
            textview.setText(goal);
        }

        public void setPostText(String text) {
            TextView textview = (TextView) itemView.findViewById(R.id.posttext);
            textview.setText(text);
        }

        public void setPostusername(String name) {
            TextView textView = (TextView) itemView.findViewById(R.id.postname);
            textView.setText(name);
        }

        public void setPosthomeimage(final String image) {
            final ImageView imageview = (ImageView) itemView.findViewById(R.id.posthomeimage);
            Picasso.with(itemView.getContext()).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(imageview, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(itemView.getContext()).load(image).into(imageview);
                }
            });

        }
        public void setUserImage(final String image){
            final ImageView imageview = (ImageView) itemView.findViewById(R.id.circleImageView);
            Picasso.with(itemView.getContext()).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(imageview, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(itemView.getContext()).load(image).into(imageview);
                }
            });
        }

        public void setPostawayimage(final String image) {
            final ImageView imageview = (ImageView) itemView.findViewById(R.id.postawayimage);
            Picasso.with(itemView.getContext()).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(imageview, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(itemView.getContext()).load(image).into(imageview);
                }
            });
        }
        public  void setPostImage(final String image){
            final ImageView imageview= (ImageView) itemView.findViewById(R.id.postimage);
            if(image==null){
                ConstraintLayout.LayoutParams layout= (ConstraintLayout.LayoutParams) imageview.getLayoutParams();
                layout.height= 0;
                imageview.setLayoutParams(layout );
            }
            Picasso.with(itemView.getContext()).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(imageview, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(itemView.getContext()).load(image).into(imageview);
                }
            });

        }
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
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


}
