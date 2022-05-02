package com.example.covid_19spreadresistor;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Profilesettings extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
EditText name;
EditText phn_no;
EditText address;
EditText postalcode;
static String pc="";
Button b;
DatabaseReference reference,r2;
Map<String,String> info;
LocationListener listener;
LocationManager manager;
String locality="";
    String addressfine="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilesettings);
        name = findViewById(R.id.nameid);
        phn_no = findViewById(R.id.phoneid);
        address = findViewById(R.id.addressid);
        postalcode = findViewById(R.id.postalid);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        info = new HashMap<>();
        reference = FirebaseDatabase.getInstance().getReference("People").child(user.getUid());
        b = findViewById(R.id.uploadid);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(address.getText().toString()) && !TextUtils.isEmpty(phn_no.getText().toString()) && !TextUtils.isEmpty(name
                        .getText().toString()) && !TextUtils.isEmpty(postalcode.getText().toString())) {


                    info.put("Name", name.getText().toString());
                    info.put("Address", address.getText().toString());
                    info.put("Postal Code", postalcode.getText().toString());
                    info.put("Phone Number", phn_no.getText().toString());

                    manager = (LocationManager) Profilesettings.this.getSystemService(Context.LOCATION_SERVICE);
                    listener = new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                             locationupdate(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    };

                    if (ContextCompat.checkSelfPermission(Profilesettings.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Profilesettings.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                    } else {
                        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
                        Location l = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (l != null) {
                                locationupdate(l);
                        }
                    }
                }
            }
        });



    }
    @Override
    public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
                                             @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
            }

        }
    }
    public void locationupdate(Location location) {
        info.put("Latitude",String.valueOf(location.getLatitude()));
        info.put("Longitude",String.valueOf(location.getLongitude()));
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addr = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if(addr!=null) {
                if (addr.size() > 0 && addr.get(0).getLocality() != null) {
                    addressfine += addr.get(0).getLocality();

                }
                if (addr.size() > 0 && addr.get(0).getThoroughfare()!=null) {
                    addressfine+=addr.get(0).getThoroughfare();
                }
                if(addr.size() >0 && addr.get(0).getThoroughfare()!=null) {
                    addressfine+=addr.get(0).getSubLocality();
                }
                if(addr.size()>0 && addr.get(0).getPostalCode()!=null) {
                     pc=addr.get(0).getPostalCode();
                }

            }

        }catch(Exception e) {
                e.printStackTrace();

        }
        info.put("Location trace",addressfine);
        reference.setValue(info);

    }
}
