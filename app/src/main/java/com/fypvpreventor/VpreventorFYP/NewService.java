package com.fypvpreventor.VpreventorFYP;

import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class NewService extends Service {

    // declaring object of MediaPlayer
public Ringtone r;
    @Override

    // execution of service will start
    // on calling this method
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(FakeCall.sound == 0){
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        }
        else {
            r = FakeCall.r;
        }
        r.play();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            r.setLooping(true);
        }


        return START_STICKY;
    }

    @Override

    // execution of the service will
    // stop on calling this method
    public void onDestroy() {
        super.onDestroy();
        r.stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}