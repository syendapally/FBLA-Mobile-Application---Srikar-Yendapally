package com.sriyendapkar.syend.garagesale;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private TextView textView;
    private EditText editText;
    private String comments;
    private DatabaseReference firebaseDatabase;
    private DatabaseReference root;
    private String temp_key;
    private  String commentLast;
    private String move;
    private EditText name;
    private SensorManager sm;
    private Sensor proxSensor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.sale_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("1AGarage Sale");
        root = FirebaseDatabase.getInstance().getReference().child("Comments");

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        proxSensor= sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sm.registerListener(this,proxSensor,SensorManager.SENSOR_DELAY_NORMAL);
            //   textView.setText(editText.getText().toString());

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<GSale, SaleViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<GSale, SaleViewHolder>(
                GSale.class,
                R.layout.sale_row,
                SaleViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(final SaleViewHolder viewHolder, final GSale model, int position) {
             viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setImg(getApplicationContext(),model.getImg());
                viewHolder.setComment(model.getComment());
                viewHolder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*Map<String, Object> map = new HashMap<String, Object>();
                        temp_key = root.push().getKey();
                        root.updateChildren(map);
                        DatabaseReference message_root = root.child(temp_key);
                        Map<String, Object> map2 = new HashMap<String, Object>();
                        map2.put("comment", viewHolder.editText.getText().toString());

                        message_root.updateChildren(map2);
                        viewHolder.editText.setText("");
                        */
                        model.setComment(viewHolder.name.getText().toString() + ": "+ viewHolder.editText.getText().toString());
                        viewHolder.setComment(model.getComment());
                        mDatabase.child(model.getTitle()).child("comment").setValue(model.getComment()); //creates comments in the database

                    }
                });
                root.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        viewHolder.textView.append(append_chat_conversation(dataSnapshot) + "\n");
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        viewHolder.textView.append(append_chat_conversation(dataSnapshot) + "\n");
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });





            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }
    private String append_chat_conversation(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()){

            commentLast= (String)((DataSnapshot)i.next()).getValue();

        }
        return commentLast;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        recyclerView.setBackgroundColor((int)(Math.random()*Integer.MAX_VALUE+Integer.MIN_VALUE));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public static class SaleViewHolder extends RecyclerView.ViewHolder {
        View mView;
         Button btn;
         TextView textView;
         EditText editText;
           EditText name;
        public SaleViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            btn = (Button) mView.findViewById(R.id.comment);
            textView =(TextView) mView.findViewById(R.id.commentview);
            editText= (EditText) mView.findViewById(R.id.commententer);
            name = (EditText)mView.findViewById(R.id.personName);
        }
        public void setTitle(String title){
            TextView postTitle = (TextView) mView.findViewById(R.id.post_title);
            postTitle.setText(title);
        }
        public void setDesc (String desc){
            TextView postDescription = (TextView) mView.findViewById(R.id.post_desc);
            postDescription.setText(desc);
        }
        public void setPrice(String price){
            TextView postPrice = (TextView) mView.findViewById(R.id.post_price);
            postPrice.setText(price);
        }
        public void setImg(Context ctx, String img){
            ImageView postImage= (ImageView) mView.findViewById(R.id.postImage);
            Picasso.with(ctx).load(img).into(postImage);
        }
        public void setComment(String comment){
            TextView postComment = (TextView) mView.findViewById(R.id.commentview);
            postComment.append(comment +"\n");
        }
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.add){
            Intent intent = new Intent(MainActivity.this, Post.class);
            startActivity(intent);
        }
        else if (item.getItemId()==R.id.chat)
        {
            Intent intent = new Intent(MainActivity.this, ChatList.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
