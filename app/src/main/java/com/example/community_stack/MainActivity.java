package com.example.community_stack;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static int AUTHUI_REQUEST_CODE = 7192;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //button = findViewById(R.id.loginRegister);
        handleLoginRegister();


    }

    public void handleLoginRegister(){

        List<AuthUI.IdpConfig> provider = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        Intent intent = AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(provider).setTosAndPrivacyPolicyUrls("https://google.com","https://google.com").setIsSmartLockEnabled(false).setLogo(R.drawable.new_logo).build();
        startActivityForResult(intent,AUTHUI_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AUTHUI_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                ///We have signed in or we have a new user
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d("","Msg: "+user.getEmail());
                if(user.getMetadata().getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp()){
                    Toast.makeText(this,"Welcome new user",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this,"Welcome back user",Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(this, Dashboard.class);
                startActivity(intent);
                this.finish();
            }
            else{
                ///Signing failed
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if(response == null){
                    Log.d("","Msg:user has cancelled sign in");
                }
                else{
                    Log.d("","Msg"+ response.getError());
                }
            }
        }
    }
}