package com.upper.team10.soccer.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.upper.team10.soccer.R;

public class Login extends AppCompatActivity   {
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    private SignInButton signinbuttongoogle;
    public  GoogleApiClient mGoogleApiClient;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ProgressDialog mDialog;
    private TextInputEditText email, password;
    private FloatingActionButton login;
    private CardView register;
    private ProgressDialog mProgress;
    private  Pair<View,String> pair;
    private String name,url;
    private DatabaseReference mdRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_login);
        givingId();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)

                .requestIdToken(getString(R.string.default_web_client_id))

                .requestEmail()

                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)

                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {

                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {


                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)

                .build();

        signinbuttongoogle = (SignInButton) findViewById(R.id.signinbuttongoogle);

        signinbuttongoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull  FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(Login.this, Main.class);
                    if(!TextUtils.isEmpty(name)){
                        intent.putExtra("name",name);
                        intent.putExtra("url",url);
                    }

                    startActivity(intent);
                }



            }
        };
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(haveNetworkConnection()){
                    if(TextUtils.isEmpty(email.getText().toString())||TextUtils.isEmpty(password.getText().toString())){

                        Toast.makeText(Login.this,"Email and Password should not be empty", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        mProgress.show();

                        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    mProgress.dismiss();
                                    email.setText("");
                                    password.setText("");
                                } else {
                                    Toast.makeText(Login.this, "Login Fail...", Toast.LENGTH_SHORT).show();
                                    mProgress.hide();
                                }
                            }
                        });
                    }
                }
                else{
                    Toast.makeText(Login.this, "No Connection", Toast.LENGTH_SHORT).show();
                }


            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,SignUp.class);
                startActivity(intent);
            }
        });

    }

    private void givingId() {
        mProgress = new ProgressDialog(Login.this);
        mProgress.setTitle("Sign In");
        mProgress.setMessage("Loading . . .");
        mProgress.setCanceledOnTouchOutside(false);
        pair=Pair.create(findViewById(R.id.imagelogo),"mylogo");
        email = (TextInputEditText) findViewById(R.id.email);
        password = (TextInputEditText) findViewById(R.id.password);
        login = (FloatingActionButton) findViewById(R.id.login);
        register = (CardView) findViewById(R.id.register);
        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Signing");
        mDialog.setMessage("Loading . . .");
        mAuth = FirebaseAuth.getInstance();
        ImageView imageView9= (ImageView) findViewById(R.id.imageView9);
        Picasso.with(getApplicationContext()).load(R.drawable.login).into(imageView9);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                gettingProfile(account);
                firebaseAuthWithGoogle(account);
            } else {
                mDialog.hide();
                Toast.makeText(this, "Sign in Fail", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void gettingProfile(GoogleSignInAccount account) {
        name=account.getDisplayName();
        url=account.getPhotoUrl().toString();
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mDialog.dismiss();
                        } else {
                            Toast.makeText(Login.this, "Sign in Fail", Toast.LENGTH_SHORT).show();
                            mDialog.hide();
                        }

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





}