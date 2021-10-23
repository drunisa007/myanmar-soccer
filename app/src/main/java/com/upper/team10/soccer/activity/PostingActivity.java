package com.upper.team10.soccer.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.upper.team10.soccer.R;

import java.util.HashMap;
import java.util.Map;

public class PostingActivity extends AppCompatActivity implements TextWatcher {
    private EditText uploadedithome,uploadeditaway,uploadtext;
    private CardView upload;
    private TextView uploadhome,uploadaway,uploadhomegoal,uploadawaygoal;
    private String homename,awayname;
    private DatabaseReference mdRef;
    private String post_uid;
    private ProgressDialog mProgress;
    private String username,imageurl;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference databaseUsername;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Prediction");


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        databaseUsername=FirebaseDatabase.getInstance().getReference().child("Profiles");
        databaseUsername.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(mUser.getUid())){
                    DatabaseReference data=databaseUsername.child(mUser.getUid());
                    data.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            username=dataSnapshot.child("name").getValue(String.class);
                            imageurl=dataSnapshot.child("image").getValue(String.class);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    new MaterialDialog.Builder(PostingActivity.this)
                            .title("Alert")
                            .content("Please change your name first")
                            .positiveText("Change")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Intent intent=new Intent(PostingActivity.this,Setting.class);
                                    startActivity(intent);

                                }
                            })
                            .cancelable(false)
                            .canceledOnTouchOutside(false)
                            .negativeText("Cancel")
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    finish();
                                }
                            })
                            .show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mProgress=new ProgressDialog(this);
        mProgress.setTitle("Upload");
        mProgress.setMessage("Posting");
        uploadhome= (TextView) findViewById(R.id.uploadhome);
        uploadaway= (TextView) findViewById(R.id.uploadaway);
        uploadtext= (EditText) findViewById(R.id.uploadtext);
        homename=getIntent().getStringExtra("homename");
        awayname=getIntent().getStringExtra("awayname");
        post_uid=getIntent().getStringExtra("post_uid");
        mdRef= FirebaseDatabase.getInstance().getReference().child(post_uid);
        uploadhome.setText(homename.toString());
        uploadaway.setText(awayname.toString());
        uploadedithome= (EditText) findViewById(R.id.uploadedithome);
        uploadeditaway= (EditText) findViewById(R.id.uploadeditaway);
        upload= (CardView) findViewById(R.id.upload);
        uploadhomegoal= (TextView) findViewById(R.id.uploadgoalhome);
        uploadawaygoal= (TextView) findViewById(R.id.uploadgoalaway);
        uploadedithome.addTextChangedListener(this);
        uploadeditaway.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                uploadawaygoal.setText(uploadeditaway.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result=haveNetworkConnection();
                if(result){
                    if(!TextUtils.isEmpty(uploadtext.getText().toString())){
                        mProgress.show();
                        final DatabaseReference mRef=mdRef.push();
                        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Map<String,String> map=new HashMap<>();
                                map.put("postgoal",uploadhomegoal.getText().toString()+" : "+uploadawaygoal.getText().toString());
                                map.put("posttext",uploadtext.getText().toString());
                                map.put("postusername",username);
                                map.put("imageone","default");
                                map.put("imagetwo","default");
                                map.put("image",imageurl);
                                mRef.setValue(map);
                                finish();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(PostingActivity.this, "error", Toast.LENGTH_SHORT).show();
                                mProgress.hide();
                            }
                        });
                    }
                    else{
                        Toast.makeText(PostingActivity.this, "Reason should not be empty", Toast.LENGTH_SHORT).show();
                    }
                    }

                else{
                    Toast.makeText(PostingActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        uploadhomegoal.setText(uploadedithome.getText().toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
    @Override
    public void onPause() {
        super.onPause();

        if ((mProgress != null) && mProgress.isShowing())
            mProgress.dismiss();
        mProgress = null;
    }
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
