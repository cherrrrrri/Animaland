package com.example.animaland;


import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.example.animaland.LoginAndSignUp.LiquidFragment;
import com.example.animaland.tool.BasicActivity;
import com.jem.liquidswipe.LiquidSwipeViewPager;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.imsdk.v2.V2TIMSDKListener;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

public class MainActivity extends BasicActivity implements CancelAdapt {


    ImageView logo,bg;
    TextView name;
    LottieAnimationView lottieAnimationView,lottieAnimationView2;
    private Activity mActivity= MainActivity.this;
    private int sdkAppID=1400784977;
    final List<Fragment> startfragments = new ArrayList<>();
    private void firstRun() {
        SharedPreferences sharedPreferences = getSharedPreferences("FirstRun",0);
        Boolean first_run = sharedPreferences.getBoolean("First",true);
        if (first_run){
            sharedPreferences.edit().putBoolean("First",false).commit();
            //第一次下载时所需经历的Activity

        }
        else {

        }
    }

    public static String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_introductory);
        playMusic();
        init();
        firstRun();
        //找到控件
        name=findViewById(R.id.app_name);
        logo=findViewById(R.id.app_logo);
        bg=findViewById(R.id.app_bg);
        lottieAnimationView=findViewById(R.id.lottie);

        name.animate().translationY(2200).setDuration(1000).setStartDelay(2000);
        logo.animate().translationY(2200).setDuration(1000).setStartDelay(2000);
        bg.animate().translationY(-3500).setDuration(1000).setStartDelay(2000);
        lottieAnimationView.animate().translationY(2200).setDuration(2000).setStartDelay(2000);


        //液体过渡
        LiquidSwipeViewPager pager = (LiquidSwipeViewPager) findViewById(R.id.pager);
        startfragments.add(new Fragment(R.layout.liquid1));
        startfragments.add(new Fragment(R.layout.liquid2));
        startfragments.add(new LiquidFragment(R.layout.liquid3));
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return startfragments.get(position);
            }

            @Override
            public int getCount() {
                return startfragments.size();
            }
        });


    }



    /**
     * IM初始化
     */
    private void init() {
        //腾讯云IM
        V2TIMSDKConfig config = new V2TIMSDKConfig();
        // 3. 指定 log 输出级别，详情请参考 SDKConfig。
        config.setLogLevel(V2TIMSDKConfig.V2TIM_LOG_INFO);
        // 4. 初始化 SDK 并设置 V2TIMSDKListener 的监听对象。
        IMManager.initConnectTXService(mActivity, sdkAppID, config, new V2TIMSDKListener() {
            @Override
            public void onConnecting() {
                super.onConnecting();
                Log.i("腾讯云即时通信IM", "正在连接腾讯云服务器");
            }

            @Override
            public void onConnectSuccess() {
                super.onConnectSuccess();
                Log.i("腾讯云即时通信IM", "连接腾讯云服务器成功");
            }

            @Override
            public void onConnectFailed(int code, String error) {
                super.onConnectFailed(code, error);
                Log.i("腾讯云即时通信IM", "连接腾讯云服务器失败");
            }

            @Override
            public void onKickedOffline() {
                super.onKickedOffline();
                Log.i("腾讯云即时通信IM", "当前用户被踢下线");
                //  login();
            }

            @Override
            public void onUserSigExpired() {
                super.onUserSigExpired();
                Log.i("腾讯云即时通信IM", "登录票据已经过期");
                //   login();
            }
        });
    }

    public void onStop() {
        super.onStop();
    }

}
