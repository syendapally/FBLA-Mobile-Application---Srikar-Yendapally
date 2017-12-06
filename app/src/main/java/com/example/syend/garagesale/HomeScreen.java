
package com.sriyendapkar.syend.garagesale;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
    }
    public void enterGarageSaleApp(View view) {
        Intent intent = new Intent(HomeScreen.this, MainActivity.class);
        startActivity(intent); //allows user to go to the next screen

    }
}
