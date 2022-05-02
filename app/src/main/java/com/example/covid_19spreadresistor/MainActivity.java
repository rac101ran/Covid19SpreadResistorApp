package com.example.covid_19spreadresistor;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.CheckBox;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    LocationManager manager;
    LocationListener listener;
    String lat,lon;
    FirebaseAuth auth;
    private AppBarConfiguration mAppBarConfiguration;
    CheckBox b1, b2, b3, b4, bc1, bc2, bc3, bc4;
    DatabaseReference reference,ref2;
    BluetoothAdapter adapter;
    String devicename="";
    String addr="";
    String rssi="";
    int f;


    FirebaseUser user;
    double l02;
    Map<String,Object> map=new HashMap<>();
    private int goingoutfrequency=0;
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Intent i = new Intent(MainActivity.this, Welcomepage.class);
            startActivity(i);
            finish();
        }
    }


    BroadcastReceiver broadcastrecieve =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();

            if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {

            }

          else  if(action.equals(BluetoothDevice.ACTION_FOUND)) {


                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    try {
                        devicename = device.getName().toString();
                        addr = device.getAddress().toString();
                        rssi = String.valueOf(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));
                    }catch(Exception e) {
                           e.printStackTrace();
                    }
                    if (devicename==null || devicename.equals("")) {
                           f=0;
                    } else if (addr==null || addr.equals("")) {
                             f=1;
                    }

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        auth = FirebaseAuth.getInstance();

        if (user != null) {
            user = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference("People").child(user.getUid());
            ref2 = FirebaseDatabase.getInstance().getReference("People").child(user.getUid()).child("Longitude");
        }
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);


       /* fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.hotspotfragment,R.id.hotspotnearpc,R.id.help)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.nav_home) {
                    Intent i = new Intent(MainActivity.this, Homepage.class);
                    startActivity(i);

                }
                if (destination.getId() == R.id.nav_gallery) {
                    Intent i = new Intent(MainActivity.this, Shieldstrength.class);
                    startActivity(i);

                }
                if (destination.getId() == R.id.nav_slideshow) {
                    Intent i = new Intent(MainActivity.this, Diagnosiscall.class);
                    startActivity(i);
                }
                if(destination.getId()==R.id.hotspotnearpc) {
                     Intent i=new Intent(MainActivity.this,MapsActivity.class);
                      startActivity(i);
                }
            }
        });
        new AlertDialog.Builder(this).setIcon(R.drawable.ic_local_hospital_black_24dp).setTitle("Ready for check up?").setMessage(
                "Click yes to Continue").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(MainActivity.this, Dailysynptomscheck.class);
                        startActivity(i);
                    }
                }
        ).setNegativeButton("No",null).show();


        if (Build.VERSION.SDK_INT >= 26 ) {
            manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            listener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    locationupdates(location);
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

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
                Location l = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (l != null) {
                    locationupdates(l);
                }
            }



        }




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




    public void locationupdates(Location location) {
        lat=Double.toString(location.getLatitude());
        lon=Double.toString(location.getLongitude());
        Intent intent=new Intent(MainActivity.this,Locationservice.class);
        intent.putExtra("lat",lat);
        intent.putExtra("lon",lon);
        adapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(broadcastrecieve, filter);

        if(f==0) {
            intent.putExtra("Addr", addr);
            intent.putExtra("rssi",rssi);
            intent.putExtra("namedevice","");
        }
        else if(f==1) {
            intent.putExtra("namedevice", devicename);
            intent.putExtra("rssi",rssi);
            intent.putExtra("Addr","");
        }
        if(adapter!=null) {
            adapter.startDiscovery();
        }

        startForegroundService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (R.id.action_settings == item.getItemId()) {
            Intent i = new Intent(MainActivity.this, Profilesettings.class);
            startActivity(i);
            return true;
        }
        if (R.id.logout == item.getItemId()) {
            auth.signOut();
            Intent i = new Intent(MainActivity.this, Welcomepage.class);
            startActivity(i);
            finish();
            return true;

        }
        if(R.id.bluetoothexposure==item.getItemId()) {
            Intent i = new Intent(MainActivity.this,Bluetoohpositivecheck.class);
            startActivity(i);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
