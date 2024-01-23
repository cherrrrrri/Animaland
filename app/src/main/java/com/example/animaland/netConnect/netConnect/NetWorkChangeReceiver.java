package com.example.animaland.netConnect.netConnect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NetWorkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!ConnectUtil.isNetworkConnected(context))
        {
            Toast.makeText(context, "检测不到可用的网络连接", Toast.LENGTH_LONG).show();

        }
        if(ConnectUtil.isWifiConnected(context))
        {
            Toast.makeText(context, "您正在连接Wi-Fi", Toast.LENGTH_LONG).show();
        }
        if(ConnectUtil.isMobileConnected(context))
        {
            Toast.makeText(context, "您正在使用移动网络", Toast.LENGTH_LONG).show();
        }
    }
}
