package com.kmitl.itl.enableandroid.service;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kmitl.itl.enableandroid.R;

public class BusNotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        RemoteMessage.Notification notification = remoteMessage.getNotification();

        if (notification != null) {
            //pending Intent
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, "bus")
                            .setAutoCancel(true)
                            .setContentTitle(notification.getTitle())
                            .setContentText(notification.getBody())
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), AudioManager.STREAM_NOTIFICATION)
                            .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                            .setColor(Color.YELLOW);


            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null)
                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
    }

}
