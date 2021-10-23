package com.upper.team10.soccer.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.upper.team10.soccer.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class about_activity extends AppCompatActivity {
    private Toolbar toolbar;
    private CircleImageView aungthu,waiwai,nankhine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_activity);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("About");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        aungthu= (CircleImageView) findViewById(R.id.aungthuimageview);
        waiwai= (CircleImageView) findViewById(R.id.waiwaiimageview);
        nankhine= (CircleImageView) findViewById(R.id.nankhineimageview);
        TextView textview= (TextView) findViewById(R.id.nan);
        aungthu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","newarunkatwal@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello");
                startActivity(emailIntent);
            }
        });
        waiwai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","waiwaitheint@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello");
                startActivity(emailIntent);            }
        });
        nankhine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","nankhaingzarthinn@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello");
                startActivity(emailIntent);
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
}
