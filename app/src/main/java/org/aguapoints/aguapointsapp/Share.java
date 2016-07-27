package org.aguapoints.aguapointsapp;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Share extends Fragment {

    DatabaseReference mDatabase;
    int alreadyShared;
    int pointsEarned;

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    CallbackManager callbackManager;


    ShareDialog shareDialog;

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_share, container,false);

        setUpScreen();

        final ShareDialog shareDialog = new ShareDialog(getActivity());
        Button fbShareButton = (Button) myView.findViewById(R.id.sharebtn);
        fbShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alreadyShare();
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("Agua Points App - Rewards for for supporting a cause to build water wells")
                            .setContentDescription(
                                    "Get rewarded for not using your phone while helping a cause to build water wells for those in need. Available now on App store and Google Play. ")
                            .setContentUrl(Uri.parse("http://www.aguapoints.org"))
                            .build();

                    shareDialog.show(linkContent);
                }

            }
        });


        return myView;
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public void setUpScreen(){
        final String userId = getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(userId).addListenerForSingleValueEvent(
                new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        pointsEarned = user.pointsEarned;
                        String str = Integer.toString(pointsEarned);
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
                        alreadyShared = user.FacebookShare;
                        if (alreadyShared == 0)
                        {
                            Button fbShareButton = (Button) myView.findViewById(R.id.sharebtn);
                            fbShareButton.setVisibility(View.VISIBLE);
                            TextView shareTxt = (TextView) getActivity().findViewById(R.id.sharetxt);
                            shareTxt.setVisibility(View.INVISIBLE);
                        }
                        else {
                            Button fbShareButton = (Button) myView.findViewById(R.id.sharebtn);
                            fbShareButton.setVisibility(View.INVISIBLE);
                            TextView shareTxt = (TextView) getActivity().findViewById(R.id.sharetxt);
                            shareTxt.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public void alreadyShare(){
        //Updates users points by +50
        //Hides share Button & displays text
        if (alreadyShared == 0) {
            alreadyShared = 1;
            pointsEarned += 50;
            String str = Integer.toString(pointsEarned);
            TextView totalpoints = (TextView) myView.findViewById(R.id.totalPoints);
            totalpoints.setText(str);

         // Updates Firebase with new points and facebook share...
            final String userId = getUid();
            FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("pointsEarned").setValue(pointsEarned);
            FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("FacebookShare").setValue(alreadyShared);

        }


    }

}