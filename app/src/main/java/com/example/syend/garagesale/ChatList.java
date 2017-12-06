package com.sriyendapkar.syend.garagesale;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ChatList extends AppCompatActivity {

    private ListView listView;
    private ImageView imageView;
    private EditText editText;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> listOfRooms= new ArrayList<String>();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        imageView= (ImageView) findViewById(R.id.addChat);
        editText= (EditText) findViewById(R.id.addRoom);
        listView = (ListView) findViewById(R.id.chatList);
        arrayAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfRooms);

        listView.setAdapter(arrayAdapter);
    /*
        Database Root is used to update the children
  */      imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(editText.getText().toString())) {
                    String chatname = editText.getText().toString();

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(chatname, "");
                    root.updateChildren(map);
                }
                Intent intent = new Intent(getApplicationContext(), Chat.class);
                if(!TextUtils.isEmpty(editText.getText().toString())) {
                    intent.putExtra("chat_name", editText.getText().toString());

                    editText.setText("");
                    startActivity(intent);
                }
            }
        });


        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                Set<String> set = new HashSet<String>();

                while(iterator.hasNext()){
                   String key = ((DataSnapshot)iterator.next()).getKey();
                    if(key.equals("1AGarage Sale")){

                    }
                        else
                        //set.add(((DataSnapshot)iterator.next()).getKey());
                         set.add(key);

                }
                listOfRooms.clear();
                listOfRooms.addAll(set);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),Chat.class);
                intent.putExtra("chat_name", ((TextView)view).getText().toString());
                intent.putExtra("user_name", name);
                startActivity(intent);
            }
        });
    }

    private void requestUserName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Your Name");
        final EditText inputfield = new EditText(this);
        builder.setView(inputfield);
        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = inputfield.getText().toString();
                dialog.cancel();

            }
        });
        builder.show();
    }

}
