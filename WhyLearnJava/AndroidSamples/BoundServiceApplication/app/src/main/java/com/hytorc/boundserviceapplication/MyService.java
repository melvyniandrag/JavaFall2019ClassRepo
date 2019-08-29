package com.hytorc.boundserviceapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Binder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyService extends Service {
    private final IBinder melvynsBinder = new MyLocalBinder();

    public MyService() {
    }

    public String getCurrentTime(){
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss", Locale.US );
        return df.format(new Date());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return melvynsBinder;
    }

    /*
     * Extends binder, binder means we can bind two objects
     */
    public class MyLocalBinder extends Binder{
        MyService getService(){
            return MyService.this;
        }
    }


}
