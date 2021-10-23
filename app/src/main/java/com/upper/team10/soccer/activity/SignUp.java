package com.upper.team10.soccer.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.upper.team10.soccer.R;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
  private TextInputEditText username,password,realusername,passwordretype;
    private FloatingActionButton mButton;
    private FirebaseAuth mAuth;
    private String name;
    private String url;
    private DatabaseReference puttingdataRef;
    private ProgressDialog mDialog;
    private String textone,texttwo;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_signup);
        mDialog=new ProgressDialog(this);
        mDialog.setTitle("Sign Up");
        mDialog.setMessage("Creating Account");
        ImageView imageView9= (ImageView) findViewById(R.id.imageView9);
        Picasso.with(getApplicationContext()).load(R.drawable.login).into(imageView9);
        puttingdataRef= FirebaseDatabase.getInstance().getReference().child("Profiles");
        username= (TextInputEditText) findViewById(R.id.signusername);
        password= (TextInputEditText) findViewById(R.id.signpassword);
        passwordretype= (TextInputEditText) findViewById(R.id.signpasswordretype);
        realusername= (TextInputEditText) findViewById(R.id.realusername);
        mButton= (FloatingActionButton) findViewById(R.id.signupbutton);
        mAuth=FirebaseAuth.getInstance();
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username_string=username.getText().toString();
                final String realuser_name=realusername.getText().toString();
                if(haveNetworkConnection()){
                    if(!(TextUtils.isEmpty(username_string)||TextUtils.isEmpty(password.getText().toString())))
                    {
                        String textone=password.getText().toString();
                        String texttwo=passwordretype.getText().toString();
                        boolean finalresult=checkpassword(textone,texttwo);
                        if(finalresult){
                            mDialog.setCanceledOnTouchOutside(false);
                            mDialog.show();
                            mAuth.createUserWithEmailAndPassword(username_string,password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        final DatabaseReference mRef=puttingdataRef.child(mAuth.getCurrentUser().getUid());
                                        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Map<String,String> map=new HashMap<>();
                                                map.put("name",realuser_name);
                                                map.put("balance","5000");
                                                map.put("image","https://firebasestorage.googleapis.com/v0/b/soccer-982f0.appspot.com/o/advertisementpic%2Fdefault-avatar.png?alt=media&token=4142d167-5df7-4a9c-baad-895e1d8736ca");
                                                mRef.setValue(map);
                                                mDialog.dismiss();
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Toast.makeText(SignUp.this, "Sing Up Fail", Toast.LENGTH_SHORT).show();
                                                mDialog.dismiss();
                                            }
                                        });
                                    }
                                    else{
                                        Toast.makeText(SignUp.this, "Invalid Email.", Toast.LENGTH_SHORT).show();
                                        mDialog.dismiss();

                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(SignUp.this, "Password must be matched", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        Toast.makeText(SignUp.this, "Please fill all data", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(SignUp.this, "No Connection", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private boolean checkpassword(String textone, String texttwo) {
        if(textone.length()==texttwo.length()){
            if(textone.contentEquals(texttwo)){
                return true;
            }
            else{
                return false;
            }
        }
        return false;
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
    protected void onDestroy() {
        super.onDestroy();
        if(mDialog!=null&&mDialog.isShowing()){
            mDialog.dismiss();
        }
    }
}
