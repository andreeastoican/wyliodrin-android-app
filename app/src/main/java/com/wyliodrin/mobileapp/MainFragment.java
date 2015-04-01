package com.wyliodrin.mobileapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wyliodrin.mobileapp.api.WylioBoard;

public class MainFragment extends Fragment {

    public MainFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Simple Message
        final WylioBoard board = new WylioBoard("250a7843-9eda-4a92-942d-dba0dfe3b0a0b836cb66-0ad1-46d6-b6ba-458781a38ca08ced6536-0a8a-49b0-bbf3-b072631d0ccf", "andreea.stoican.5_random@wyliodrin.com");

        //final WylioBoard board = new WylioBoard("b883db27-a072-4481-aff2-2e5297d781c80a6febc0-6263-4118-9eeb-2fbb0530585b46a813ed-7eda-49b4-be5e-4a319c6c0da13a155792-fc2e-413f-82d9-9728ca5b829a");

        return rootView;
    }

}
