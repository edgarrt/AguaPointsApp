package org.aguapoints.aguapointsapp;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

/*
 * Created by edgartrujillo on 7/12/16.
 */
public class ShopFragment extends Fragment {
    private GridView gridView;
    private GridViewAdapter gridAdapter;

    View myView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_shop, container, false);

        gridView = (GridView) myView.findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter( getActivity(), R.layout.rewardview, getData());
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);

                //Create intent
                Intent intent = new Intent(getActivity(), DetailView.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("image", item.getImage());
                startActivity(intent);
            }
        });

        return myView;
    }


    String [] items = {
            "Amazon $50 Giftcard","GoPro HERO","Nixon Watch","PlayStation 4","Sephora $50 Giftcard","Ulta $50 Giftcard","Amazon $10 Giftcard",
            "Best Buy $20 Giftcard","Sephora $25 Giftcard","Victoria Secrets $25 Giftcard",
            "Target $10 Giftcard", "Buffalo Wild Wings $20 Giftcard", "Chick-fil-A $15 Giftcard ","In-N-Out $15 Giftcard",
            "PizzaHut $15 Giftcard","Starbucks $10 Giftcard"
    };

    private ArrayList<ImageItem> getData() {


        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        imgs.recycle();
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            imageItems.add(new ImageItem(bitmap, items[i]));
        }
        return imageItems;
    }
}