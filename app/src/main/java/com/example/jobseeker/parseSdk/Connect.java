package com.example.jobseeker.parseSdk;

import android.app.Application;

import com.parse.Parse;


public class Connect extends Application {

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("gRw1Gi6IbKyW5gzLO9Z4ELrn8yc4bR23yHt9LdeT")
                .clientKey("m6vU8Jrlji7N8gt7XeCjBXUjZ4KqrtzEO7hzz6Rl")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}