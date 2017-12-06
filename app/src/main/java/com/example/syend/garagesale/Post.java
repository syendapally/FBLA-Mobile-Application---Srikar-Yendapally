package com.sriyendapkar.syend.garagesale;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Post extends AppCompatActivity {
private ImageView imageView;
    private static final int Gallery_Request= 1;
    private Button button;
    private EditText title;
    private EditText desc;
    private EditText price;
    private Uri mImageUri = null;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
       @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        button= (Button) findViewById(R.id.Add_Item);
           storageReference= FirebaseStorage.getInstance().getReference();
           databaseReference = FirebaseDatabase.getInstance().getReference().child("1AGarage Sale");
        imageView = (ImageView) findViewById(R.id.newPic);
        title = (EditText) findViewById(R.id.title);
        price = (EditText) findViewById(R.id.price);
        desc = (EditText) findViewById(R.id.desc);
    imageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent galleryInent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryInent.setType("image/*");
            startActivityForResult(galleryInent, Gallery_Request);
        }
    });
   button.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
       addItem();
       }
   });
    }
   //Creates item with title, description, and price, and stored in database
    private void addItem() {
        final String priceStr = price.getText().toString().trim();
        final String titleStr = title.getText().toString().trim();
        final String descStr = desc.getText().toString().trim();
        if(!TextUtils.isEmpty(titleStr) &&!TextUtils.isEmpty(descStr)&&!TextUtils.isEmpty(priceStr)&& mImageUri!=null)
        {
        StorageReference filepath = storageReference.child("Garage_Sale").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost = databaseReference.child(titleStr); //child is created in database

                    newPost.child("title").setValue(titleStr);
                    newPost.child("desc").setValue(descStr);
                    newPost.child("price").setValue(priceStr);
                    newPost.child("comment").setValue("");
                    newPost.child("img").setValue(downloadUrl.toString());
                    Toast.makeText(Post.this, "Item Added", Toast.LENGTH_SHORT).show(); //Toast/Message is created
                    Intent intent = new Intent (Post.this, MainActivity.class);
                    intent.putExtra("newPost",newPost.getRoot().toString());
                    startActivity(intent);
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Gallery_Request && resultCode == RESULT_OK){
            mImageUri = data.getData();
            imageView.setImageURI(mImageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
