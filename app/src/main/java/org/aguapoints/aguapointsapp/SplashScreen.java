package org.aguapoints.aguapointsapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SplashScreen extends AppCompatActivity {


    DatabaseReference mDatabase;


    FirebaseAuth mAuth;


    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mAuth = FirebaseAuth.getInstance();


                if (mAuth.getCurrentUser() == null) {
                    loadLoginView();
                    //loadMainView();
                }else{
                    loadMainView();
                }
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }



    private void loadLoginView() {
        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    private void loadMainView() {
        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    }