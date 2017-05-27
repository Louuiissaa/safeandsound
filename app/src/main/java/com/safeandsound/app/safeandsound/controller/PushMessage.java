package com.safeandsound.app.safeandsound.controller;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.safeandsound.app.safeandsound.AppController;
import com.safeandsound.app.safeandsound.R;

/**
 * Created by louisapabst on 27.05.17.
 */

public class PushMessage {

    public void sendNotification(String message) {
        Context context = AppController.getInstance();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Safe & Sound")
                .setContentText(message);
        mBuilder.setTicker(message);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(666, mBuilder.build());
    }
}
