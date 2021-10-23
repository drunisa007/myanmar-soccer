package com.upper.team10.soccer.activity;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.upper.team10.soccer.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SectionActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView section_stadium, section_section, section_price, section_location, section_match, section_matchdate;
    private String stdname, stdsection, stdprice, stdloc, stdmatch, stdmatchdate;
    private CardView section_cardview_buy,location_finder;
    private CardView chooseadult_dialog, buycancel_dialog, buycomfirm_dialog;
    private TextView adult_section_dialog, section_dialog;
    private MaterialDialog material_dialog;
    private MaterialDialog mdialog;
    private String adult_number;
    private List<String> listadult = new ArrayList<>();
    private DatabaseReference mBalanceRef;
    private DatabaseReference mdRef;
    private DatabaseReference mTicketRef;
    private FirebaseAuth mAuth;
    private String mBalance;
    private String time, name, homelogo, awaylogo,image;
    private TextInputEditText section_edittext;
    private String matchhomename, matchawayname;
    private MaterialDialog mDialog;
    private CollapsingToolbarLayout sectioncollapsing;
    private ImageView viewflipperone,viewflippertwo;
    private String imagelist[];
    private int x;
    private   Map<String, String> ondatamap;
    private DatabaseReference mdatabaseticketSingleDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        givingId();
        for (int i = 1; i <= 10; i++) {
            listadult.add(String.valueOf(i));
        }
        Intent intent = getIntent();
        if (intent != null) {
            stdname = intent.getStringExtra("name");
            stdsection = intent.getStringExtra("section");
            stdprice = intent.getStringExtra("price");
            //stdloc is matchtype
            stdloc = intent.getStringExtra("location");
            stdmatch = intent.getStringExtra("match");
            stdmatchdate = intent.getStringExtra("matchdate");
            matchhomename = intent.getStringExtra("matchhomename");
            matchawayname = intent.getStringExtra("matchawayname");
            homelogo = intent.getStringExtra("homelogo");
            awaylogo = intent.getStringExtra("awaylogo");

        }
        settingImage();
        gettingImage(stdsection);

        section_stadium.setText(stdname);
        section_section.setText(stdsection);
        section_price.setText(stdprice + " Kyats");
        section_location.setText(stdloc);
        section_match.setText(stdmatch);
        section_matchdate.setText(stdmatchdate);
        section_cardview_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdialog = new MaterialDialog.Builder(SectionActivity.this)
                        .customView(R.layout.single_section_layout_dialog, true)
                        .build();
                View view = mdialog.getCustomView();
                givingDialogId(view);
                section_dialog.setText(stdsection);
                mdialog.show();
            }
        });
    }

    private void gettingImage(String stdsection) {
        if(stdsection.equals("Section A")){
            realImageSetting(0,1);
        }

    else    if(stdsection.equals("Section B")){
            realImageSetting(2,3);
        }
     else   if(stdsection.equals("Section C")){
            realImageSetting(4,5);
        }
      else  if(stdsection.equals("Section D")){
            realImageSetting(6,7);
        }

    }

    private void realImageSetting(final int x, final int y) {
        Picasso.with(getApplicationContext()).load(imagelist[x]).networkPolicy(NetworkPolicy.OFFLINE).into(viewflipperone, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(getApplicationContext()).load(imagelist[x]).into(viewflipperone);
            }
        });
        Picasso.with(getApplicationContext()).load(imagelist[y]).networkPolicy(NetworkPolicy.OFFLINE).into(viewflippertwo, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(getApplicationContext()).load(imagelist[y]).into(viewflippertwo);
            }
        });
    }

    private void settingImage() {
        imagelist= new String[]{"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/StadiumSectionName%2Ft1.jpg?alt=media&token=fec4eac4-954d-4386-84e0-a46622aa099a",
"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/StadiumSectionName%2Ft2.jpg?alt=media&token=0f52d97d-852d-4a0c-9644-efcfb68614c6",
                "https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/StadiumSectionName%2Ft3.jpg?alt=media&token=f151e57c-a232-4c3f-ba05-c0be6ad0604b",
                "https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/StadiumSectionName%2Ft4.jpg?alt=media&token=a9546a9c-ef59-48dd-9b9f-4562f04b3258"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/StadiumSectionName%2Ft5.jpg?alt=media&token=d58ca285-5a85-425f-ac82-7732351734fa"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/StadiumSectionName%2Ft6.jpg?alt=media&token=6c018bd6-bff4-4a72-9fb8-6edc4be07d44"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/StadiumSectionName%2Ft7.jpg?alt=media&token=d1a921b8-0f92-4087-9e1e-229a971db62e"
                ,"https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/StadiumSectionName%2Ft8.jpg?alt=media&token=969e9a94-ead0-4f74-b079-cf4621c689c9"
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        material_dialog = new MaterialDialog.Builder(SectionActivity.this)
                .title("Choose Seat Counts")
                .items(listadult)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        adult_section_dialog.setText(text);
                        adult_number = text.toString();
                        adult_section_dialog.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                }).build();
    }

    private void givingDialogId(View view) {
        section_dialog = (TextView) view.findViewById(R.id.section_dialog);
        chooseadult_dialog = (CardView) view.findViewById(R.id.chooseadult_dialog);
        buycancel_dialog = (CardView) view.findViewById(R.id.buycancel_dialog);
        buycomfirm_dialog = (CardView) view.findViewById(R.id.buycomfirm_dialog);
        adult_section_dialog = (TextView) view.findViewById(R.id.adult_section_dialog);
        chooseadult_dialog.setOnClickListener(SectionActivity.this);
        section_edittext = (TextInputEditText) view.findViewById(R.id.section_edittext);
        buycancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdialog.dismiss();
            }
        });
        buycomfirm_dialog.setOnClickListener(SectionActivity.this);
    }

    private void givingId() {
        mAuth = FirebaseAuth.getInstance();
        CollapsingToolbarLayout sectioncollapsing = (CollapsingToolbarLayout) findViewById(R.id.sectioncollapsing);
        sectioncollapsing.setTitle("");
        Toolbar toolbar = (Toolbar) findViewById(R.id.sectiontoolbar);
        setSupportActionBar(toolbar);
        initCollapsingToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewflipperone= (ImageView) findViewById(R.id.viewflipperone);
        viewflippertwo= (ImageView) findViewById(R.id.viewflippertwo);
        section_stadium = (TextView) findViewById(R.id.section_stadium);
        section_section = (TextView) findViewById(R.id.section_section);
        section_price = (TextView) findViewById(R.id.section_price);
        section_location = (TextView) findViewById(R.id.section_location);
        section_match = (TextView) findViewById(R.id.section_match);
        section_matchdate = (TextView) findViewById(R.id.section_matchdate);
        section_cardview_buy = (CardView) findViewById(R.id.section_cardview_buy);
        location_finder= (CardView) findViewById(R.id.location_finder);
        location_finder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("geo:16.817179, 96.185179"));
                startActivity(i);
            }
        });
        mBalanceRef = FirebaseDatabase.getInstance().getReference().child("Profiles").child(mAuth.getCurrentUser().getUid());
        mBalanceRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mBalance = dataSnapshot.child("balance").getValue(String.class);
                name = dataSnapshot.child("name").getValue(String.class);
                image=dataSnapshot.child("image").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mdRef = FirebaseDatabase.getInstance().getReference().child("Profiles").child(mAuth.getCurrentUser().getUid());
        mTicketRef = FirebaseDatabase.getInstance().getReference().child("Ticketdetails").child(mAuth.getCurrentUser().getUid());
        Calendar canlender = Calendar.getInstance();
        time = canlender.getTime().toString();
        mDialog = new MaterialDialog.Builder(this)
                .title("Ticket")
                .content("Buying Ticket. . .")
                .canceledOnTouchOutside(false)
                .progress(true, 0)
                .build();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.chooseadult_dialog:
                material_dialog.show();
                break;
            case R.id.buycomfirm_dialog:
                String name = section_edittext.getText().toString();
                if (!TextUtils.isEmpty(name)) {
                    if (!TextUtils.isEmpty(adult_number)) {
                        if(name.length()==11){
                            doingComfirm();
                        }
                        else{
                            Toast.makeText(this, "Invalid Phone Number ", Toast.LENGTH_SHORT).show();
                        }


                    }
                    else {
                        Toast.makeText(this, "Please Choose Adult", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Phone no should not be empty ", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private void doingComfirm() {
        int price = Integer.parseInt(stdprice);
        final String cost = getCost(price, adult_number);
        String result = calculate(cost, mBalance);
        if(result.equals("Ok")){
            new MaterialDialog.Builder(SectionActivity.this)
                    .title("Seat Price is " + cost + " MMK")
                    .content(" Account Balance is " + mBalance + " MMK" + "\n" + "Are you sure want to buy this ticket? ")
                    .positiveText("Buy")
                    .positiveColor(getResources().getColor(R.color.colorPrimary))
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                int bal = Integer.parseInt(mBalance);
                                int cost_int = Integer.parseInt(cost);
                                final int final_bal = bal - cost_int;
                                final Map<String, String> map = new HashMap();
                                map.put("balance", String.valueOf(final_bal));
                                map.put("name", name);
                                map.put("image",image);
                                mdRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        mdRef.setValue(map);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                final DatabaseReference ticketdetails = mTicketRef.push();
                                ticketdetails.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        ondatamap = new HashMap<String, String>();
                                        ondatamap.put("stdsection", stdsection);
                                        ondatamap.put("stdname", stdname);
                                        ondatamap.put("stdadult", adult_number);
                                        ondatamap.put("time", time);
                                        ondatamap.put("uid", mAuth.getCurrentUser().getUid().toString());
                                        ondatamap.put("date", stdmatchdate);
                                        ondatamap.put("name", name);
                                        ondatamap.put("email", section_edittext.getText().toString());
                                        ondatamap.put("matchname", matchhomename);
                                        ondatamap.put("matchaway", matchawayname);
                                        ondatamap.put("matchtype", stdloc);
                                        ondatamap.put("homelogo", homelogo);
                                        ondatamap.put("awaylogo", awaylogo);
                                        ticketdetails.setValue(ondatamap);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                x=Integer.parseInt(adult_number);
                                String query=matchhomename+matchawayname+time+mAuth.getCurrentUser().getUid().toString();
                                mdatabaseticketSingleDetails=FirebaseDatabase.getInstance().getReference().child("Details").child(mAuth.getCurrentUser().getUid()).child(query);
                                for(int i=0;i<x;i++) {
                                    final DatabaseReference mdatabase=mdatabaseticketSingleDetails.push();
                                    mdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            mdatabase.setValue(ondatamap);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                                settingAsynTask();


                            }

                    })
                    .negativeText("Cancel")
                    .show();
        }
        else{
            new MaterialDialog.Builder(SectionActivity.this)
                    .title("Alert")
                    .content(Html.fromHtml(getString(R.string.title)))
                    .contentColor(getResources().getColor(R.color.colorPrimary))
                    .positiveText("Ok")
                    .positiveColor(getResources().getColor(R.color.colorPrimary))
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            startActivity(new Intent(SectionActivity.this,Setting.class));
                        }
                    })
                    .negativeText("Cancel")
                    .show();
        }

    }

    private void initCollapsingToolbar() {
        sectioncollapsing = (CollapsingToolbarLayout) findViewById(R.id.sectioncollapsing);
        sectioncollapsing.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.section_appbar);
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
                    sectioncollapsing.setTitle(stdname + " (" + stdsection + " )");
                    isShow = true;
                } else if (isShow) {
                    sectioncollapsing.setTitle(" ");
                    isShow = false;
                }
            }
        });
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
                            Thread.sleep(8000);
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
                            mdialog.dismiss();
                            createNoti();
                            Toast.makeText(SectionActivity.this, "Thanks you , Have a nice day for the Match Sir", Toast.LENGTH_LONG).show();
                          finish();

                        }
                    }
                };
        syntask.execute();
    }



    private  String calculate(String cost, String mBalance) {
        int cost_int = Integer.parseInt(cost);
        int balance = Integer.parseInt(mBalance);
        if (cost_int > balance) {
            return "In";
        }
        return "Ok";

    }

    private String getCost(int price, String adult_number) {
        int number = Integer.parseInt(adult_number);
        int result = price * number;
        return String.valueOf(result);
    }

    private void createNoti() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(SectionActivity.this);
        builder.setContentText("Tickets");
        builder.setContentText("Check Your Tickets in Information Section");
        builder.setSmallIcon(R.drawable.designn);
        builder.setVibrate(new long[]{200, 200, 200, 200, 200});
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(sound);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    }

