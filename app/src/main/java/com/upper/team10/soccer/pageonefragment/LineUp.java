package com.upper.team10.soccer.pageonefragment;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.upper.team10.soccer.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LineUp extends Fragment {
    private List<String> list = new ArrayList<>();
    private TextView subhomename, subawayname;
    private TextView home1, home2, home3, home4, home5, home6, home7, home8, home9, home10, home11;
    private TextView away1, away2, away3, away4, away5, away6, away7, away8, away9, away10, away11;
    private TextView homesub1, homesub2, homesub3, homesub4, homesub5;
    private TextView awaysub1, awaysub2, awaysub3, awaysub4, awaysub5;
    private String parent, position;
    private String matchhomename, matchawayname;
    private DatabaseReference mdRef;
    private DatabaseReference databasehome, databaseaway;
    private DatabaseReference databasehomesub, databaseawaysub;
    private LinearLayout linearlayoutone, linearlayouttwo;
    private String gk = "GK ";
    private String df = "DF ";
    private String mf = "MF ";
    private String fw = "FW ";
    private ImageView imagelineup;
    private Button retry;
    private int colorone, colortwo;
    private int rad;
    private View v;
    private CardView cardviewonecolor, cardviewonecolors, cardviewtwocolor, cardviewtwocolors;
    private DatabaseReference mChangingLayout;
    private int images[];
    private String realhome, realaway;

    public LineUp() {

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        FloatingActionButton fab1 = (FloatingActionButton) getActivity().findViewById(R.id.fab1);
        fab1.setVisibility(View.GONE);
        Bundle bundle = getArguments();
        parent = bundle.getString("parent");
        position = bundle.getString("position");
        changingLayout(Integer.parseInt(bundle.getString("data")), container, inflater);
        givingid(v);
        return v;
    }

    private void changingLayout(int rad, ViewGroup container, LayoutInflater inflater) {
        switch (rad) {
            case 0:
                v = inflater.inflate(R.layout.fragment_line_up, container, false);
                break;

            case 1:
                v = inflater.inflate(R.layout.fragment_line_up_two, container, false);
                break;

            case 2:
                v = inflater.inflate(R.layout.fragment_line_up_three, container, false);
                break;
            case 3:
                v = inflater.inflate(R.layout.fragment_line_up_four, container, false);
                break;
            case 4:
                v = inflater.inflate(R.layout.fragment_line_up_five, container, false);
                break;
            default:
                v = inflater.inflate(R.layout.fragment_line_up, container, false);


        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mdRef = FirebaseDatabase.getInstance().getReference().child(parent).child(position);
        mdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                matchhomename = dataSnapshot.child("matchhomename").getValue(String.class);
                matchawayname = dataSnapshot.child("matchawayname").getValue(String.class);
                realhome = testing(matchhomename);
                realaway = testing(matchawayname);
                subhomename.setText(realhome);
                subawayname.setText(realaway);
                databasehome = FirebaseDatabase.getInstance().getReference().child(realhome);
                databasehome.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                        home1.setText(map.get("gk"));
                        home2.setText(map.get("df1"));
                        home3.setText(map.get("df2"));
                        home4.setText(map.get("df3"));
                        home5.setText(map.get("df4"));
                        home6.setText(map.get("mf1"));
                        home7.setText(map.get("mf2"));
                        home8.setText(map.get("fw1"));
                        home9.setText(map.get("fw2"));
                        home10.setText(map.get("mf3"));
                        home11.setText(map.get("mf4"));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                databaseaway = FirebaseDatabase.getInstance().getReference().child(realaway);
                databaseaway.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, String> map1 = (Map<String, String>) dataSnapshot.getValue();
                        away1.setText(map1.get("gk"));
                        away2.setText(map1.get("df1"));
                        away3.setText(map1.get("df2"));
                        away4.setText(map1.get("df3"));
                        away5.setText(map1.get("df4"));
                        away6.setText(map1.get("mf1"));
                        away7.setText(map1.get("mf2"));
                        away8.setText(map1.get("fw1"));
                        away9.setText(map1.get("fw2"));
                        away10.setText(map1.get("mf3"));
                        away11.setText(map1.get("mf4"));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                databasehomesub = FirebaseDatabase.getInstance().getReference().child(realhome + "Sub");
                databasehomesub.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, String> map2 = (Map<String, String>) dataSnapshot.getValue();
                        homesub1.setText(gk + map2.get("gk"));
                        homesub2.setText(df + map2.get("df1"));
                        homesub3.setText(df + map2.get("df2"));
                        homesub4.setText(mf + map2.get("mf"));
                        homesub5.setText(fw + map2.get("fw"));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                databaseawaysub = FirebaseDatabase.getInstance().getReference().child(realaway + "Sub");
                databaseawaysub.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                        awaysub1.setText(gk + map.get("gk"));
                        awaysub2.setText(df + map.get("df1"));
                        awaysub3.setText(df + map.get("df2"));
                        awaysub4.setText(mf + map.get("mf"));
                        awaysub5.setText(fw + map.get("fw"));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void givingid(View view) {
        home1 = (TextView) view.findViewById(R.id.home1);
        home2 = (TextView) view.findViewById(R.id.home2);
        home3 = (TextView) view.findViewById(R.id.home3);
        home4 = (TextView) view.findViewById(R.id.home4);
        home5 = (TextView) view.findViewById(R.id.home5);
        home6 = (TextView) view.findViewById(R.id.home6);
        home7 = (TextView) view.findViewById(R.id.home7);
        home8 = (TextView) view.findViewById(R.id.home8);
        home9 = (TextView) view.findViewById(R.id.home9);
        home10 = (TextView) view.findViewById(R.id.home10);
        home11 = (TextView) view.findViewById(R.id.home11);
        away1 = (TextView) view.findViewById(R.id.away1);
        away2 = (TextView) view.findViewById(R.id.away2);
        away3 = (TextView) view.findViewById(R.id.away3);
        away4 = (TextView) view.findViewById(R.id.away4);
        away5 = (TextView) view.findViewById(R.id.away5);
        away6 = (TextView) view.findViewById(R.id.away6);
        away7 = (TextView) view.findViewById(R.id.away7);
        away8 = (TextView) view.findViewById(R.id.away8);
        away9 = (TextView) view.findViewById(R.id.away9);
        away10 = (TextView) view.findViewById(R.id.away10);
        away11 = (TextView) view.findViewById(R.id.away11);
        homesub1 = (TextView) view.findViewById(R.id.homesub1);
        homesub2 = (TextView) view.findViewById(R.id.homesub2);
        homesub3 = (TextView) view.findViewById(R.id.homesub3);
        homesub4 = (TextView) view.findViewById(R.id.homesub4);
        homesub5 = (TextView) view.findViewById(R.id.homesub5);
        awaysub1 = (TextView) view.findViewById(R.id.awaysub1);
        awaysub2 = (TextView) view.findViewById(R.id.awaysub2);
        awaysub3 = (TextView) view.findViewById(R.id.awaysub3);
        awaysub4 = (TextView) view.findViewById(R.id.awaysub4);
        awaysub5 = (TextView) view.findViewById(R.id.awaysub5);
        subhomename = (TextView) view.findViewById(R.id.subhomename);
        subawayname = (TextView) view.findViewById(R.id.subawayname);
        cardviewonecolor = (CardView) view.findViewById(R.id.cardviewonecolor);
        cardviewonecolors = (CardView) view.findViewById(R.id.cardviewonecolors);
        cardviewtwocolor = (CardView) view.findViewById(R.id.cardviewtwocolor);
        cardviewtwocolors = (CardView) view.findViewById(R.id.cardviewtwocolors);
        imagelineup = (ImageView) view.findViewById(R.id.imagelineup);
        images = new int[]{R.drawable.lineup1, R.drawable.lineup2, R.drawable.lineup3, R.drawable.line3, R.drawable.line4};


    }

    private String testing(String matchhomename) {
        String result = null;
        int aye, chin, gfa, hanthar, magwe, naypyi, rakhine, shan, southern, yadanarbon, yangon, zwekapin, ctstar, ctyan, dag, mah, mawya,
                mya, phoe, pong, sil, than, uni, singa, thai, phil, indo, viet, malay, cam, myan, uzbe, dpr, timor, jap, bru, loa;
        String[] arrayname = {"Ayeyawaddy", "ChinUnited", "Gfa", "Hantharwaddy", "Magwe", "NayPyiTaw", "Rakhine", "Shan", "SouthernMyanmar", "Yadanarbon",
                "Yangon", "Zwekapin"
                , "CityStars", "CityYangon", "Dagon", "Mahar", "Mawyawady", "Myawady", "Phoenix", "PongGan", "SilverStars", "Thanlwin", "UniversityFC"
                , "Singapore", "Thailand", "Phillipines", "Indonesia", "Vietnam", "Malaysia", "Cambodia", "Myanmar", "Uzbekistan", "DPRKorea", "TimorLeste", "Japan"
                , "Brunei", "Loas"


        };
        String home = matchhomename.replaceAll("\\s", "").toLowerCase();
        aye = home.indexOf("aye");
        chin = home.indexOf("chin");
        gfa = home.indexOf("gfa");
        hanthar = home.indexOf("han");
        magwe = home.indexOf("mag");
        naypyi = home.indexOf("naypyi");
        rakhine = home.indexOf("rak");
        shan = home.indexOf("shan");
        southern = home.indexOf("south");
        yadanarbon = home.indexOf("yadanar");
        yangon = home.indexOf("yang");
        zwekapin = home.indexOf("zweka");
        ctstar = home.indexOf("cityst");
        ctyan = home.indexOf("cityyan");
        dag = home.indexOf("dag");
        mah = home.indexOf("mah");
        mawya = home.indexOf("maw");
        mya = home.indexOf("mya");
        phoe = home.indexOf("phoe");
        pong = home.indexOf("pong");
        sil = home.indexOf("sil");
        than = home.indexOf("than");
        uni = home.indexOf("uni");
        singa = home.indexOf("singa");
        thai = home.indexOf("thai");
        phil = home.indexOf("phil");
        indo = home.indexOf("indo");
        viet = home.indexOf("viet");
        malay = home.indexOf("malay");
        cam = home.indexOf("cam");
        myan = home.indexOf("myan");
        uzbe = home.indexOf("uzbe");
        dpr = home.indexOf("dpr");
        timor = home.indexOf("timor");
        jap = home.indexOf("jap");
        bru = home.indexOf("bru");
        loa = home.indexOf("loa");
        if (aye == 0) {
            result = arrayname[0];
        } else if (chin == 0) {
            result = arrayname[1];
        } else if (hanthar == 0) {
            result = arrayname[3];
        } else if (gfa == 0) {
            result = arrayname[2];
        } else if (magwe == 0) {
            result = arrayname[4];

        } else if (naypyi == 0) {
            result = arrayname[5];

        } else if (rakhine == 0) {
            result = arrayname[6];

        } else if (shan == 0) {
            result = arrayname[7];

        } else if (southern == 0) {
            result = arrayname[8];
        } else if (yadanarbon == 0) {
            result = arrayname[9];
        } else if (yangon == 0) {
            result = arrayname[10];
        } else if (zwekapin == 0) {
            result = arrayname[11];
        } else if (ctstar == 0) {
            result = arrayname[12];
        } else if (ctyan == 0) {
            result = arrayname[13];
        } else if (dag == 0) {
            result = arrayname[14];
        } else if (mah == 0) {
            result = arrayname[15];
        } else if (mawya == 0) {
            result = arrayname[16];
        } else if (mya == 0) {
            result = arrayname[30];
        } else if (phoe == 0) {
            result = arrayname[18];
        } else if (pong == 0) {
            result = arrayname[19];
        } else if (sil == 0) {
            result = arrayname[20];
        } else if (than == 0) {
            result = arrayname[21];
        } else if (uni == 0) {
            result = arrayname[22];
        } else if (singa == 0) {
            result = arrayname[23];

        } else if (thai == 0) {
            result = arrayname[24];
        } else if (phil == 0) {
            result = arrayname[25];
        } else if (indo == 0) {
            result = arrayname[26];
        } else if (viet == 0) {
            result = arrayname[27];
        } else if (malay == 0) {
            result = arrayname[28];
        } else if (cam == 0) {
            result = arrayname[29];
        } else if (myan == 0) {
            result = arrayname[30];
        } else if (uzbe == 0) {
            result = arrayname[31];
        } else if (dpr == 0) {
            result = arrayname[32];
        } else if (timor == 0) {
            result = arrayname[33];
        } else if (jap == 0) {
            result = arrayname[34];
        } else if (bru == 0) {
            result = arrayname[35];
        } else if (loa == 0) {
            result = arrayname[36];
        }
        return result;
    }


}
