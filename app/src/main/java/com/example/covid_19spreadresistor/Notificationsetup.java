package com.example.covid_19spreadresistor;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Notificationsetup extends Application {
 static String notid="discovid19";
    @Override
    public void onCreate() {
        super.onCreate();

        setNotification();
    }

     private void setNotification() {

         if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
             NotificationChannel notificationChannel=new NotificationChannel(
                     notid,
                     "notificationcovid19",
                    NotificationManager.IMPORTANCE_DEFAULT
             );
             NotificationManager manager=getSystemService(NotificationManager.class);

             manager.createNotificationChannel(notificationChannel);

         }


     }
}
