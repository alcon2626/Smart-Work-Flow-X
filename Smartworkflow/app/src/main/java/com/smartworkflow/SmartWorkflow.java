package com.smartworkflow;

import android.content.Intent;

import com.firebase.client.Firebase;

/**
 * Created by LeoAizen on 9/26/2016.
 */

public class SmartWorkflow extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        startService(new Intent(this, SmartWorkflowService.class));
    }
}
