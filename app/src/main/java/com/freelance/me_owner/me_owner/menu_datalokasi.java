package com.freelance.me_owner.me_owner;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by UutBT on 11/2/2018.
 */

public class menu_datalokasi extends Fragment {

    public menu_datalokasi(){}
    RelativeLayout view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = (RelativeLayout) inflater.inflate(R.layout.menu_datalokasi, container, false);


        getActivity().setTitle("Data Lokasi Usaha");

        return view;
    }
}
