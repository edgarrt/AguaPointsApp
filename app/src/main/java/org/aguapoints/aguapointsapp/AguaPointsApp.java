/*
 * Created by edgartrujillo on 5/25/16.
 */
package org.aguapoints.aguapointsapp;

import android.app.Application;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.database.FirebaseDatabase;

// import com.facebook.appevents.AppEventsLogger;
// import com.facebook.FacebookSdk;

public class AguaPointsApp extends Application {
    @Override
        public void onCreate() {
            super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        MultiDex.install(this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

    }

    }

