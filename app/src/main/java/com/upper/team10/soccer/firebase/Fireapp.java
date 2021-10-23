package com.upper.team10.soccer.firebase;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.upper.team10.soccer.Testing.ConnectivityReceiver;

/**
 * Created by USER on 10/13/2017.
 */

public class Fireapp extends Application {
    public boolean connected = false;
    private static Fireapp mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
        mInstance = this;

    }
    public static synchronized Fireapp getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
