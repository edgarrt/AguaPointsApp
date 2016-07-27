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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class settings extends Fragment{


    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_settings, container,false);

        Button logout = (Button) myView.findViewById(R.id.logoutbutton);
        Button policy = (Button) myView.findViewById(R.id.policybutton);
        Button terms = (Button) myView.findViewById(R.id.termsbutton);
        Button problems = (Button) myView.findViewById(R.id.problembutton);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                LoginView();
            }
        });
        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pageView = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.iubenda.com/privacy-policy/7836898"));
                startActivity(pageView);

            }
        });


        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Pageview = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.aguapoints.org/#!terms-of-use/kq8qa"));
                startActivity(Pageview);

            }
        });


        problems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent pageview = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.aguapoints.org/#!help/lfclr"));
                startActivity(pageview);
            }
        });
    return myView;
    }

    private void LoginView() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}
