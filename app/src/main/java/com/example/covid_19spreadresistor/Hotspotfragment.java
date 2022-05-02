package com.example.covid_19spreadresistor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class Hotspotfragment extends Fragment {

    public Hotspotfragment() {
        // Required empty public constructor
    }


    public static Hotspotfragment newInstance(String param1, String param2) {
        Hotspotfragment fragment = new Hotspotfragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        return inflater.inflate(R.layout.fragment_hotspotfragment, container, false);
    }
}
