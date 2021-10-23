package com.upper.team10.soccer.mainfragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.vision.CameraSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.upper.team10.soccer.Model.TicketModel;
import com.upper.team10.soccer.R;
import com.upper.team10.soccer.activity.Arun;
import com.upper.team10.soccer.activity.Setting;
import com.upper.team10.soccer.activity.TicketSingleActivity;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.data;
import static android.R.attr.mode;
import static android.R.attr.multiArch;
import static android.R.attr.text;
import static android.app.Activity.RESULT_OK;
import static android.graphics.Color.WHITE;
import static com.upper.team10.soccer.activity.Main.mToolbar;

public class PageFour extends Fragment {

    private ImageView coverpagefour;
    private CircleImageView profilepagefour;
    private TextView namepagefour;
    private RecyclerView mRecyclerview;
    private DatabaseReference mdRef;
    private DatabaseReference mProfiles;
    private String mUserid;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private TextView textviewbalance;
    public PageFour() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_page_four, container, false);
        mAuth=FirebaseAuth.getInstance();
        if(mAuth!=null){
            mUser=mAuth.getCurrentUser();
            if(mAuth.getCurrentUser()!=null){
              mUserid= mAuth.getCurrentUser().getUid();
                mdRef = FirebaseDatabase.getInstance().getReference().child("Ticketdetails");
                mRef=mdRef.child(mUserid);
                mProfiles = FirebaseDatabase.getInstance().getReference().child("Profiles");
                mProfiles.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(mUserid) ){
                            final DatabaseReference database = mProfiles.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            database.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    final Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                                    namepagefour.setText(dataSnapshot.child("name").getValue(String.class));
                                    textviewbalance.setText(dataSnapshot.child("balance").getValue(String.class)+" Kyats");

                                    final String url=dataSnapshot.child("image").getValue(String.class);
                                    Picasso.with(getActivity()).load(url).networkPolicy(NetworkPolicy.OFFLINE).into(profilepagefour, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError() {
                                            Picasso.with(getActivity()).load(url).into(profilepagefour);
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

            }

        }

        coverpagefour = (ImageView) v.findViewById(R.id.coverpagefour);
        profilepagefour = (CircleImageView) v.findViewById(R.id.profilepagefour);
        namepagefour = (TextView) v.findViewById(R.id.namepagefour);
        textviewbalance= (TextView) v.findViewById(R.id.textviewbalance);
        mRecyclerview = (RecyclerView) v.findViewById(R.id.pagefourrecyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mRecyclerview.setLayoutManager(layoutManager);
        namepagefour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Setting.class);
                startActivity(intent);
            }
        });
        return v;


    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mAuth.getCurrentUser()!=null){
            FirebaseRecyclerAdapter<TicketModel, MyTicketHolder> firebaseRadapter = new FirebaseRecyclerAdapter<TicketModel, MyTicketHolder>(
                    TicketModel.class, R.layout.pagefourbuyticket_layout, MyTicketHolder.class,mRef
            ) {
                @Override
                protected void populateViewHolder(final MyTicketHolder viewHolder, final TicketModel model, int position) {
                    viewHolder.setMatchtpe(model.getMatchtype());
                    viewHolder.setHomeLogo(model.getHomelogo());
                    viewHolder.setAwayLogo(model.getAwaylogo());
                    viewHolder.setDate(model.getDate());
                    viewHolder.setMatchSeat(model.getName());
                    viewHolder.textView_section.setText(model.getStdsection());
                    viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                         String all = generateAll();
                            MaterialDialog qrdialog=new MaterialDialog.Builder(getActivity())
                                    .title("Ticket")
                                    .customView(R.layout.qrcodelayout,false)
                                    .build();
                            View viewqr=qrdialog.getCustomView();
                            ImageView singleqrcode= (ImageView) viewqr.findViewById(R.id.singleqrcode);
                            GenerateClick(viewqr,all,singleqrcode);
                            qrdialog.show();
                            return true;
                        }

                        private String generateAll() {
                            String all=mAuth.getCurrentUser().getUid()+"\n"
                                    +model.getMatchname()+"\n"
                                    +model.getMatchaway()+"\n"
                                    +model.getStdname()+"\n"
                                    +model.getStdadult()+"\n"
                                    +model.getStdsection()+"\n"
                                    +model.getTime()+"\n"
                                    +model.getDate()+"\n"
                                    +model.getHomelogo()+"\n"
                                    +model.getAwaylogo();
                            return all;

                        }
                    });
                    final String ref_pos=getRef(position).getKey();
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean result=haveNetworkConnection();
                            if(result){
                                Intent intent=new Intent(getActivity(),TicketSingleActivity.class);
                                Pair<View,String> pairone= Pair.create(viewHolder.itemView.findViewById(R.id.homelogo),"hlogo");
                                Pair<View,String> pairtwo=Pair.create(viewHolder.itemView.findViewById(R.id.awaylogo),"alogo");
                                ActivityOptionsCompat compact=ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),pairone,pairtwo);
                                intent.putExtra("homename",model.getMatchname());
                                intent.putExtra("awayname",model.getMatchaway());
                                intent.putExtra("ticketname",model.getName());
                                intent.putExtra("ticketadult",model.getStdadult());
                                intent.putExtra("stdsection",model.getStdsection());
                                intent.putExtra("time",model.getTime());
                                intent.putExtra("date",model.getDate());
                                intent.putExtra("uid",model.getUid());
                                intent.putExtra("email",model.getEmail());
                                intent.putExtra("homelogo",model.getHomelogo());
                                intent.putExtra("awaylogo",model.getAwaylogo());
                                intent.putExtra("ref_pos",ref_pos);
                                intent.putExtra("matchtype",model.getMatchtype());
                                startActivity(intent,compact.toBundle());
                            }
                            else{
                                Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }


            };
            firebaseRadapter.notifyDataSetChanged();
            mRecyclerview.setAdapter(firebaseRadapter);



        }
    }




    public static class MyTicketHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView textView_section;
        public MyTicketHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            textView_section = (TextView) itemView.findViewById(R.id.ticket_section);
        }
        public void setMatchtpe(String string){
            TextView textview= (TextView) itemView.findViewById(R.id.matchtype);
            textview.setText(string);
        }
        public void setHomeLogo(final String string){
            final ImageView imageview= (ImageView) itemView.findViewById(R.id.homelogo);
            Picasso.with(itemView.getContext()).load(string).networkPolicy(NetworkPolicy.OFFLINE).into(imageview, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(itemView.getContext()).load(string).into(imageview);
                }
            });
        }
        public void setMatchSeat(String seat){
            TextView textview= (TextView) itemView.findViewById(R.id.nameseat);
            textview.setText(seat);
        }
        public void setAwayLogo(final String string){
            final ImageView imageview= (ImageView) itemView.findViewById(R.id.awaylogo);
            Picasso.with(itemView.getContext()).load(string).networkPolicy(NetworkPolicy.OFFLINE).into(imageview, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(itemView.getContext()).load(string).into(imageview);
                }
            });
        }
        public void setDate(String string){
            TextView textview= (TextView) itemView.findViewById(R.id.date);
            textview.setText(string);
        }


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
    public void GenerateClick(View view,String data,ImageView imageview){
        try {
            int width =300,height = 300;
            int smallestDimension = width < height ? width : height;

            String qrCodeData = data;
            //setting parameters for qr code
            String charset = "UTF-8";
            Map<EncodeHintType, ErrorCorrectionLevel> hintMap =new HashMap<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            CreateQRCode(qrCodeData, charset, hintMap, smallestDimension, smallestDimension,imageview);

        } catch (Exception ex) {
            Log.e("QrGenerate",ex.getMessage());
        }
    }
    public  void CreateQRCode(String qrCodeData, String charset, Map hintMap, int qrCodeheight, int qrCodewidth,ImageView imageview){


        try {
            //generating qr code.
            BitMatrix matrix = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset),
                    BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
            //converting bitmatrix to bitmap

            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width * height];
            // All are 0, or black, by default
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    //for black and white
                    //pixels[offset + x] = matrix.get(x, y) ? BLACK : WHITE;
                    //for custom color
                    pixels[offset + x] = matrix.get(x, y) ?
                            ResourcesCompat.getColor(getResources(),R.color.colorPrimary,null) :WHITE;
                }
            }
            //creating bitmap
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

            //getting the logo
            Bitmap overlay = BitmapFactory.decodeResource(getResources(), R.drawable.smalllogo );
            //setting bitmap to image view
            imageview.setImageBitmap(mergeBitmaps(overlay,bitmap));

        }catch (Exception er){
            Log.e("QrGenerate",er.getMessage());
        }
    }
    public Bitmap mergeBitmaps(Bitmap overlay, Bitmap bitmap) {

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        Bitmap combined = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Canvas canvas = new Canvas(combined);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        canvas.drawBitmap(bitmap, new Matrix(), null);

        int centreX = (canvasWidth  - overlay.getWidth()) /2;
        int centreY = (canvasHeight - overlay.getHeight()) /2 ;
        canvas.drawBitmap(overlay, centreX, centreY, null);

        return combined;
    }
}