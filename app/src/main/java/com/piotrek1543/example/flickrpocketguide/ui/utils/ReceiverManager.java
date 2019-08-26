package com.piotrek1543.example.flickrpocketguide.ui.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ReceiverManager {

    private static List<BroadcastReceiver> receivers = new ArrayList<BroadcastReceiver>();
    private static ReceiverManager ref;
    private Context context;

    private ReceiverManager(Context context){
        this.context = context;
    }

    public static synchronized ReceiverManager init(Context context) {      
        if (ref == null) ref = new ReceiverManager(context);
        return ref;
    }

    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter intentFilter){
        receivers.add(receiver);
        Intent intent = context.registerReceiver(receiver, intentFilter);
        Timber.i("registered receiver: " + receiver + "  with filter: " + intentFilter);
        Timber.i("receiver Intent: " + intent);
        return intent;
    }

    public boolean isReceiverRegistered(BroadcastReceiver receiver){
        boolean registered = receivers.contains(receiver);
        Timber.i("is receiver " + receiver + " registered? " + registered);
        return registered;
    }

    public void unregisterReceiver(BroadcastReceiver receiver){
        if (isReceiverRegistered(receiver)){
            receivers.remove(receiver);
            context.unregisterReceiver(receiver);
            Timber.i("unregistered receiver: " + receiver);
        }
    }
}