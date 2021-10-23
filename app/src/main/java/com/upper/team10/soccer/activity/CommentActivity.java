package com.upper.team10.soccer.activity;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.upper.team10.soccer.Model.Fixture;
import com.upper.team10.soccer.Model.comment;
import com.upper.team10.soccer.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {
    private RecyclerView commentrecyclerview;
    private DatabaseReference mdRef;
    private String pos_id,post_uid,post_username;
    private EditText edittextcomment;
    private String commenttext;
    private ImageView sendImage;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String user_id;
    private String commentname,commentimage;
    private DatabaseReference mDatabase;
    private List<comment> list=new ArrayList<>();
    private ImageView imageview_custom_first,imageview_custom;
    private TextView textview_custom_goal,textview_custom_name;
    private String homeimage,awayimage,matchgoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        pos_id=getIntent().getStringExtra("pos_id");
        post_uid=getIntent().getStringExtra("post_uid");
        post_username=getIntent().getStringExtra("post_username");
        homeimage=getIntent().getStringExtra("homeimage");
        awayimage=getIntent().getStringExtra("awayimage");
        matchgoal=getIntent().getStringExtra("matchgoal");
        givingId();
        sendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commenttext=edittextcomment.getText().toString();
                if(!TextUtils.isEmpty(commenttext)){
                    final DatabaseReference database=mdRef.push();
                    database.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map<String,String> map=new HashMap<>();
                            map.put("name",commentname);
                            map.put("url",commentimage);
                            map.put("comment",commenttext);
                            database.setValue(map);
                            edittextcomment.setText("");
                            edittextcomment.clearFocus();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    Toast.makeText(CommentActivity.this,"Comment should not be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        commentrecyclerview.setLayoutManager(layoutManager);
    }

    private void settingRecycler() {
        FirebaseRecyclerAdapter<comment,MyCommentHolder> adapter=new FirebaseRecyclerAdapter<comment, MyCommentHolder>(
                comment.class,R.layout.comment_layout,MyCommentHolder.class,mdRef
        )
        {
            @Override
            protected void populateViewHolder(MyCommentHolder  viewHolder, comment model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setComment(model.getComment());
                viewHolder.setUrl(model.getUrl());
            }
        };
        adapter.notifyDataSetChanged();
        commentrecyclerview.setAdapter(adapter);
    }


    public static class MyCommentHolder extends RecyclerView.ViewHolder {
        View itemView;
        public MyCommentHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
        }
        public void setUrl(final String url){
            final CircleImageView imageview= (CircleImageView) itemView.findViewById(R.id.commentPic);
            Picasso.with(itemView.getContext()).load(url).networkPolicy(NetworkPolicy.OFFLINE).into(imageview, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(itemView.getContext()).load(url).into(imageview);
                }
            });
        }
        public void setName(String name){
            TextView textview= (TextView) itemView.findViewById(R.id.commentName);
            textview.setText(name);
        }
        public void   setComment(String comment){
            TextView textview= (TextView) itemView.findViewById(R.id.commentText);
            textview.setText(comment);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabase=FirebaseDatabase.getInstance().getReference("Profiles");
        final DatabaseReference databaseReference=mDatabase.child(user_id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                commentname=dataSnapshot.child("name").getValue().toString();
                commentimage=dataSnapshot.child("image").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        settingRecycler();

    }

    private void givingId() {
        commentrecyclerview= (RecyclerView) findViewById(R.id.commentrecyclerview);
            mdRef= FirebaseDatabase.getInstance().getReference().child(post_uid).child(pos_id).child("Comments");
        edittextcomment= (EditText) findViewById(R.id.edittextcomment);
        sendImage= (ImageView) findViewById(R.id.sendImage);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar=getSupportActionBar();
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setTitle(post_username);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LayoutInflater inflater= (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v=inflater.inflate(R.layout.custom_layout,null);
        imageview_custom_first= (ImageView) v.findViewById(R.id.imageview_custom_first);
        imageview_custom= (ImageView) v.findViewById(R.id.imageview_custom);
        textview_custom_goal= (TextView) v.findViewById(R.id.textview_custom_goal);
        textview_custom_name= (TextView) v.findViewById(R.id.textview_custom_name);
        textview_custom_name.setText(post_username);
        actionbar.setCustomView(v);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        user_id=mUser.getUid();
        settingImageOfActionBar();


    }

    private void settingImageOfActionBar() {
                Picasso.with(getApplicationContext()).load(homeimage).networkPolicy(NetworkPolicy.OFFLINE).into(imageview_custom_first, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(getApplicationContext()).load(homeimage).into(imageview_custom_first);
                    }
                });
        Picasso.with(getApplicationContext()).load(awayimage).networkPolicy(NetworkPolicy.OFFLINE).into(imageview_custom, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(getApplicationContext()).load(awayimage).into(imageview_custom);
            }
        });
        textview_custom_goal.setText(matchgoal);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
             onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}
