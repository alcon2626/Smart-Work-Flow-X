package com.kronolog;

import android.content.Intent;

import com.firebase.client.Firebase;

/**
 * Created by LeoAizen on 9/26/2016.
 */

public class KronoLog extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        startService(new Intent(this, SmartWorkflowService.class));
    }
}
