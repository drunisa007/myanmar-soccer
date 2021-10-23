package com.upper.team10.soccer.activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.upper.team10.soccer.Model.Model_Receive_Ticket;
import com.upper.team10.soccer.R;

public class Receive_Ticket extends AppCompatActivity {
   private RecyclerView receive_ticket_recycycler;
    private DatabaseReference mdRef;
    private FirebaseAuth mAuth;
    private DatabaseReference mNotiRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive__ticket);
        givingId();
    }

    private void givingId() {
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tickets");
        receive_ticket_recycycler= (RecyclerView) findViewById(R.id.receive_ticket_recycycler);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        receive_ticket_recycycler.setLayoutManager(manager);
        mAuth=FirebaseAuth.getInstance();
        mdRef= FirebaseDatabase.getInstance().getReference().child("ReceiveTickets").child(mAuth.getCurrentUser().getUid());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Model_Receive_Ticket,MyReceiveTicketHolder> adapter=new FirebaseRecyclerAdapter<Model_Receive_Ticket, MyReceiveTicketHolder>(
                Model_Receive_Ticket.class,
                R.layout.my_receive_ticket_layout,
                MyReceiveTicketHolder.class,
                mdRef


        ) {
            @Override
            protected void populateViewHolder(MyReceiveTicketHolder viewHolder, Model_Receive_Ticket model, int position) {
                  viewHolder.setName("From "+model.getName());
                viewHolder.setQrimage(generateAll(model));
                viewHolder.receive_section_match.setText(model.getMatchname()+" Vs "+model.getMatchaway());
                viewHolder.receive_section_matchtype.setText(model.getMatchtype());
                viewHolder.receive_section_stadium.setText(model.getStdname());
                viewHolder.receive_section_section.setText(model.getStdsection());
                viewHolder.receive_section_matchdate.setText(model.getDate());
            }

            private String generateAll(Model_Receive_Ticket model) {
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
        };
        adapter.notifyDataSetChanged();
        receive_ticket_recycycler.setAdapter(adapter);
    }

    public static  class MyReceiveTicketHolder extends RecyclerView.ViewHolder {
        TextView receive_section_stadium,receive_section_section,receive_section_matchdate,receive_section_matchtype,receive_section_match;
        View itemView;
        public MyReceiveTicketHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            receive_section_match= (TextView) itemView.findViewById(R.id.receive_section_match);
            receive_section_stadium= (TextView) itemView.findViewById(R.id.receive_section_stadium);
            receive_section_section= (TextView) itemView.findViewById(R.id.receive_section_section);
            receive_section_matchdate= (TextView) itemView.findViewById(R.id.receive_section_matchdate);
            receive_section_matchtype= (TextView) itemView.findViewById(R.id.receive_section_matchtype);
        }
        public void setName(String sender_name){
            TextView receive_name= (TextView) itemView.findViewById(R.id.receive_name);
            receive_name.setText(sender_name);
        }
        public void setQrimage(String all){
            ImageView imageview= (ImageView) itemView.findViewById(R.id.receive_qrcode);
            MultiFormatWriter multi=new MultiFormatWriter();
            try{
                BitMatrix bitmatrix=multi.encode(all, BarcodeFormat.QR_CODE,300,300);
                BarcodeEncoder barcodeencoder=new BarcodeEncoder();
                Bitmap bitmap=barcodeencoder.createBitmap(bitmatrix);
                imageview.setImageBitmap(bitmap);
            }
            catch (WriterException e){
                e.printStackTrace();
            }
        }

    }
}
