package com.example.jobseeker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.jobseeker.app.homePage.HomePage;
import com.example.jobseeker.parseSdk.Connect;

import me.pushy.sdk.Pushy;
import me.pushy.sdk.receivers.PushyBootReceiver;
import me.pushy.sdk.services.PushyFirebaseService;

public class PushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String notificationTitle = "Job Seeker";
        String notificationText = "Test notification";

        // Attempt to extract the "message" property from the payload: {"message":"Hello World!"}
        if (intent.getStringExtra("message") != null) {
            notificationText = intent.getStringExtra("message");
        }


        // Prepare a notification with vibration, sound and lights
        Notification notification = new NotificationCompat.Builder(context, Connect.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_notif)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        // Automatically configure a Notification Channel for devices running Android O+
        Pushy.setNotificationChannel(notification, context);

        // Get an instance of the NotificationManager service
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notification);

        // Build the notification and display it
        notificationManager.notify(1, notification);
    }
}
