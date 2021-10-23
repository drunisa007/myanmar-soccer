package com.upper.team10.soccer.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.upper.team10.soccer.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUserActivity extends AppCompatActivity {
    private RecyclerView recyclerview_alluseractivity;
    private DatabaseReference mdRef;
    private Toolbar toolbar;
    private String ref_pos;
    private DatabaseReference mTicketRef,mTicketRefText;
    private FirebaseAuth mAuth;
    private String adultname;
    private String homename,awayname,ticketname,ticketadult,stdsection,time,date, uid,email,homelogo,awaylogo,matchtype;
    private String user_postion;
    private List<String> list=new ArrayList<>();
    private   DatabaseReference mDeleteticketDetais;
    private DatabaseReference mReceiveTicket;
    private MaterialDialog mDialog,mdialog;
    private DatabaseReference mdProfile;
    private DatabaseReference mNoti;
    private String check_username;
    private List<String> listname=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);
        givingId();
     Intent intent=getIntent();
        if(intent!=null){
            homename=intent.getStringExtra("homename");
            awayname=intent.getStringExtra("awayname");
            ticketname=intent.getStringExtra("ticketname");
            ticketadult=intent.getStringExtra("ticketadult");
            stdsection=intent.getStringExtra("stdsection");
            time=intent.getStringExtra("time");
            date=intent.getStringExtra("date");
            uid=intent.getStringExtra("uid");
            email=intent.getStringExtra("email");
            matchtype=intent.getStringExtra("matchtype");
            homelogo=intent.getStringExtra("homelogo");
            awaylogo=intent.getStringExtra("awaylogo");
            ref_pos=intent.getStringExtra("ref_pos");
        }
        user_postion=getIntent().getStringExtra("user_postion");
        String query=homename+awayname+time+mAuth.getCurrentUser().getUid().toString();
         mReceiveTicket=FirebaseDatabase.getInstance().getReference().child("ReceiveTickets");
        mDeleteticketDetais= FirebaseDatabase.getInstance().getReference().child("Details").child(mAuth.getCurrentUser().getUid()).child(query);
        mTicketRef=FirebaseDatabase.getInstance().getReference().child("Ticketdetails").child(mAuth.getCurrentUser().getUid())
        .child(ref_pos);
        mTicketRefText=FirebaseDatabase.getInstance().getReference().child("Ticketdetails").child(mAuth.getCurrentUser().getUid())
                .child(ref_pos);
        mTicketRefText.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            String adult= dataSnapshot.child("stdadult").getValue(String.class);
            givingData(adult);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recyclerview_alluseractivity.setLayoutManager(new LinearLayoutManager(this));

    }

    private void givingData(String adult) {
        list.add(0,adult);
    }

    private void givingId() {
        mAuth=FirebaseAuth.getInstance();
        mNoti=FirebaseDatabase.getInstance().getReference().child("Notifications");
        recyclerview_alluseractivity= (RecyclerView) findViewById(R.id.recyclerview_alluseractivity);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Choose friend to share tickets");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mdRef=FirebaseDatabase.getInstance().getReference().child("Profiles");
        mdProfile=FirebaseDatabase.getInstance().getReference().child("Profiles").child(mAuth.getCurrentUser().getUid());
        mdProfile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listname.add(0,dataSnapshot.child("name").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mdProfile=FirebaseDatabase.getInstance().getReference().child("Profiles");
        mDialog=new MaterialDialog.Builder(this).title("Sending")
                .content("Please wait...")
                .progress(true,0)
                .canceledOnTouchOutside(false)
                .cancelable(false)
                .build();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<AllUser,MyAllUserViewHolder> adapter=new FirebaseRecyclerAdapter<AllUser, MyAllUserViewHolder>(
                AllUser.class,R.layout.alluser_layout,MyAllUserViewHolder.class,mdRef) {
            @Override
            protected void populateViewHolder(final MyAllUserViewHolder viewHolder, final AllUser model, int position) {
                final String user_pos=getRef(position).getKey();
                if(listname.get(0).equals(model.getName())){
                       viewHolder.carviewtesting.setLayoutParams(new CardView.LayoutParams(0,0));
                }
                else{

                    viewHolder.setName(model.getName());
                    viewHolder.setImage(model.getImage());

                }
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
            mdialog= new MaterialDialog.Builder(AllUserActivity.this)
                                .title(model.getName())
                                .content("Are you sure? You want to send ticket to "+model.getName())
                                .positiveText("Yes")
                                .negativeText("No")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                       final int x=Integer.parseInt(list.get(0))-1;
                                        mTicketRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                mTicketRef.child("stdadult").setValue(String.valueOf(x));
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                        mDeleteticketDetais.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                mDeleteticketDetais.child(user_postion).removeValue();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                        final DatabaseReference mReceive=mReceiveTicket.child(user_pos).push();
                                        mReceive.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                             Map<String,String>  receiveMap = new HashMap<String, String>();
                                                receiveMap.put("stdsection", stdsection);
                                                receiveMap.put("stdname","Thuwanna Stadium" );
                                                receiveMap.put("stdadult", "1");
                                                receiveMap.put("time", time);
                                                receiveMap.put("uid", mAuth.getCurrentUser().getUid().toString());
                                                receiveMap.put("date", date);
                                                receiveMap.put("name", ticketname);
                                                receiveMap.put("email", email);
                                                receiveMap.put("matchname", homename);
                                                receiveMap.put("matchaway", awayname);
                                                receiveMap.put("matchtype", matchtype);
                                                receiveMap.put("homelogo", homelogo);
                                                receiveMap.put("awaylogo", awaylogo);
                                                receiveMap.put("sender_name",ticketname);
                                                mReceive.setValue(receiveMap);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                        settingAsynTask();

                                    }
                                }).build();

                             mdialog.show();
                    }
                });

            }
        };

        recyclerview_alluseractivity.setAdapter(adapter);

    }

    public static class MyAllUserViewHolder extends RecyclerView.ViewHolder {
        View view;
        CardView carviewtesting;
        public MyAllUserViewHolder(View itemView) {
            super(itemView);
            this.view=itemView;
            carviewtesting= (CardView) view.findViewById(R.id.cardviewtesting);
        }
        public void setName(String name){
            TextView textviewuser= (TextView) view.findViewById(R.id.textviewuser);
            textviewuser.setText(name);
        }
        public void setImage(final String name){
            final CircleImageView cir_imageview= (CircleImageView) view.findViewById(R.id.imageuser);
            Picasso.with(view.getContext()).load(name).networkPolicy(NetworkPolicy.OFFLINE).into(cir_imageview, new Callback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onError() {
                    Picasso.with(view.getContext()).load(name).into(cir_imageview);
                }
            });
        }
    }
    private void settingAsynTask() {
        AsyncTask<String, String, String> syntask =
                new AsyncTask<String, String, String>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        mDialog.show();
                    }

                    @Override
                    protected String doInBackground(String... params) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return "done";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (s.equals("done")) {
                            mDialog.dismiss();
                            finish();
                            Toast.makeText(getApplicationContext(), "You have successfully send your ticket", Toast.LENGTH_LONG).show();


                        }
                    }
                };
        syntask.execute();
    }
    @Override
    public void onPause() {
        super.onPause();

        if ((mdialog != null) && mdialog.isShowing())
            mdialog.dismiss();
        mDialog.dismiss();
        mDialog=null;
        mdialog = null;
    }

}
