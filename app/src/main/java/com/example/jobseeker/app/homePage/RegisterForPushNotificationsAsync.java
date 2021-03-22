package com.example.jobseeker.app.homePage;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseUser;

import java.lang.ref.WeakReference;
import java.net.URL;

import me.pushy.sdk.Pushy;

import static com.parse.Parse.getApplicationContext;

class RegisterForPushNotificationsAsync extends AsyncTask<Void, Void, Object> {
    private WeakReference<Activity> activityRef;

    public RegisterForPushNotificationsAsync(Activity activity) {
        activityRef = new WeakReference<>(activity);
    }

    protected Object doInBackground(Void... params) {
        try {
            // Register the device for notifications
            String deviceToken = Pushy.register(getApplicationContext());

            // Registration succeeded, log token to logcat
            Log.d("Pushy", "Pushy device token: " + deviceToken);

            // Send the token to your backend server via an HTTP GET request
            ParseUser.getCurrentUser().put("pushyDeviceToken", deviceToken);

            ParseUser.getCurrentUser().saveEventually(e -> {
                if (e==null){
                    Toast.makeText(getApplicationContext(), "token is saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            // Provide token to onPostExecute()
            return deviceToken;
        }
        catch (Exception exc) {
            // Registration failed, provide exception to onPostExecute()
            return exc;
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        String message;

        // Registration failed?
        if (result instanceof Exception) {
            // Log to console
            Log.e("Pushy", result.toString());

            // Display error in alert
            message = ((Exception) result).getMessage();
        }
        else {
            message = "Pushy device token: " + result.toString() + "\n\n(copy from logcat)";
        }

        Activity activity = activityRef.get();
        // Registration succeeded, display an alert with the device token
        new android.app.AlertDialog.Builder(activity)
                .setTitle("Pushy")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}