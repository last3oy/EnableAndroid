package com.kmitl.itl.enableandroid.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kmitl.itl.enableandroid.MyApp;
import com.kmitl.itl.enableandroid.R;

public class BusNotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        String isArriveValue = remoteMessage.getData().get("isArrive");
        if (isArriveValue != null) {
            Boolean isArrive = Boolean.valueOf(isArriveValue);
            if (isArrive) {
                MyApp.getInstance().unsubscribeBus();
            }
        }

        if (notification != null) {

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, "0")
                            .setSmallIcon(R.drawable.ic_bus)
                            .setContentTitle(notification.getTitle())
                            .setContentText(notification.getBody())
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri);


            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                if (Build.VERSION.SDK_INT >= 26) {
                    NotificationChannel channel = new NotificationChannel("0", "bus_channel",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.createNotificationChannel(channel);

                    notificationBuilder.setChannelId("0");
                }
                notificationManager.notify(0, notificationBuilder.build());
            }
        }
    }

}
