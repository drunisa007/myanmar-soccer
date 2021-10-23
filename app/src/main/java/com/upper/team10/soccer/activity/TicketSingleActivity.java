package com.upper.team10.soccer.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.upper.team10.soccer.Model.TicketModel;
import com.upper.team10.soccer.Model.TicketModelHorizontal;
import com.upper.team10.soccer.R;
import com.upper.team10.soccer.mainfragment.PageFour;
import com.upper.team10.soccer.pageonefragment.Ticket;

public class TicketSingleActivity extends AppCompatActivity {
    private  TextView name,adult,matchsection,matchdate,tickethomename,ticketawayname;
    private String homename,awayname,ticketname,ticketadult,stdsection,time,date,
    uid,email,homelogo,awaylogo,matchtype;
    private ImageView tickethomelogo,ticketawaylogo,ticketlogogo;
    private CollapsingToolbarLayout collapsingToolbar;
    private String all;
    private Toolbar tickettoolbar;
    private String query;
    private FirebaseAuth mAuth;
    private RecyclerView horizontalRecycler;
    private DatabaseReference mdRef;
    private String ref_pos;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_single);
        givingId();
        initCollapsingToolbar();
        final String ticketlogo="https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/advertisementpic%2Fticketstadium.jpg?alt=media&token=3e616429-1c1c-48bd-a3db-d13370647a3e";
        Picasso.with(this).load(ticketlogo).networkPolicy(NetworkPolicy.OFFLINE).into(ticketlogogo, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(TicketSingleActivity.this).load(ticketlogo).into(ticketlogogo);
            }
        });;
         intent=getIntent();
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
            homelogo=intent.getStringExtra("homelogo");
            awaylogo=intent.getStringExtra("awaylogo");
            ref_pos=intent.getStringExtra("ref_pos");
            matchtype=intent.getStringExtra("matchtype");
        }
        String query=homename+awayname+time+mAuth.getCurrentUser().getUid().toString();
        mdRef= FirebaseDatabase.getInstance().getReference().child("Details").child(mAuth.getCurrentUser().getUid()).child(query);
        all=homename+awayname+ticketname+ticketadult+stdsection+time+date+homelogo+awaylogo;
        name.setText(ticketname);
        adult.setText(ticketadult);
        matchsection.setText(stdsection);
        matchdate.setText(date);
        Picasso.with(getApplicationContext()).load(homelogo).networkPolicy(NetworkPolicy.OFFLINE).into(tickethomelogo, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(getApplicationContext()).load(homelogo).into(tickethomelogo);
            }
        });
        Picasso.with(getApplicationContext()).load(awaylogo).networkPolicy(NetworkPolicy.OFFLINE).into(ticketawaylogo, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(getApplicationContext()).load(awaylogo).into(ticketawaylogo);
            }
        });
        tickethomename.setText(homename);
        ticketawayname.setText(awayname);

    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<TicketModelHorizontal,MyticketHorizontalHolder> adapter=new FirebaseRecyclerAdapter<TicketModelHorizontal, MyticketHorizontalHolder>(
                TicketModelHorizontal.class,R.layout.qrcoderecyclerviewlayout, MyticketHorizontalHolder.class,mdRef

        ) {
            @Override
            protected void populateViewHolder(final MyticketHorizontalHolder viewHolder, final TicketModelHorizontal model, int position) {
                final String user_positon=getRef(position).getKey();
                viewHolder.setQrcode(generateAll(model));
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Intent in=new Intent(viewHolder.itemView.getContext(),AllUserActivity.class);
                        in.putExtras(intent);
                        in.putExtra("user_postion",user_positon);
                        startActivity(in);
                        return false;
                    }
                });
                viewHolder.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in=new Intent(viewHolder.itemView.getContext(),AllUserActivity.class);
                        in.putExtras(intent);
                        in.putExtra("user_postion",user_positon);
                        startActivityForResult(in,1000);
                    }
                });


            }

            private String generateAll(TicketModelHorizontal model) {
                String all=mAuth.getCurrentUser().getUid()+"\n"
                        +model.getMatchname()+"\n"
                        +model.getMatchaway()+"\n"
                        +model.getStdname()+"\n"
                        +model.getStdadult()+"\n"
                        +model.getStdsection()+"\n"
                        +model.getTime()+"\n"
                        +model.getDate()+"\n"
                        +model.getHomelogo()+"\n"
                        +model.getAwaylogo();
                return  all;
            }


        };
        adapter.notifyDataSetChanged();
        horizontalRecycler.setNestedScrollingEnabled(false);
        horizontalRecycler.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000&resultCode==RESULT_OK){
            finish();
        }
    }



    public static class MyticketHorizontalHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView share;
        PopupMenu popmenu;
        public MyticketHorizontalHolder(final View itemView) {
            super(itemView);
            this.itemView=itemView;
            share= (TextView) itemView.findViewById(R.id.share);
        }
        public  void setQrcode(String all){
            ImageView imageview= (ImageView) itemView.findViewById(R.id.qrimageview);
            MultiFormatWriter multi=new MultiFormatWriter();
            try{
                BitMatrix bitmatrix=multi.encode(all, BarcodeFormat.QR_CODE,300,300);
                BarcodeEncoder barcodeencoder=new BarcodeEncoder();
                Bitmap bitmap=barcodeencoder.createBitmap(bitmatrix);
                imageview.setImageBitmap(bitmap);
            }
            catch (WriterException e){
                e.printStackTrace();
            }
        }
        public void setMenu(){

        }
    }




    private void givingId() {
        tickettoolbar= (Toolbar) findViewById(R.id.tickettoolbar);
        setSupportActionBar(tickettoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth=FirebaseAuth.getInstance();
        name= (TextView) findViewById(R.id.ticketname);
        adult= (TextView) findViewById(R.id.ticketadult);
        matchsection= (TextView) findViewById(R.id.matchsection);
        matchdate= (TextView) findViewById(R.id.matchdate);
        tickethomename= (TextView) findViewById(R.id.tickethomename);
        ticketawayname= (TextView) findViewById(R.id.ticketawayname);
        tickethomelogo= (ImageView) findViewById(R.id.tickethomelogo);
        ticketawaylogo= (ImageView) findViewById(R.id.ticketawaylogo);
        ticketlogogo= (ImageView) findViewById(R.id.ticketlogo);
        horizontalRecycler= (RecyclerView) findViewById(R.id.horizontalRecycler);
        LinearLayoutManager manager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        horizontalRecycler.setLayoutManager(manager);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
    private void initCollapsingToolbar() {
        collapsingToolbar= (CollapsingToolbarLayout) findViewById(R.id.coltool);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.ticket_single_appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("Ticket Details");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }



}
