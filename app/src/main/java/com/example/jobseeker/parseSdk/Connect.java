package com.example.jobseeker.parseSdk;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import com.parse.Parse;


public class Connect extends Application {

    // Initializes Parse SDK as soon as the application is created
    public static final String CHANNEL_1_ID = "channel";

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("5Qnz0e83vi8p7xiQZaXfWV2B9gTZzpBaHlo00gKf")
                .clientKey("Cx1a454KK3J9VnTBPR0nQGPbXS5EQzHbqrFhIWrH")
                .server("https://parseapi.back4app.com")
                .maxRetries(0)
                .build()
        );

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        NotificationChannel channel1 = new NotificationChannel(
                CHANNEL_1_ID, "Channel1", NotificationManager.IMPORTANCE_HIGH
        );
        channel1.setDescription("This is test notification");
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel1);
    }
}