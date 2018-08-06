package com.gaminho.pi;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gaminho.pi.beans.Pupil;

import java.util.HashMap;

public class FireBaseService extends Service {

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    private int tes = -2;

    HashMap<String, Pupil> mPupils;
    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {

        public FireBaseService getService() {
            return FireBaseService.this;
        }
    }

    public int testBinder(long pTime){
        Log.d(getClass().getSimpleName(), "Just wanna test the binding with value: " + pTime);
        tes ++;
        return tes;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        Log.d(getClass().getSimpleName(), "Service created");
        super.onCreate();
    }
}
