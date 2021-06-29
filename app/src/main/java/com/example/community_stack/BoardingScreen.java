package com.example.community_stack;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BoardingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_boarding_screen);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        Intent intent;
                        if(user == null)
                        {
                            intent = new Intent(BoardingScreen.this, MainActivity.class);
                        }

                        else
                        {
                            intent = new Intent(BoardingScreen.this, Dashboard.class);
                        }
                        startActivity(intent);

                        finish();

                    }
                });
            }
        }).start();

    }


}