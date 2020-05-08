package com.rumooursindoyo.moheeeetgupta;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {


    public NotificationFragment() {
        // Required empty public constructor
    }
    private CardView cardViewlocal;
    private CardView cardViewglobal;

    private CardView cardViewsuggest;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate (R.layout.fragment_notification, container, false);

                cardViewlocal =(CardView)view.findViewById (R.id.Local);
        cardViewlocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent localupdateIntent = new Intent(getActivity (),Local_Activity.class );
                startActivity(localupdateIntent);

            }
        });
         cardViewglobal =(CardView)view.findViewById (R.id.global);
        cardViewglobal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent globalupdateIntent = new Intent(getActivity (),global.class);
                startActivity(globalupdateIntent);

            }
        });


    return  view;
    }
}
