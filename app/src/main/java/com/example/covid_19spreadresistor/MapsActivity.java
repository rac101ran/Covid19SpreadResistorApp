package com.example.covid_19spreadresistor;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            if (Locationservice.postalcode != null || !Locationservice.postalcode.equals("")) {
                reference = FirebaseDatabase.getInstance().getReference(Locationservice.postalcode);
            }
            reference = FirebaseDatabase.getInstance().getReference("110070");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                    while (it.hasNext()) {
                        DataSnapshot id = it.next();
                        if (id.child("shield strength").exists() && Double.parseDouble(id.child("shield strength").getValue().toString()) >= 200) {

                            LatLng l = new LatLng(Double.parseDouble(id.child("lat").getValue().toString()), Double.parseDouble(id.child("lon").getValue().toString()));
                            mMap.addMarker(new MarkerOptions().position(l).title(id.child("name").getValue().toString()).icon(BitmapDescriptorFactory.defaultMarker(
                                    BitmapDescriptorFactory.HUE_GREEN)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l, 19.5f));

                        } else if (id.child("shield strength").exists() && Double.parseDouble(id.child("shield strength").getValue().toString()) >= 100 &&
                                Double.parseDouble(id.child("shield strength").getValue().toString()) < 200) {

                            LatLng l = new LatLng(Double.parseDouble(id.child("lat").getValue().toString()), Double.parseDouble(id.child("lon").getValue().toString()));
                            mMap.addMarker(new MarkerOptions().position(l).title(id.child("name").getValue().toString()).icon(
                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l, 19.5f));

                        } else if (id.child("shield strength").exists()) {

                            LatLng l = new LatLng(Double.parseDouble(id.child("lat").getValue().toString()), Double.parseDouble(id.child("lon").getValue().toString()));
                            mMap.addMarker(new MarkerOptions().position(l).title(id.child("name").getValue().toString()).icon(
                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l, 19.5f));
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}


