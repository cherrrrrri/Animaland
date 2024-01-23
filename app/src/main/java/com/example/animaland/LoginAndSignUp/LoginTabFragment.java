package com.example.animaland.LoginAndSignUp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.animaland.IMManager;
import com.example.animaland.Island;
import com.example.animaland.R;
import com.example.animaland.Signature.GenerateTestUserSig;
import com.example.animaland.tool.DatabaseHelper;
import com.example.animaland.tool.ThreadHelper;
import com.example.animaland.tool.User;
import com.example.animaland.tool.Utils;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMSDKListener;

import java.sql.SQLException;

public class LoginTabFragment extends Fragment {

    private static EditText userPhone;
    private static EditText userPwd;
    private static Button login;
    private static TextView forgetPass, tips_number, tips_pwd;
    V2TIMSDKListener sdkListener;
    private Activity mActivity= getActivity();
    private int sdkAppID=1400784977;
    User user = new User();


    private static float v = 0;
    DatabaseHelper db = new DatabaseHelper();
    Utils u = new Utils();
    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                Intent intent = new Intent(getActivity(), Island.class);
                startActivity(intent);
            }else if(msg.what == 1){
                tips_pwd.setVisibility(View.VISIBLE);
                tips_pwd.setText("密码不正确");
            }else if(msg.what == 2){
                tips_number.setVisibility(View.VISIBLE);
                tips_number.setText("用户不存在，请先注册");
            }else if (msg.what == 3){
                tips_number.setVisibility(View.INVISIBLE);//输入正确，隐藏提示文字
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        System.gc();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.page_login, container, false);

        findViewById(root);//找到控件
        anim();
        showInputTips();


        //设置“忘记密码”的监听事件
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转至“忘记密码”的界面
                Intent intent = new Intent(getActivity(), ForgetPass.class);
                startActivity(intent);
            }
        });

        //设置“登录”的监听事件
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //检测密码
                if(userPwd.getText().toString().length()==0){
                    tips_pwd.setVisibility(View.VISIBLE);
                    tips_pwd.setText("密码不能为空");
                }else{
                    tips_pwd.setVisibility(View.INVISIBLE);//输入正确，隐藏提示文字
                }

                if(userPhone.getText().toString().isEmpty()){
                    tips_number.setVisibility(View.VISIBLE);
                    tips_number.setText("手机号码不能为空");
                }

                //检测手机号
                if (!userPhone.getText().toString().isEmpty() && !userPwd.getText().toString().isEmpty()) {//手机号和密码都不为空
                  //  Thread threads = new Thread(new Runnable() {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            try {
                                if (db.existPhoneAndPass(userPhone.getText().toString(),userPwd.getText().toString())) {//查找手机和密码
                                    //找到手机和密码：登录成功
                                    //跳转至主地图

                                    login();
                                    user.tel=userPhone.getText().toString();
                                    user.name = db.findName(userPhone.getText().toString());//用户昵称
                                    System.out.println("登录"+user.tel);
                                    handler.sendEmptyMessage(0);
                                } else {//找不到：查找手机
                                    if(db.existPhone(userPhone.getText().toString())){
                                        //找到：密码不正确
                                        handler.sendEmptyMessage(1);
                                    }else{
                                        //找不到请先注册
                                       handler.sendEmptyMessage(2);
                                    }
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (java.lang.InstantiationException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    ThreadHelper.cachedThreadPool.submit(runnable);

                }
            }
        });
        return root;
    }


    private void findViewById(ViewGroup root){
        //找到控件
        userPhone=root.findViewById(R.id.phoneNumber);
        userPwd=root.findViewById(R.id.pwd);
        forgetPass=root.findViewById(R.id.forgetPass);
        login=root.findViewById(R.id.login);
        tips_number=root.findViewById(R.id.tips_phoneNumber);
        tips_pwd=root.findViewById(R.id.tips_pwd);

    }
    private void showInputTips(){
        //设置提示文字

        //检查手机号码
        userPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(userPhone.hasFocus()==false){
                    if(userPhone.getText().toString().isEmpty()){
                        tips_number.setVisibility(View.VISIBLE);
                        tips_number.setText("手机号码不能为空");
                    }else {//如果不为空
                        if (!u.checkTel(userPhone.getText().toString())) {//手机格式不正确
                            tips_number.setVisibility(View.VISIBLE);
                            tips_number.setText("手机号码格式错误");
                        } else {//手机格式正确
                           // Thread threads = new Thread(new Runnable() {
                             Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (!db.existPhone(userPhone.getText().toString())) {//手机号不存在：先注册
                                            handler.sendEmptyMessage(2);
                                        }else{
                                            tips_number.setVisibility(View.INVISIBLE);//输入正确，隐藏提示文字
                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (java.lang.InstantiationException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            ThreadHelper.cachedThreadPool.submit(runnable);

                        }
                    }
                }
            }
        });

        //检查密码
        userPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (userPwd.hasFocus() == false) {
                    LoginAndSignUp.background.setImageResource(R.drawable.buding_open);
                    if (userPwd.getText().toString().length() == 0) {
                        tips_pwd.setVisibility(View.VISIBLE);
                        tips_pwd.setText("密码不能为空");
                    } else
                        tips_pwd.setVisibility(View.INVISIBLE);//输入正确，隐藏提示文字
                }else{
                    LoginAndSignUp.background.setImageResource(R.drawable.buding_close);
                }
            }
            });


    }

    public static void anim(){
        //设置动画
        userPhone.setTranslationY(500);
        userPwd.setTranslationY(500);
        forgetPass.setTranslationY(500);
        login.setTranslationY(500);
        tips_number.setTranslationY(500);
        tips_pwd.setTranslationY(500);

        userPhone.setAlpha(v);
        userPwd.setAlpha(v);
        forgetPass.setAlpha(v);
        login.setAlpha(v);
        tips_number.setAlpha(v);
        tips_pwd.setAlpha(v);

        userPhone.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(0).start();
        tips_number.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(0).start();
        userPwd.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(200).start();
        tips_pwd.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(200).start();
        forgetPass.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        login.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();

    }



    /**
     * 登录这里的id和sig分别为userid和用userID加密后生成的sign（服务端提供）
     * 这里的userid可以替换为自己的userid
     * usersign可以使用开发者后台提供的生成工具生成测试（生成工具地址：https://console.cloud.tencent.com/im-detail/tool-usersig）
     * 具体正式usersign需要服务端生成后返回
     */
    public void login() {
        Log.i("腾讯云即时通信IM", "登录的账号为：" +userPhone.getText().toString());
        Log.i("腾讯云即时通信IM", "登录的密码为：" + GenerateTestUserSig.genTestUserSig(userPhone.getText().toString()));
        String UserID=userPhone.getText().toString();
        String UserSig=GenerateTestUserSig.genTestUserSig(UserID);
        IMManager.login(UserID, UserSig, new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {
                Log.i("腾讯云即时通信IM", "登录失败，错误码为" + i + "，原因为：" + s);
            }

            @Override
            public void onSuccess() {
                Log.i("腾讯云即时通信IM", "登录成功，即將加入房间");
                startActivity(new Intent(getActivity(), Island.class));
            }
        });
    }

    public void onDestroyView() {
        super.onDestroyView();
    }



}