package com.example.jobseeker.parseSdk;

import android.app.Application;

import com.parse.Parse;


public class Connect extends Application {

    // Initializes Parse SDK as soon as the application is created
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
    }
}