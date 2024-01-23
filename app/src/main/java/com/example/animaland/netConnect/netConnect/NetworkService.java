package com.example.animaland.netConnect.netConnect;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class NetworkService extends Service {

    private NetWorkChangeReceiver receiver;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        registerReceiver();
    }

    private void registerReceiver(){
        IntentFilter broadcastFilter=new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        receiver=new NetWorkChangeReceiver();
        registerReceiver(receiver,broadcastFilter);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if(receiver!=null){
            unregisterReceiver(receiver);
        }

    }
}