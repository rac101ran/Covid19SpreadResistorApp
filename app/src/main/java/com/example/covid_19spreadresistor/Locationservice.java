package com.example.covid_19spreadresistor;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static com.example.covid_19spreadresistor.Notificationsetup.notid;

public class Locationservice extends Service {
FirebaseAuth auth;
DatabaseReference datatbase,d2,databaseReference;
FirebaseUser user;
Map<String,Object> updatevaryingloc,l2,goingoutupload,locationuploadformap;
String addr,rssi,name;
String lat,lon;
String shieldstrength;
static int goingoutfrequecy=0;
static String postalcode="";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         updatevaryingloc=new HashMap<>();
         goingoutupload=new HashMap<>();
         locationuploadformap=new HashMap<>();
         l2=new HashMap<>();
         auth=FirebaseAuth.getInstance();
         user=FirebaseAuth.getInstance().getCurrentUser();
         if(user!=null) {
             datatbase = FirebaseDatabase.getInstance().getReference("People").child(user.getUid());
             Notification not = null;
             try {
                 if (!postalcode.equals("")) {
                     databaseReference = FirebaseDatabase.getInstance().getReference(postalcode).child(user.getUid());
                 }
                 d2 = datatbase.child("Bluetooth Exposure");
                 lat = intent.getStringExtra("lat");
                 lon = intent.getStringExtra("lon");
                 if (intent.getStringExtra("namedevice") == null || intent.getStringExtra("namedevice").equals("")) {
                     addr = intent.getStringExtra("Addr");
                     rssi = intent.getStringExtra("rssi");
                     l2.put(addr, rssi);

                 } else if (intent.getStringExtra("Addr") == null || intent.getStringExtra("Addr").equals("")) {
                     name = intent.getStringExtra("namedevice");
                     rssi = intent.getStringExtra("rssi");
                     l2.put(name, rssi);
                 }
                 Intent i = new Intent(Locationservice.this, MainActivity.class);
                 PendingIntent pendingintent = PendingIntent.getActivity(this, 0, i, 0);
                 not = new NotificationCompat.Builder(this, notid)
                         .setContentTitle("Covid 19 Spread Resistor").setContentText(lat + " \n " + lon).setSmallIcon(R.drawable.corona).setContentIntent(
                                 pendingintent
                         ).build();
                 updatevaryingloc.put("varying lat", lat);
                 updatevaryingloc.put("varying lon", lon);


                 datatbase.updateChildren(updatevaryingloc);
                 d2.updateChildren(l2);

                 datatbase.addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                         try {
                             if (!TextUtils.isEmpty(dataSnapshot.child("Latitude").getValue().toString()) && !TextUtils.isEmpty(dataSnapshot.child("Longitude").getValue().toString())) {
                                 Distanceofpositivecase distance = new Distanceofpositivecase(Double.parseDouble(dataSnapshot.child("Latitude").getValue().toString()),
                                         Double.parseDouble(dataSnapshot.child("Longitude").getValue().toString()), Double.parseDouble(lat), Double.parseDouble(lon));

                                 if (distance.distance() >= 0.200) {
                                     goingoutfrequecy++;
                                 }
                                 shieldstrength = dataSnapshot.child("shield strength").getValue().toString();
                                 postalcode = dataSnapshot.child("Postal Code").getValue().toString();
                             }

                         }catch(Exception e) {
                               e.printStackTrace();
                         }
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError databaseError) {

                     }
                 });

                 if (goingoutfrequecy > 0) {
                     updatevaryingloc.put("Going out frequency", goingoutfrequecy);
                     datatbase.updateChildren(updatevaryingloc);
                 }

                 locationuploadformap.put("lat", lat);
                 locationuploadformap.put("lon", lon);
                 locationuploadformap.put("shield strength",shieldstrength);
                 databaseReference.updateChildren(locationuploadformap);

             } catch (Exception e) {
                 e.printStackTrace();
             }
             startForeground(1, not);

             Log.e("postalcode",Locationservice.postalcode);
         }

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
