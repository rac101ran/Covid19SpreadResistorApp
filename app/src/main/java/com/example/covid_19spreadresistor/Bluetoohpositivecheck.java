package com.example.covid_19spreadresistor;
import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Bluetoohpositivecheck extends AppCompatActivity {
    BluetoothAdapter adapterbluetooth,ad;
    FirebaseAuth auth;
    FirebaseUser user;
    Button searchbutton;
    ArrayList<String> ive_info;
    ArrayAdapter<String> adapter;
    BluetoothDevice devicee;
    ListView list;
    Map<String,Object> updateexposer,my;
    DatabaseReference ref,ref2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetoohpositivecheck);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        searchbutton = findViewById(R.id.button2);
        ref = FirebaseDatabase.getInstance().getReference("People").child(user.getUid()).child("Bt device exposer");
        ref2=FirebaseDatabase.getInstance().getReference("People").child(user.getUid()).child("My bluetooth device");
        updateexposer = new HashMap<>();
        my=new HashMap<>();
        ive_info = new ArrayList<>();

        list = findViewById(R.id.lisid);
        adapterbluetooth = BluetoothAdapter.getDefaultAdapter();
        ad=BluetoothAdapter.getDefaultAdapter();

        IntentFilter intentfilter = new IntentFilter();

        intentfilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentfilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentfilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentfilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(br, intentfilter);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ive_info);
        list.setAdapter(adapter);
    }


    private final BroadcastReceiver br=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
              String action=intent.getAction();
              if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                  searchbutton.setEnabled(true);
              } else if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                  BluetoothDevice DEVICE = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                  String name = DEVICE.getName();
                  String addr = DEVICE.getAddress();
                  String rssi = String.valueOf(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));
                  if (name == null || name.equals("")) {
                      if(!ive_info.contains(addr + " " + rssi)) {
                          ive_info.add(addr + " " + rssi);
                          updateexposer.put(addr, rssi);
                      }
                  } else {
                      if(!ive_info.contains(name + " " + rssi)) {
                          ive_info.add(name + " " + rssi);
                          updateexposer.put(name, rssi);
                      }
                  }
                     adapter.notifyDataSetChanged();
                     ref.updateChildren(updateexposer);
              }
        }
    };
      public void find(View view) {

          searchbutton.setEnabled(false);
          adapterbluetooth.startDiscovery();
         my.put("Device name",Settings.Secure.getString(getContentResolver(),"bluetooth_name"));
          Toast.makeText(this,android.provider.Settings.Secure.getString(getApplicationContext().getContentResolver(),"bluetooh_address"), Toast.LENGTH_SHORT).show();
         ref2.setValue(my);
      }


}
