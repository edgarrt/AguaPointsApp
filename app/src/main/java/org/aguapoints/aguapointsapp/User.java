package org.aguapoints.aguapointsapp;

import com.google.firebase.database.IgnoreExtraProperties;

/*
 * Created by edgartrujillo on 5/30/16.
 */
@IgnoreExtraProperties
public class User {
    public String username;

    public String email;

    public int pointsEarned;

    public int extraPoints;

    public int timeStamp;

    public int FacebookShare;

    public int CurrentAdsSeen = 0;

    public String online = "yes";




    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, int pointsEarned, int extraPoints, int timeStamp, String online, int
                 FacebookShare, int CurrentAdsSeen) {
        this.username = username;
        this.email = email;
        this.extraPoints = extraPoints;
        this.CurrentAdsSeen = CurrentAdsSeen;
        this.FacebookShare = FacebookShare;
        this.online = online;
        this.timeStamp = timeStamp;
        this.pointsEarned = pointsEarned;
    }

}