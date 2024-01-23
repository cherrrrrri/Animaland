package com.example.animaland.netConnect.netConnect;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @version
 * @Description: 网络物理连接状态管理类
 */
public class ConnectUtil {
    private static final String TAG = ConnectUtil.class.getSimpleName();

    /**
     * @Title: isNetworkConnected
     * @Description: 网络是否可用
     * @param @param context
     * @param @return
     * @return boolean
     */
    public static boolean isNetworkConnected(Context context) {
        System.out.println(TAG+"[Begin]");
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = cm.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    System.out.println(TAG+"[End]");
                    return true;
                } else {
                    continue;
                }
            }
        }
        System.out.println(TAG+"[End]");
        return false;
    }

    public static boolean isAvailable(Context context){
        ConnectivityManager cManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cManager.getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }
    //2）判断WIFI网络是否可用
    public static boolean isWifiConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if(info != null){
            if(info.isConnectedOrConnecting()){
                return info.getTypeName().equalsIgnoreCase("WIFI");
            }
        }
        return false;
    }
    //3）判断MOBILE网络是否可用
    public static boolean isMobileConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if(info != null){
            if(info.isConnectedOrConnecting()){
                return info.getTypeName().equalsIgnoreCase("MOBILE");
            }
        }
        return false;
    }
    // 4）获取当前网络连接的类型信息
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }
}
