package com.upper.team10.soccer.Testing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.upper.team10.soccer.R;

public class TwoLayoutRecyclerview extends AppCompatActivity {
    private RecyclerView recyclerview;
    private DatabaseReference mdRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_layout_recyclerview);
        mdRef= FirebaseDatabase.getInstance().getReference().child("Testing");
        recyclerview= (RecyclerView) findViewById(R.id.recyclertwolayout);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Testing,MyTestingHolder> adapter=new FirebaseRecyclerAdapter<Testing, MyTestingHolder>(
                Testing.class,R.layout.testinglayout,MyTestingHolder.class,mdRef

        ) {
            @Override
            protected void populateViewHolder(MyTestingHolder viewHolder, Testing model, int position) {
                viewHolder.setName(model.getName());
            }
        };
        adapter.notifyDataSetChanged();
        recyclerview.setAdapter(adapter);
    }

    private static class MyTestingHolder extends RecyclerView.ViewHolder {
        View itemView;
        public MyTestingHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
        }
        public void setName(String name){
            TextView textview= (TextView) itemView.findViewById(R.id.textviewtesing);
            textview.setText(name);
        }
    }
}

