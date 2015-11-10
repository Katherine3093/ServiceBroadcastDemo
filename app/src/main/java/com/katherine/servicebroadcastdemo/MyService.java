package com.katherine.servicebroadcastdemo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.os.Process;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class MyService extends Service {

    LocalBinder mBinder;

    HandlerThread mHandlerThread;

    Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new LocalBinder();

        mHandlerThread = new HandlerThread("ServiceThread", Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();

        mHandler = new Handler(mHandlerThread.getLooper());

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void sendMessage(final String message) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.v("Service", message);

                Intent intent = new Intent("RECEIVE_MSG");

                intent.putExtra("result", message);

                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
            }
        });
    }

    public Context getContext() {
        return this;
    }

    public class LocalBinder extends Binder {

        MyService getService() {
            return MyService.this;
        }
    }
}
