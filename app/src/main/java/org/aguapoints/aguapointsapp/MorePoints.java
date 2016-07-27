package org.aguapoints.aguapointsapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jirbo.adcolony.*;

public class MorePoints extends Fragment implements AdColonyAdAvailabilityListener, AdColonyAdListener {

   final public String APP_ID  = "app185a7e71e1714831a49ec7";
        final private String ZONE_ID = "vz06e8c32a037749699e7050";

        //Button used to play videos, enabled when ads are available
        private Button button;
        private ProgressBar progress;
        private TextView text;

        boolean maxed;

        int stamp;

        int hours;

        int diff;

        int points;

        int freePoints;

        boolean newDay = false;

        int extraPoints = 0;

        int totalPoints;

        DatabaseReference mDatabase;

        public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


//    var Timestamp:NSTimeInterval = NSDate().timeIntervalSince1970 / 3600

//    var TodaysDate:NSTimeInterval = NSDate().timeIntervalSince1970 / 3600

        double timeRemaining = 0.0;

        double timeNeeded = 0.0;



//    var timeStamp: CFAbsoluteTime!

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_more_points, container,false);

            //Configure AdColony in your launching Activity's onCreate() method
            //so that ads can be available as soon as possible.
            configureAdcolony();
            setUpView();
            text = (TextView) myView.findViewById(R.id.sorrytext);
            button = (Button) myView.findViewById( R.id.videoButton );
            button.setOnClickListener( new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    AdColonyVideoAd ad = new AdColonyVideoAd( ZONE_ID ).withListener(MorePoints.this);
                    ad.show();
                    adsSeen();

                }
            } );

            //If ads are already ready for zone (i.e. if onCreate is happening for a second time) we want
            //to make sure our button is enabled.
            if (AdColony.statusForZone( ZONE_ID ).equals( "active" ))
            {
                button.setEnabled( true );
            }

        return myView;
    }

        public void configureAdcolony(){
            AdColony.configure( getActivity() , "version:1.0,store:google", APP_ID, ZONE_ID );

            //Register an AdColonyAdAvailabilityListener to be notified of changes in a zone's
            //ad availability.
            AdColony.addAdAvailabilityListener( this );

            setUpView();

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
        }

        @Override
        public void onPause()
        {
            super.onPause();
            AdColony.pause();
        }


        public void setUpView() {
            final String userId = getUid();
            FirebaseDatabase.getInstance().getReference().child("users").child(userId).addListenerForSingleValueEvent(
                    new com.google.firebase.database.ValueEventListener() {
                        @Override
                        public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            totalPoints = user.pointsEarned;
                            String str = Integer.toString(totalPoints);
                            TextView totalpoints = (TextView) myView.findViewById(R.id.totalPoints);
                            totalpoints.setText(str);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });

            FirebaseDatabase.getInstance().getReference().child("users").child(userId).addListenerForSingleValueEvent(
                    new com.google.firebase.database.ValueEventListener() {
                        @Override
                        public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            extraPoints = user.extraPoints;
                            switch( extraPoints) {
                                case 0:
                                    text.setVisibility(View.INVISIBLE);
                                    button.setVisibility(View.VISIBLE);
                                    break;
                                case 1: text.setVisibility(View.INVISIBLE);
                                    button.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    text.setVisibility(View.INVISIBLE);
                                    button.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                    text.setVisibility(View.VISIBLE);
                                    button.setVisibility(View.INVISIBLE);
                                    whenAval();
                                    break;
                                default:
                                    text.setVisibility(View.VISIBLE);
                                    button.setVisibility(View.INVISIBLE);
                                    break;
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });


        }

        public void whenAval(){
            final String userId = getUid();
            FirebaseDatabase.getInstance().getReference().child("users").child(userId).addListenerForSingleValueEvent(
                    new com.google.firebase.database.ValueEventListener() {
                        @Override
                        public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                             stamp = user.timeStamp;
                             hours = (int) (System.currentTimeMillis() / 3600000);
                             diff = hours - stamp;
                            if ( diff > 23){
                                maxed = false;
                                updateTimeStamp();
                            }
                            else{
                                text.setVisibility(View.VISIBLE);
                                button.setVisibility(View.INVISIBLE);
                            }
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });

        }

        public void updateTimeStamp(){
            final String userId = getUid();
            int hours = (int) (System.currentTimeMillis() / 3600000);
            // Update Firebase with timestamp value;
            if (maxed){
                //Updates maxedTimeStamp to firebase
                FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("timeStamp").setValue(hours);
                FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("extraPoints").setValue(3);
            }
            else {
                //Updates timestamp & extrapoints back to zero
                FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("timeStamp").setValue(0);
                FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("extraPoints").setValue(0);
            }

        }

        public void updatePoints(){
            final String userId = getUid();

            FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("pointsEarned").setValue(totalPoints);
            FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("extraPoints").setValue(extraPoints);


        }

        public void adsSeen(){
            final String userId = getUid();
            FirebaseDatabase.getInstance().getReference().child("users").child(userId).addListenerForSingleValueEvent(
                    new com.google.firebase.database.ValueEventListener() {
                        @Override
                        public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            totalPoints = user.pointsEarned;
                            String str = Integer.toString(totalPoints);
                            TextView totalpoints = (TextView) getView().findViewById(R.id.totalPoints);
                            totalpoints.setText(str);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });

            FirebaseDatabase.getInstance().getReference().child("users").child(userId).addListenerForSingleValueEvent(
                    new com.google.firebase.database.ValueEventListener() {
                        @Override
                        public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            extraPoints = user.extraPoints;

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });


            TextView totalpoints = (TextView) getActivity().findViewById(R.id.totalPoints);

            int adSeen = extraPoints;
            switch ( adSeen) {
                case 0:
                    extraPoints = 1;
                    totalPoints++;
                    updatePoints();
                    maxed = false;
                    button.setEnabled( true );
                    Log.d("mymessage", "points" + extraPoints);
                    button.setVisibility( View.VISIBLE);
                    text.setVisibility(View.INVISIBLE);
                    String str = Integer.toString(totalPoints);
                    totalpoints.setText(str);
                    break;

               case 1:
                   extraPoints = 2;
                   totalPoints++;
                   updatePoints();
                   maxed = false;
                   button.setEnabled( true );
                   Log.d("mymessage", "points" + extraPoints);
                   button.setVisibility( View.VISIBLE);
                   text.setVisibility(View.INVISIBLE);
                   button.setEnabled(true);
                   str = Integer.toString(totalPoints);
                   totalpoints.setText(str);
                   break;

               case 2:
                   extraPoints = 3;
                   totalPoints++;
                   updatePoints();
                   maxed = true;
                   button.setEnabled( true );
                   Log.d("mymessage", "points" + extraPoints);
                   button.setVisibility( View.INVISIBLE);
                   text.setVisibility(View.VISIBLE);
                   updateTimeStamp();
                   str = Integer.toString(totalPoints);
                   totalpoints.setText(str);
                   break;

               default:
                   extraPoints++;
                   totalPoints++;
                   updatePoints();
                   maxed = false;
                   button.setVisibility( View.VISIBLE);
                   text.setVisibility(View.INVISIBLE);
                   button.setEnabled( true );
                   break;


           }

        }
    }
