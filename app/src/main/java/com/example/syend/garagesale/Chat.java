package com.sriyendapkar.syend.garagesale;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Chat extends AppCompatActivity implements SensorEventListener {

    private FirebaseUser user;
    private TextView message;
    private ImageView send;
    private Button ring;
    private EditText input_msg;
    private DatabaseReference root;
    private String temp_key;
    private String emailStr;
    private String chatName;
    private SensorManager sm;
    private Sensor proxSensor;
    private ScrollView scrollView;

    ArrayList<String> values = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            chatName = extras.getString("chat_name");
        }

        send = (ImageView) findViewById(R.id.send);
        //ring = (Button) findViewById(R.id.ring);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        proxSensor= sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sm.registerListener(this,proxSensor,SensorManager.SENSOR_DELAY_NORMAL);

        input_msg = (EditText) findViewById(R.id.input);
        message = (TextView) findViewById(R.id.messageChat);
        setTitle("Chat Room- " + chatName);

        //FirebaseDatabase fb1 = FirebaseDatabase.getInstance();  //getReference().child("Chat Room");
        root = FirebaseDatabase.getInstance().getReference().child(chatName);
        //Method that posts messages
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);
                DatabaseReference message_root = root.child(temp_key);
                Map<String, Object> map2 = new HashMap<String, Object>();

                map2.put("message", input_msg.getText().toString());
                message_root.updateChildren(map2);
                input_msg.setText("");


            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);

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
    private String chat_msg;
    private void append_chat_conversation(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()){

            chat_msg = (String)((DataSnapshot)i.next()).getValue();
            message.append(" "+ "\n" + chat_msg +" \n" + "\n" );

        }
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        message.setTextColor((int)(Math.random()*Integer.MAX_VALUE+Integer.MIN_VALUE));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}




