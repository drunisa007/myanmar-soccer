package com.upper.team10.soccer.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.test.suitebuilder.annotation.Suppress;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.upper.team10.soccer.R;

import java.util.HashMap;
import java.util.Map;

import static com.upper.team10.soccer.R.id.image;
import static com.upper.team10.soccer.R.id.upload;

public class PicturePosting extends AppCompatActivity implements TextWatcher {
    private EditText uploadedithomepic,uploadeditawaypic,uploadtextpic;
    private TextView uploadhomepic,uploadawaypic;
    private TextView uploadgoalhomepic,uploadgoalawaypic;
    private CardView uploadpic;
    private ImageView uploadimagepic;
    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference databaseUsername,mdRef;
    private int codeid=1;
    private String username,imageurl;
    private String homename,awayname,post_uid;
    private  StorageReference storageReference;
    private ProgressDialog mDialog;
    private String realdownloaduri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_posting);
        settingtoolbar();
        givingId();
        uploadimagepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,codeid);
            }
        });



    }
    protected void onStart() {
        super.onStart();
        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        databaseUsername= FirebaseDatabase.getInstance().getReference().child("Profiles");
        databaseUsername.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    new MaterialDialog.Builder(PicturePosting.this)
                            .title("Alert")
                            .content("Please change your name first")
                            .positiveText("Change")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Intent intent=new Intent(PicturePosting.this,Setting.class);
                                    startActivity(intent);

                                }
                            })
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
        homename=getIntent().getStringExtra("homename");
        awayname=getIntent().getStringExtra("awayname");
        post_uid=getIntent().getStringExtra("post_uid");
        mdRef= FirebaseDatabase.getInstance().getReference().child(post_uid);
        uploadhomepic.setText(homename.toString());
        uploadawaypic.setText(awayname.toString());
        uploadedithomepic= (EditText) findViewById(R.id.uploadedithomepic);
        uploadeditawaypic= (EditText) findViewById(R.id.uploadeditawaypic);
        uploadpic= (CardView) findViewById(R.id.uploadpic);
        uploadgoalhomepic= (TextView) findViewById(R.id.uploadgoalhomepic);
        uploadgoalawaypic= (TextView) findViewById(R.id.uploadgoalawaypic);
        uploadedithomepic.addTextChangedListener(PicturePosting.this);
        uploadeditawaypic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                uploadgoalawaypic.setText(uploadeditawaypic.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        uploadpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result=haveNetworkConnection();
                if(result){
                    if(!TextUtils.isEmpty(uploadtextpic.getText().toString())){
                        if(realdownloaduri!=null){
                            mProgress.show();
                            final DatabaseReference mRef=mdRef.push();
                            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Map<String,String> map=new HashMap<>();
                                    map.put("postgoal",uploadgoalhomepic.getText().toString()+" : "+uploadgoalawaypic.getText().toString());
                                    map.put("posttext",uploadtextpic.getText().toString());
                                    map.put("postusername",username);
                                    map.put("imageone","default");
                                    map.put("imagetwo","default");
                                    map.put("url",realdownloaduri.toString());
                                    map.put("image",imageurl);
                                    mRef.setValue(map);
                                    finish();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(PicturePosting.this, "error", Toast.LENGTH_SHORT).show();
                                    mProgress.hide();
                                }
                            });
                        }
                        else{
                            Toast.makeText(PicturePosting.this, "Please Choose an image...", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        Toast.makeText(PicturePosting.this, "Reason should not be empty", Toast.LENGTH_SHORT).show();
                    }
                }

                else{
                    Toast.makeText(PicturePosting.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==codeid&&resultCode==RESULT_OK){
            mDialog.show();
            final Uri uri=data.getData();
            Picasso.with(getApplicationContext()).load(uri).into(uploadimagepic);
            StorageReference storage=storageReference.child(uri.getLastPathSegment());
            storage.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests")
                    Uri downloaduri=taskSnapshot.getDownloadUrl();
                    realdownloaduri=downloaduri.toString();
                    AsyncTask<String, String, String> syntask =
                            new AsyncTask<String, String, String>() {
                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
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

                                    }
                                }
                            };
                    syntask.execute();
                }
            });
        }


    }

    private void givingId() {
        mDialog=new ProgressDialog(this);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setMessage("Please Wait . . .");
        storageReference= FirebaseStorage.getInstance().getReference().child("Posting");
        uploadedithomepic= (EditText) findViewById(R.id.uploadedithomepic);
        uploadeditawaypic= (EditText) findViewById(R.id.uploadeditawaypic);
        uploadtextpic= (EditText) findViewById(R.id.uploadtextpic);
        uploadhomepic= (TextView) findViewById(R.id.uploadhomepic);
        uploadawaypic= (TextView) findViewById(R.id.uploadawaypic);
        uploadpic= (CardView) findViewById(R.id.uploadpic);
        uploadgoalhomepic= (TextView) findViewById(R.id.uploadgoalhomepic);
        uploadgoalawaypic= (TextView) findViewById(R.id.uploadgoalawaypic);
        uploadimagepic= (ImageView) findViewById(R.id.uploadimagepic);
    }

    private void settingtoolbar() {
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Prediction");
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        uploadgoalhomepic.setText(uploadedithomepic.getText().toString());

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
    @Override
    public void onPause() {
        super.onPause();

        if ((mProgress != null) && mProgress.isShowing())
            mProgress.dismiss();
        mDialog.dismiss();
        mProgress = null;
        mDialog.dismiss();
    }
}
