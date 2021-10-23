package com.upper.team10.soccer.pageonefragment;


import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.squareup.picasso.Picasso;
import com.upper.team10.soccer.Model.Match;
import com.upper.team10.soccer.R;
import com.upper.team10.soccer.activity.Main;
import com.upper.team10.soccer.activity.SectionActivity;
import com.upper.team10.soccer.activity.Setting;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.upper.team10.soccer.R.id.section_dialog;
import static com.upper.team10.soccer.R.id.section_edittext;

public class Ticket extends Fragment {
    private CardView cardviewcall;
    private CardView cardviewbuy;
    private TextInputEditText buyemail;
    private CardView buycomfirm, buycancel;
    private DatabaseReference mdRef,mTicketRef;
    private FirebaseAuth mAuth;
    private String parent, position;
    private List<String> list = new ArrayList<>();
    private List<String> listadult = new ArrayList<>();
    private CardView choosestadium, chooseadult;
    private TextView stadiumsection, adult;
    private String stdsection, stdname, stdadult, time, uid, name, email, matchname, matchaway, matchtype, homelogo, awaylogo, date;
    private DatabaseReference databaseprofile;
    private String priceList, pricestdsec, pricestdadult;
    private MaterialDialog dilogdialog;
    private MaterialDialog mDialog;
    private String mBalance,mName,mImage;
    private View v;
    private DatabaseReference mBalanceRef;
    private int x;
    private ImageView sectionA,sectionB,sectionC,sectionD;
    private DatabaseReference mdatabaseticketSingleDetails;
    private   Map<String, String> ondatamap;

    public Ticket() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        FloatingActionButton fab1= (FloatingActionButton) getActivity().findViewById(R.id.fab1);
        fab1.setVisibility(View.INVISIBLE);
        if (bundle != null) {
            parent = bundle.getString("parent");
            position = bundle.getString("position");
            matchname = bundle.getString("hometext");
            matchaway = bundle.getString("awaytext");
            homelogo = bundle.getString("homeimage");
            awaylogo = bundle.getString("awayimage");
            matchtype = bundle.getString("matchtype");
            date = bundle.getString("date");


        }
        else {
            Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
        }
        if(haveNetworkConnection()){
            if(parent.equals("FinishedMatches")){
                v=inflater.inflate(R.layout.no_content_available,container,false);
            }
            else{
                if(checkingData()){
                    v = inflater.inflate(R.layout.fragment_ticket, container, false);
                    working();
                }
                else{
                    v=inflater.inflate(R.layout.no_content_available,container,false);

                }

            }

        }
        else{
             v = inflater.inflate(R.layout.finished_for_layout, container, false);

        }

        return v;
    }

    private boolean checkingData() {
        if(matchname.equalsIgnoreCase("Myanmar")||matchname.equalsIgnoreCase("Chin Utd")||matchname.equalsIgnoreCase("GFA")||matchname.equalsIgnoreCase("Yadanarbon")||matchname.equalsIgnoreCase("Nay Pyi Taw")||matchname.equalsIgnoreCase("Ayeyarwady")||matchname.equalsIgnoreCase("Zwekapin")||matchname.equalsIgnoreCase("Southern Myanmar")||matchname.equalsIgnoreCase("Rakhine Utd")||matchname.equalsIgnoreCase("Magwe")||matchname.equalsIgnoreCase("Yangon Utd")||matchname.equalsIgnoreCase("Shan Utd")||matchname.equalsIgnoreCase("Hantharwady")||matchname.equalsIgnoreCase("Ayeyarwady")||matchname.equalsIgnoreCase("Malaysia")||matchname.equalsIgnoreCase("Thailand")||matchname.equalsIgnoreCase("Indonesia")){
            return true;
        }
        return false;
    }

    private void working() {
        givingId();
         sectionA = (ImageView) v.findViewById(R.id.sectionA);
        sectionB= (ImageView) v.findViewById(R.id.sectionB);
        sectionC= (ImageView) v.findViewById(R.id.sectionC);
        sectionD= (ImageView) v.findViewById(R.id.sectionD);
     ImageView  imageView2= (ImageView) v.findViewById(R.id.imageView2);
        Picasso.with(getContext()).load(R.drawable.footballstat).into(imageView2);
        puttingData();
        mDialog = new MaterialDialog.Builder(getActivity())
                .title("Buying Ticket")
                .content("Please Wait...")
                .canceledOnTouchOutside(false)
                .progress(true, 0)
                .build();
      /*  mDialog.setMessage("Buying");
        mDialog.setCanceledOnTouchOutside(false);*/
        databaseprofile = FirebaseDatabase.getInstance().getReference().child("Profiles");
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        mdRef = FirebaseDatabase.getInstance().getReference().child("Profiles").child(mAuth.getCurrentUser().getUid());
        list.add("Section A");
        list.add("Section B");
        list.add("Section C");
        list.add("Section D");
        for (int i = 1; i <= 10; i++) {
            listadult.add(String.valueOf(i));
        }
        cardviewcall = (CardView) v.findViewById(R.id.carviewcall);
        cardviewbuy = (CardView) v.findViewById(R.id.cardviewbuy);
        if (parent.equals("FinishedMatches")) {
            cardviewcall.setVisibility(View.INVISIBLE);
            cardviewbuy.setVisibility(View.INVISIBLE);
            sectionA.setEnabled(false);
            sectionB.setEnabled(false);
            sectionC.setEnabled(false);
            sectionD.setEnabled(false);
        }
        cardviewcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+"09973437020"));
                startActivity(intent);
            }
        });
        cardviewbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dilogdialog = new MaterialDialog.Builder(getActivity())
                        .customView(R.layout.custombuylayout, false)
                        .canceledOnTouchOutside(false)
                        .build();
                View customview = dilogdialog.getCustomView();
                buycancel = (CardView) customview.findViewById(R.id.buycancel);
                buycomfirm = (CardView) customview.findViewById(R.id.buycomfirm);
                stadiumsection = (TextView) customview.findViewById(R.id.stadiumSection);
                adult = (TextView) customview.findViewById(R.id.adult);
                buyemail = (TextInputEditText) customview.findViewById(R.id.buyemail);
                choosestadium = (CardView) customview.findViewById(R.id.choosestadium);
                chooseadult = (CardView) customview.findViewById(R.id.chooseadult);
                if (!(((AppCompatActivity) v.getContext()).isFinishing())) {
                    dilogdialog.show();
                }
                buycancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dilogdialog.dismiss();

                    }
                });

                buycomfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean result = haveNetworkConnection();
                        if (result) {

                            String one = stadiumsection.getText().toString();
                            String two = adult.getText().toString();
                            String three = buyemail.getText().toString();
                            Log.e("arun", one + " " + two);
                            String oneresult = checktwo(one.toLowerCase());
                            if (oneresult.equals("0")) {
                                one = null;
                            }
                            Log.e("arun", oneresult);
                            String tworesult = checkthree(two.toLowerCase());
                            if (tworesult.equals("0")) {
                                two = null;
                            }
                            Log.e("arun", tworesult);
                            if (TextUtils.isEmpty(three) || TextUtils.isEmpty(one) || TextUtils.isEmpty(two)) {

                                Toast.makeText(getActivity(), "Phone,Stadium and Seat counts should not be empty", Toast.LENGTH_SHORT).show();

                            }
                            else {
                                if(three.length()==11) {


                                    if (checkemail(buyemail.getText().toString())) {
                                        String price_result = calculating(two, one);
                                        String final_Result= calculate(price_result, mBalance);
                                        if(final_Result.equals("Ok")){
                                            doing_cuttongTicket(price_result);
                                        }
                                        else{
                                            new MaterialDialog.Builder(getActivity())
                                                    .title("Alert")
                                                    .content(Html.fromHtml(getString(R.string.title)))                                                    .contentColor(getResources().getColor(R.color.colorPrimary))
                                                    .positiveText("Ok")
                                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            startActivity(new Intent(getActivity(),Setting.class));
                                                        }
                                                    })
                                                    .negativeText("Cancel")
                                                    .show();
                                        }
                                    }
                                    else {
                                        Toast.makeText(getActivity(), "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Toast.makeText(getActivity(), "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                                }
                            }


                        } else {
                            Toast.makeText(getActivity(), "Please check your internet connection ", Toast.LENGTH_LONG).show();
                        }

                    }
                });
                choosestadium.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new MaterialDialog.Builder(getActivity())
                                .title("Choose Section")
                                .items(list)
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                        stadiumsection.setText(text);
                                        stadiumsection.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        stdsection = text.toString();
                                        Calendar canlender = Calendar.getInstance();
                                        time = canlender.getTime().toString();
                                    }
                                })
                                .show();
                    }
                });
                chooseadult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new MaterialDialog.Builder(getActivity())
                                .title("Choose Seat Counts")
                                .items(listadult)
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                        adult.setText(text);
                                        adult.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        stdadult = text.toString();
                                        stdname = "Thuwanna Stadium";
                                    }
                                })
                                .show();
                    }
                });

            }
        });
    }

    private void givingId() {
        mAuth = FirebaseAuth.getInstance();
        mBalanceRef = FirebaseDatabase.getInstance().getReference().child("Profiles").child(mAuth.getCurrentUser().getUid());
        mTicketRef = FirebaseDatabase.getInstance().getReference().child("Ticketdetails").child(mAuth.getCurrentUser().getUid());
        mBalanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mBalance = dataSnapshot.child("balance").getValue(String.class);
                mName = dataSnapshot.child("name").getValue(String.class);
                mImage=dataSnapshot.child("image").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void doing_cuttongTicket(final String cost) {
        new MaterialDialog.Builder(getActivity())
                .title("Seat Price is " + cost + " MMK")
                .content("Account Balance is " + mBalance + " MMK" + "\n" + "Are you sure want to buy this ticket? ")
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
                            map.put("name", mName);
                            map.put("image",mImage);
                            mdRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                                    ondatamap.put("stdadult", adult.getText().toString());
                                    ondatamap.put("time", time);
                                    ondatamap.put("uid", mAuth.getCurrentUser().getUid().toString());
                                    ondatamap.put("date", date);
                                    ondatamap.put("name", mName);
                                    ondatamap.put("email",buyemail.getText().toString() );
                                    ondatamap.put("matchname", matchname);
                                    ondatamap.put("matchaway", matchaway);
                                    ondatamap.put("matchtype", matchtype);
                                    ondatamap.put("homelogo", homelogo);
                                    ondatamap.put("awaylogo", awaylogo);
                                    ticketdetails.setValue(ondatamap);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            x=Integer.parseInt(adult.getText().toString());
                            String query=matchname+matchaway+time+mAuth.getCurrentUser().getUid().toString();
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

                            dilogdialog.dismiss();

                        }

                })
                .negativeText("Cancel")
                .show();
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
                            createNoti();
                            Toast.makeText(getActivity(), "Thanks You , Have a nice day for the Match Sir", Toast.LENGTH_LONG).show();


                        }
                    }
                };
        syntask.execute();
    }

    private void puttingData() {
        sectionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),SectionActivity.class);
                intent.putExtra("name","Thuwanna Stadium");
                intent.putExtra("section","Section A");
                intent.putExtra("price","3000");
                intent.putExtra("location",matchtype);
                intent.putExtra("match",matchname+" Vs "+matchaway);
                intent.putExtra("matchhomename",matchname);
                intent.putExtra("matchawayname",matchaway);
                intent.putExtra("matchdate",date);
                intent.putExtra("homelogo",homelogo);
                intent.putExtra("awaylogo",awaylogo);
                startActivity(intent);
            }
        });
        sectionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),SectionActivity.class);
                intent.putExtra("name","Thuwanna Stadium");
                intent.putExtra("section","Section B");
                intent.putExtra("price","2000");
                intent.putExtra("location",matchtype);
                intent.putExtra("match",matchname+" Vs "+matchaway);
                intent.putExtra("matchhomename",matchname);
                intent.putExtra("matchawayname",matchaway);
                intent.putExtra("matchdate",date);
                intent.putExtra("homelogo",homelogo);
                intent.putExtra("awaylogo",awaylogo);
                startActivity(intent);
            }
        });
        sectionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),SectionActivity.class);
                intent.putExtra("name","Thuwanna Stadium");
                intent.putExtra("section","Section C");
                intent.putExtra("price","2000");
                intent.putExtra("location",matchtype);
                intent.putExtra("match",matchname+" Vs "+matchaway);
                intent.putExtra("matchdate",date);
                intent.putExtra("matchhomename",matchname);
                intent.putExtra("matchawayname",matchaway);
                intent.putExtra("homelogo",homelogo);
                intent.putExtra("awaylogo",awaylogo);
                startActivity(intent);
            }
        });
        sectionD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),SectionActivity.class);
                intent.putExtra("name","Thuwanna Stadium");
                intent.putExtra("section","Section D");
                intent.putExtra("price","2000");
                intent.putExtra("location",matchtype);
                intent.putExtra("matchhomename",matchname);
                intent.putExtra("matchawayname",matchaway);
                intent.putExtra("match",matchname+" Vs "+matchaway);
                intent.putExtra("matchdate",date);
                intent.putExtra("homelogo",homelogo);
                intent.putExtra("awaylogo",awaylogo);
                startActivity(intent);
            }
        });

    }
    private  String calculate(String cost, String mBalance) {
        int cost_int = Integer.parseInt(cost);
        int balance = Integer.parseInt(mBalance);
        if (cost_int > balance) {
            return "In";
        }
        return "Ok";

    }

    private String calculating(String pricestdadult, String pricestdsec) {
        int price;
        int result;
        if (pricestdsec.equals("Section A")) {
            price = 3000;
        } else {
            price = 2000;
        }
        result = price * Integer.parseInt(pricestdadult);
        return String.valueOf(result);
    }

    private String checkthree(String three) {
        int result = three.indexOf("cho");
        return String.valueOf(result);
    }

    private String checktwo(String two) {
        int result = two.indexOf("cho");
        return String.valueOf(result);
    }

    private boolean checkemail(String email) {
      if(!TextUtils.isEmpty(email)){

            return true;
        }

        return false;


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dilogdialog != null && dilogdialog.isShowing()) {
            dilogdialog.dismiss();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (dilogdialog != null && dilogdialog.isShowing()) {
            dilogdialog.dismiss();
        }
    }

    private void createNoti() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
        builder.setContentText("Tickets");
        builder.setContentText("Check Your Tickets in Information Section");
        builder.setSmallIcon(R.drawable.designn);
        builder.setVibrate(new long[]{100, 100, 100, 100, 100});
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(sound);
        NotificationManager manager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
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
