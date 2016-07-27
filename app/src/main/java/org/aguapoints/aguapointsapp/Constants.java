package org.aguapoints.aguapointsapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/*
 * Created by edgartrujillo on 5/24/16.
 */
public class Constants {
    DatabaseReference FIREBASE_URL = FirebaseDatabase.getInstance().getReference();
}
