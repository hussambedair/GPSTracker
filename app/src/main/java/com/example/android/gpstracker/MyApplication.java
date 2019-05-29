package com.example.android.gpstracker;

import android.app.Application;

import com.example.android.gpstracker.OneSignal.MyNotificationOpenedHandler;
import com.example.android.gpstracker.OneSignal.MyNotificationReceivedHandler;
import com.onesignal.OneSignal;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler())
                .setNotificationReceivedHandler(new MyNotificationReceivedHandler())
                .init();
    }
}
