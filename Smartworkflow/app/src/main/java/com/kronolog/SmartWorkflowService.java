package com.kronolog;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by SurfaceUser on 6/27/2017.
 */

public class SmartWorkflowService extends Service {
        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            // do your jobs here
            return super.onStartCommand(intent, flags, startId);
        }
}
