package org.aguapoints.aguapointsapp;

/*
 * Created by edgartrujillo on 6/8/16.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.os.CountDownTimer;
import com.jirbo.adcolony.*;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jirbo.adcolony.AdColony;
import com.jirbo.adcolony.AdColonyAd;
import com.jirbo.adcolony.AdColonyAdAvailabilityListener;
import com.jirbo.adcolony.AdColonyAdListener;
import com.jirbo.adcolony.AdColonyVideoAd;

import java.util.ArrayList;
import java.util.Calendar;

import com.google.firebase.auth.FirebaseAuth;


public class HomeFragment extends Fragment implements AdColonyAdAvailabilityListener, AdColonyAdListener{


    int adsSeen;

    boolean withinHours;

    int timeCycle;

    int originalTimer;

    int points;

    int ads;

    String time;

    FirebaseAuth mAuth;

    String useremail;

    DatabaseReference mDatabase;
    TextView totalpoints;

    Context context;


    final public String APP_ID = "app185a7e71e1714831a49ec7";
    final private String ZONE_ID = "vz06e8c32a037749699e7050";
    //Button used to play videos, enabled when ads are available
    private Button button;

    CountDownTimer timer = null;

    //start timer function
    public void startTimer() {
        timer = new CountDownTimer(timeCycle, 1000) {
            public void onTick(long millisUntilFinished) {

                long mins = millisUntilFinished / 60000;
                long s = mins * 60000 ;
                long sec = millisUntilFinished - s;
                long secs = sec / 1000;

                time = "Time Remaing: " + mins + " minutes and " + secs  + " seconds";
                TextView timer = (TextView) getActivity().findViewById(R.id.timeRemaining);
                timer.setText(time);


            }
            public void onFinish() {
                addPoint();
                timer.start();
            }
        };
        timer.start();
    }

    //cancel timer
    public void cancelTimer() {
        if(timer!=null)
            timer.cancel();
    }


    final public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public String userId = getUid();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        context = rootView.getContext();


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        if (mAuth.getCurrentUser() == null) {
            loadLoginView();
        }


        Calendar cal = Calendar.getInstance();
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        if (hours > 6 || hours < 22) {
            withinHours = true;
            retrieveValues();
            String yes = "Please lock your phone to begin.";
            TextView text = (TextView) rootView.findViewById(R.id.WithinHoursText);
            text.setText(yes);
            setUpTimerBasedOnAds();

        } else {
            withinHours = false;
            String no = "Sorry, Come back soon....";
            TextView text = (TextView) rootView.findViewById(R.id.WithinHoursText);
            text.setText(no);
            final String userId = getUid();
            FirebaseDatabase.getInstance().getReference().child("users").child(userId).addListenerForSingleValueEvent(
                    new com.google.firebase.database.ValueEventListener() {
                        @Override
                        public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            points = user.pointsEarned;
                            String str = Integer.toString(points);
                            TextView  totalpoints = (TextView) rootView.findViewById(R.id.totalPoints);
                            totalpoints.setText(str);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );

            FirebaseDatabase.getInstance().getReference().child("users").child(userId).addListenerForSingleValueEvent(
                    new com.google.firebase.database.ValueEventListener() {
                        @Override
                        public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            useremail = user.email;



                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );
            TextView timerTxt = (TextView) getActivity().findViewById(R.id.timeRemaining);
            timerTxt.setText(time);

        }

        AdColony.configure(getActivity(), "version:1.0,store:google", APP_ID, ZONE_ID);
        configureAdcolony();
        button = (Button) rootView.findViewById( R.id.Adbutton );
        button.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AdColonyVideoAd ad = new AdColonyVideoAd( ZONE_ID ).withListener( HomeFragment.this );
                cancelTimer();
                //  adWatched();
                ad.show();

            }
        } );

        //If ads are already ready for zone (i.e. if onCreate is happening for a second time) we want
        //to make sure our button is enabled.
        if (AdColony.statusForZone( ZONE_ID ).equals( "active" ))
        {
            button.setEnabled( true );
          }

        return rootView;
    }


    public void configureAdcolony(){
        AdColony.configure( getActivity() , "version:1.0,store:google", APP_ID, ZONE_ID );

        //Register an AdColonyAdAvailabilityListener to be notified of changes in a zone's
        //ad availability.
        AdColony.addAdAvailabilityListener( this );

    }

    @Override
    public void onAdColonyAdAvailabilityChange( final boolean available, String zone_id )
    {
        getActivity().runOnUiThread( new Runnable()
        {
            @Override
            public void run()
            {
                if (available)
                {
                    button.setEnabled( true );
                }
                else
                {
                    button.setEnabled( false );
                }
            }
        } );
    }

    @Override
    public void onAdColonyAdAttemptFinished( AdColonyAd ad )
    {

        //Can use the ad object to determine information about the ad attempt:
        //ad.shown();
        //ad.notShown();
        //ad.canceled();
        //ad.noFill();
        //ad.skipped();



    }

    @Override
    public void onAdColonyAdStarted( AdColonyAd ad )
    {
        //Called when the ad has started playing

    }

    @Override
    public void onResume()
    {
        super.onResume();
       AdColony.resume(getActivity());
        adWatched();

    }

    @Override
    public void onPause()
    {
        super.onPause();
        AdColony.pause();
    }

    public  void AppWithinHours(){
        if (withinHours){
            String yes = "Please lock your phone to begin.";
           // setText(yes);
            setUpTimerBasedOnAds();
        }
        else {
            String no = "Sorry, Come back soon....";
            // setText(no);
           FirebaseDatabase.getInstance().getReference().child("users").child(userId).addListenerForSingleValueEvent(
                    new com.google.firebase.database.ValueEventListener() {
                        @Override
                        public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            points = user.pointsEarned;
                            String str = Integer.toString(points);
                            TextView  totalpoints = (TextView) getActivity().findViewById(R.id.totalPoints);
                            totalpoints.setText(str);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );

            FirebaseDatabase.getInstance().getReference().child("users").child(userId).addListenerForSingleValueEvent(
                    new com.google.firebase.database.ValueEventListener() {
                        @Override
                        public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            useremail = user.email;



                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );

        }
    }

    public  void addPoint(){
        //Adds a point to users total points...
        FirebaseDatabase.getInstance().getReference().child("users").child(userId).addListenerForSingleValueEvent(
                new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        points = user.pointsEarned;
                   /*     String str = Integer.toString(points);
                        TextView  totalpoints = (TextView) getActivity().findViewById(R.id.totalPoints);
                        totalpoints.setText(str);
*/
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
        points += 1;
        FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("pointsEarned").setValue(points);
        String str = Integer.toString(points);
        TextView  totalpoints = (TextView) getActivity().findViewById(R.id.totalPoints);
        totalpoints.setText(str);
    }

    public void retrieveValues() {
        FirebaseDatabase.getInstance().getReference().child("users").child(userId).addListenerForSingleValueEvent(
                new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        points =  user.pointsEarned ;
                        String str = Integer.toString(points);
                        TextView  totalpoints = (TextView) getActivity().findViewById(R.id.totalPoints);
                        totalpoints.setText(str);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });


        FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("online").setValue("yes");


    }



    public void updateAdsSeen(){
        FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("CurrentAdsSeen").setValue(adsSeen);

    }


    public void setUpTimerBasedOnAds(){
        FirebaseDatabase.getInstance().getReference().child("users").child(userId).addListenerForSingleValueEvent(
                new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        ads = user.CurrentAdsSeen;

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        switch( ads){
            case 0:
                timeCycle = 200000;
                originalTimer = timeCycle;
                startTimer();
            case 1:
                cancelTimer();
                timeCycle = 150000;
                originalTimer = timeCycle;
                startTimer();

                break;
            case 2:
                cancelTimer();
                timeCycle = 100000;
                originalTimer = timeCycle;
                startTimer();
                break;
            case 3:
                cancelTimer();
                timeCycle = 50000;
                originalTimer = timeCycle;
                startTimer();
                break;
            case 4:
                cancelTimer();
                timeCycle = 25000;
                originalTimer = timeCycle;
                startTimer();
                break;
            case 5:
                cancelTimer();
                timeCycle = 10000;
                originalTimer = timeCycle;
                startTimer();
                break;
            case 6:
                cancelTimer();
                timeCycle = 5000;
                originalTimer = timeCycle;
                startTimer();
                break;
            default:
                cancelTimer();
                timeCycle = 5000;
                originalTimer = timeCycle;
                startTimer();
                break;
        }
    }


    public void adWatched() {

        ads += 1;
        switch (ads) {
            case 1:
                updateAdsSeen();
                cancelTimer();
                timeCycle = 150000;
                originalTimer = timeCycle;
                startTimer();
                break;
            case 2:
                updateAdsSeen();
                cancelTimer();
                timeCycle = 100000;
                originalTimer = timeCycle;
                startTimer();
                break;
            case 3:
                updateAdsSeen();
                cancelTimer();
                timeCycle = 50000;
                originalTimer = timeCycle;
                startTimer();
                break;
            case 4:
                updateAdsSeen();
                cancelTimer();
                timeCycle = 25000;
                originalTimer = timeCycle;
                startTimer();
                break;
            case 5:
                updateAdsSeen();
                cancelTimer();
                timeCycle = 10000;
                originalTimer = timeCycle;
                startTimer();
                break;
            case 6:
                updateAdsSeen();
                cancelTimer();
                timeCycle = 5000;
                originalTimer = timeCycle;
                startTimer();
                break;
            default:
                adsSeen += 1;
                updateAdsSeen();
                cancelTimer();
                timeCycle = 5000;
                originalTimer = timeCycle;
                startTimer();
                break;
        }
    }


    private void loadLoginView() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}

