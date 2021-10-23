package com.upper.team10.soccer.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.test.suitebuilder.annotation.Suppress;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.data.DataBufferObserverSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.upper.team10.soccer.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class Setting extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText changename;
    private CardView changesubmit;
    private DatabaseReference mdRef;
    private DatabaseReference mdRefName;
    private String realname;
    private FirebaseAuth mAuth;
    private String databasename;
    private DatabaseReference balanceRef;
    private DatabaseReference storebalRef;
    private TextView balance, balancename;
    private CardView balanceTopup;
    private List<String> moneyList = new ArrayList<>();
    private Map<String, String> moneyMap = new HashMap<>();
    private int code = 0;
    private CircleImageView changeimage;
    private MaterialDialog mmDialog;
    private static final int change_image_code = 100;
    private StorageReference mStorageRef;
    private String downloadString;
    private MaterialDialog mChangingProfile;
    private DatabaseReference mRefImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mAuth = FirebaseAuth.getInstance();
        givingId();
        settingBalance();
        gettingDialog();
        balanceTopup.setOnClickListener(this);
        mdRef = FirebaseDatabase.getInstance().getReference().child("Profiles");
        mdRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())) {
                    DatabaseReference database = mdRef.child(mAuth.getCurrentUser().getUid());
                    database.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            String result = dataSnapshot.child("name").getValue(String.class);
                            final String url = dataSnapshot.child("image").getValue(String.class);
                            changename.setText(result);
                            balancename.setText(result);
                            Picasso.with(getApplicationContext()).load(url).networkPolicy(NetworkPolicy.OFFLINE)
                                    .placeholder(R.drawable.default_avatar).into(changeimage, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso.with(getApplicationContext()).load(url).placeholder(R.drawable.default_avatar).into(changeimage);
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        changesubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realname = changename.getText().toString();
                boolean result = haveNetworkConnection();
                if (result) {
                    final DatabaseReference databaseReference = mdRef.child(mAuth.getCurrentUser().getUid()).child("name");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            databaseReference.setValue(realname);


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    settingAsynTask();


                } else {
                    Toast.makeText(Setting.this, "No Connection", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void gettingDialog() {
        mChangingProfile = new MaterialDialog.Builder(Setting.this)
                .content("Loading")
                .progress(true, 0)
                .canceledOnTouchOutside(false)
                .build();
    }

    private void settingBalance() {
        storebalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String bal = dataSnapshot.child("balance").getValue(String.class);
                balance.setText(bal);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void givingId() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Wallet");
        changesubmit = (CardView) findViewById(R.id.changesubmit);
        changename = (TextInputEditText) findViewById(R.id.changename);
        balance = (TextView) findViewById(R.id.balance);
        mStorageRef = FirebaseStorage.getInstance().getReference().child("UserProfiles");
        changeimage = (CircleImageView) findViewById(R.id.changeimage);
        changeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changingImage();
            }
        });
        balanceTopup = (CardView) findViewById(R.id.balanceTopup);
        balancename = (TextView) findViewById(R.id.balancename);
        storebalRef = FirebaseDatabase.getInstance().getReference().child("Profiles").child(mAuth.getCurrentUser().getUid());
        balanceRef = FirebaseDatabase.getInstance().getReference().child("Coin");
        balanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Long value = snap.getValue(Long.class);
                    String key = snap.getKey().toString();
                    moneyList.add(String.valueOf(value));
                    moneyMap.put(String.valueOf(value), key);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void changingImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, change_image_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == change_image_code && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            mChangingProfile.show();
            StorageReference stRef = mStorageRef.child(uri.getLastPathSegment());
            stRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests")
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    downloadString = downloadUri.toString();
                    loadingPic(downloadString);
                    mChangingProfile.dismiss();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Setting.this, "Error Uploading Profile Picture", Toast.LENGTH_SHORT).show();
                    mChangingProfile.dismiss();
                }
            });
        }
    }

    private void loadingPic(final String downloadString) {

        //Picasso.with(getApplicationContext()).load(downloadString).into();
        mRefImage = FirebaseDatabase.getInstance().getReference().child("Profiles");
        final DatabaseReference databaseReference = mRefImage.child(mAuth.getCurrentUser().getUid()).child("image");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                databaseReference.setValue(downloadString);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
    public void onClick(View v) {
        MaterialDialog materialDialog = new MaterialDialog.Builder(this)
                .title("Topup")
                .inputRange(8,8)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input("Pin", "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        for (String s : moneyList) {
                            if (s.equals(input.toString())) {
                                String key = moneyMap.get(s);
                                code = Integer.parseInt(key.trim());
                                balanceRef.child(key).removeValue();

                            }

                        }
                        if (code >= 1 && code <= 1000) {
                            String money = balance.getText().toString();
                            int money_int = Integer.parseInt(money);
                            final int result = money_int + 10000;
                            final DatabaseReference childstore = storebalRef.child("balance");
                            childstore.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    childstore.setValue(String.valueOf(result));
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            settingAsynTask();
                        } else if (code < 2001 && code > 1000) {
                            String money = balance.getText().toString();
                            int money_int;
                            if (money != null) {
                                money_int = Integer.parseInt(money);

                            } else {
                                money_int = 0;
                            }
                            final int result = money_int + 20000;
                            final DatabaseReference childstore = storebalRef.child("balance");
                            childstore.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    childstore.setValue(String.valueOf(result));
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            settingAsynTask();
                        } else {
                            Toast.makeText(Setting.this, "Wrong Pin", Toast.LENGTH_SHORT).show();
                        }
                        recreate();
                    }
                })
                .negativeText("Cancel")
                .build();
        materialDialog.show();

    }

    private void settingAsynTask() {
        AsyncTask<String, String, String> syntask =
                new AsyncTask<String, String, String>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        mmDialog = new MaterialDialog.Builder(Setting.this)
                                .title("Changing")
                                .progress(true, 0)
                                .cancelable(false)
                                .canceledOnTouchOutside(false)
                                .content("Please Wait . .  .")
                                .build();
                        mmDialog.show();
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
                            finish();
                        }
                    }
                };
        syntask.execute();
    }

    @Override
    public void onPause() {
        super.onPause();

        if ((mmDialog != null) && mmDialog.isShowing())
            mmDialog.dismiss();
        mmDialog = null;
    }


}
