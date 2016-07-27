package org.aguapoints.aguapointsapp;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
/*
 * Created by edgartrujillo on 7/12/16.
 */
public class DetailView extends Fragment{

    View myView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.detailview, container, false);

        String title = getActivity().getIntent().getStringExtra("title");
        Bitmap bitmap = getActivity().getIntent().getParcelableExtra("image");

        TextView titleTextView = (TextView) myView.findViewById(R.id.title);
        titleTextView.setText(title);

        ImageView imageView = (ImageView) myView.findViewById(R.id.image);
        imageView.setImageBitmap(bitmap);
        // get intent data


        return myView;

    }
/*

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailview);

        String title = getIntent().getStringExtra("title");
        Bitmap bitmap = getIntent().getParcelableExtra("image");

        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(title);

        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageBitmap(bitmap);

    }
*/

}
