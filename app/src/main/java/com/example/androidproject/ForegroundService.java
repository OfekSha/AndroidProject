package com.example.androidproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class ForegroundService extends Service {
    private Calendar ordertime;
    private class Worker extends Thread {
        int notificationTime=60;
        @Override
        public void run() {
            Calendar now=Calendar.getInstance();
            while (now.before(ordertime)) {
                now.add(Calendar.MINUTE, notificationTime);
                if (now.after(ordertime)){
                    updateNotification("less then "+notificationTime+" minutes");
                    notificationTime/=2;
                }

                now = Calendar.getInstance();
            }
            stopForeground(true);


        }
    }
    private void updateNotification(String text){
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Table ordering service reminder")
                .setContentText(text)
                .setSmallIcon(R.drawable.table)
                .setOngoing(true)
                .build();
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.notify(1,notification);
    }
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra"); // 00:00
        String hour=input.substring(0,input.indexOf(":"));
        String minute=input.substring(input.indexOf(":")+1);
        ordertime=Calendar.getInstance();
        ordertime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hour));
        ordertime.set(Calendar.MINUTE, Integer.valueOf(minute));
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, BaseActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Table ordering service reminder")
                .setContentText("you start the order at: "+Calendar.getInstance().getTime())
                .setSmallIcon(R.drawable.table)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        Worker worker=new Worker();
        worker.start();
        return START_NOT_STICKY;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
