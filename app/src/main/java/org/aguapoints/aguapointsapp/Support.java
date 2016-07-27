package org.aguapoints.aguapointsapp;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/*
 * Created by edgartrujillo on 5/30/16.
 */
public class Support extends AppCompatActivity {



    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }



}
