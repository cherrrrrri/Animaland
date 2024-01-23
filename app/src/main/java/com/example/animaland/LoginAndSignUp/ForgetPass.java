package com.example.animaland.LoginAndSignUp;

import static com.example.animaland.tool.Utils.isContainAll;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewStructure;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.animaland.Island;
import com.example.animaland.R;
import com.example.animaland.tool.DatabaseHelper;
import com.example.animaland.tool.ThreadHelper;
import com.example.animaland.tool.User;
import com.example.animaland.tool.Utils;
import com.mob.MobSDK;

import java.sql.SQLException;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import me.jessyan.autosize.internal.CancelAdapt;

public class ForgetPass extends Activity implements CancelAdapt {

    private EditText userPhone,identifyCode,userPwd1,userPwd2;
    private Button sendCode,login,back;
    private TextView tips_number,tips_identifyCode,tips_userPwd1,tips_userPwd2;
    DatabaseHelper db = new DatabaseHelper();
    Utils u = new Utils();

    final ForgetPass.MyCountDownTimer myCountDownTimer = new ForgetPass.MyCountDownTimer(60000, 1000);

    boolean checkCode = false;
    boolean checkPhone = false;
    boolean checkPwd1 = false;
    boolean checkPwd2 = false;

    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (msg.what == 0) {
                tips_number.setVisibility(View.VISIBLE);
                tips_number.setText("手机号不存在，请先注册");
            }else if(msg.what == 1){
                Intent intent=new Intent(ForgetPass.this, Island.class);
                startActivity(intent);
                Toast.makeText(ForgetPass.this,"欢迎回来！",Toast.LENGTH_SHORT).show();
            }else if(msg.what == 2){
                tips_identifyCode.setVisibility(View.VISIBLE);
                tips_identifyCode.setText("验证成功");
            }else if(msg.what == 3){
                myCountDownTimer.start();
                tips_identifyCode.setVisibility(View.VISIBLE);
                tips_identifyCode.setText("已发送验证码，请注意查收");
            }else if(msg.what == 4){
                tips_identifyCode.setVisibility(View.VISIBLE);
                tips_identifyCode.setText("验证码错误");
            }else if(msg.what == 5){
                tips_identifyCode.setVisibility(View.VISIBLE);
                tips_identifyCode.setText("验证码长度不正确");
            }else if(msg.what == 6){
                tips_identifyCode.setVisibility(View.INVISIBLE);//输入正确，隐藏提示文字
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_forgetpass);

        findViewById();//找到控件
        showInputTips();//显示提示文字

        MobSDK.submitPolicyGrantResult(true, null);//调用smssdk接口
        register();

        //设置“登录“的监听事件
        login.setOnClickListener(new View.OnClickListener() {
            boolean flag = true;            //判断用户是否已存在的标志位
            @Override
            public void onClick(View v) {
                //检查手机
                if(userPhone.getText().toString().isEmpty()){
                    tips_number.setVisibility(View.VISIBLE);
                    tips_number.setText("手机号码不能为空");
                }else {//如果不为空
                    if (!u.checkTel(userPhone.getText().toString())) {//手机格式不正确
                        tips_number.setVisibility(View.VISIBLE);
                        tips_number.setText("手机号码格式错误");
                    } else {//手机格式正确
                        //  Thread threads = new Thread(new Runnable() {
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (!db.existPhone(userPhone.getText().toString())) {//手机号不存在：先注册
                                        handler.sendEmptyMessage(0);
                                    }else{
                                        handler.sendEmptyMessage(6);
                                        //     checkPhone = true;
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

                //检查验证码
                if (identifyCode.hasFocus() == false) {
                    if (identifyCode.getText().toString().length() != 6) {
                        handler.sendEmptyMessage(5);
                    } else {
                        handler.sendEmptyMessage(6);
                        // Thread threads = new Thread(new Runnable() {
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                SMSSDK.submitVerificationCode("86", userPhone.getText().toString(), identifyCode.getText().toString());//提交验证码
                            }
                        };
                        ThreadHelper.cachedThreadPool.submit(runnable);
                        if(!checkCode)
                            handler.sendEmptyMessage(4);
                    }
                }

                //检查密码1
                if(userPwd1.getText().toString().length() == 0){
                    tips_userPwd1.setVisibility(View.VISIBLE);
                    tips_userPwd1.setText("密码不能为空");
                }else if(userPwd1.getText().toString().length() < 6){
                    tips_userPwd1.setVisibility(View.VISIBLE);
                    tips_userPwd1.setText("密码长度不能小于6");
                }else if(userPwd1.getText().toString().length() > 15){
                    tips_userPwd1.setVisibility(View.VISIBLE);
                    tips_userPwd1.setText("密码长度不能大于15");
                }else if(!isContainAll(userPwd1.getText().toString())){//没有包含大小写和数字
                    tips_userPwd1.setVisibility(View.VISIBLE);
                    tips_userPwd1.setText("密码必须包含数字和英文字母大小写");
                }else{
                    tips_userPwd1.setVisibility(View.INVISIBLE);//输入正确，隐藏提示文字
                    checkPwd1 = true;
                }
                //检查密码2
                if (!userPwd2.getText().toString().equals(userPwd1.getText().toString())) {//确定密码与密码设置不符
                    tips_userPwd2.setVisibility(View.VISIBLE);
                    tips_userPwd2.setText("密码输入不一致");
                }else{
                    tips_userPwd2.setVisibility(View.INVISIBLE);//输入正确，隐藏提示文字
                    checkPwd2 = true;
                }


                if(checkPhone && checkPwd1 && checkPwd2 && checkCode){
                    //Thread threads = new Thread(new Runnable() {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                db.changePass(userPwd1.getText().toString());
                                User user = new User();
                                user.tel=userPhone.getText().toString();
                                user.name = db.findName(userPhone.getText().toString());//用户昵称
                            } catch (SQLException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            }
                            handler.sendEmptyMessage(1);
                        }
                    };
                    ThreadHelper.cachedThreadPool.submit(runnable);
                }
            }
        });

        //发送验证码监听
        sendCode.setOnClickListener(new View.OnClickListener() {//验证码监听
            @Override
            public void onClick(View view) {
                if(userPhone.getText().toString().isEmpty()){
                    tips_number.setVisibility(View.VISIBLE);
                    tips_number.setText("手机号码不能为空");
                }else {//如果不为空
                    if (!u.checkTel(userPhone.getText().toString())) {//手机格式不正确
                        tips_number.setVisibility(View.VISIBLE);
                        tips_number.setText("手机号码格式错误");
                    } else {//手机格式正确
                        tips_number.setVisibility(View.INVISIBLE);
                        //如果手机格式正确
                        // Thread threads = new Thread(new Runnable() {
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (!db.existPhone(userPhone.getText().toString())) {//手机号不存在
                                        handler.sendEmptyMessage(0);
                                    } else {//手机号存在:
                                        SMSSDK.getVerificationCode("86", userPhone.getText().toString());// 获取验证码
                                        handler.sendEmptyMessage(3);
                                        checkPhone = true;
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
        });

        //设置“返回”的监听事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ForgetPass.this, LoginAndSignUp.class);
                startActivity(intent);
            }
        });
    }

    private void findViewById(){
        //找到控件
        //输入框
        userPhone=findViewById(R.id.signUp_phoneNumber);
        identifyCode=findViewById(R.id.identifyCode);
        userPwd1=findViewById(R.id.users_pwd1);
        userPwd2=findViewById(R.id.users_pwd2);
        //按钮
        login=findViewById(R.id.submit);
        sendCode=findViewById(R.id.code_sent);
        back=findViewById(R.id.back);
        //提示文字
        tips_number=findViewById(R.id.tips_phoneNumber);
        tips_identifyCode=findViewById(R.id.tips_identifyCode);
        tips_userPwd1=findViewById(R.id.tips_userPwd1);
        tips_userPwd2=findViewById(R.id.tips_userPwd2);
    }

    private void showInputTips(){
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
                                            handler.sendEmptyMessage(0);
                                        }else{
                                            handler.sendEmptyMessage(6);
                                            //     checkPhone = true;
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

        //检查验证码
        identifyCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                // Thread threads = new Thread(new Runnable() {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (identifyCode.hasFocus() == false) {
                            if (identifyCode.getText().toString().length() != 6) {
                                handler.sendEmptyMessage(5);
                            } else {
                                handler.sendEmptyMessage(6);
                                Thread threads = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SMSSDK.submitVerificationCode("86", userPhone.getText().toString(), identifyCode.getText().toString());//提交验证码
                                    }
                                });
                                threads.start();
                            }
                        }
                    }
                };
                ThreadHelper.cachedThreadPool.submit(runnable);

                if(!checkCode)
                    handler.sendEmptyMessage(4);
            }
        });

        //检查密码1
        userPwd1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(userPwd1.getText().toString().length() == 0){
                    tips_userPwd1.setVisibility(View.VISIBLE);
                    tips_userPwd1.setText("密码不能为空");
                }else if(userPwd1.getText().toString().length() < 6){
                    tips_userPwd1.setVisibility(View.VISIBLE);
                    tips_userPwd1.setText("密码长度不能小于6");
                }else if(userPwd1.getText().toString().length() > 15){
                    tips_userPwd1.setVisibility(View.VISIBLE);
                    tips_userPwd1.setText("密码长度不能大于15");
                }else if(!isContainAll(userPwd1.getText().toString())){//没有包含大小写和数字
                    tips_userPwd1.setVisibility(View.VISIBLE);
                    tips_userPwd1.setText("密码必须包含数字和英文字母大小写");
                }else{
                    tips_userPwd1.setVisibility(View.INVISIBLE);//输入正确，隐藏提示文字
                    checkPwd1 = true;
                }
            }
        });

        userPwd2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (userPwd2.hasFocus() == false) {
                    if (!userPwd2.getText().toString().equals(userPwd1.getText().toString())) {//确定密码与密码设置不符
                        tips_userPwd2.setVisibility(View.VISIBLE);
                        tips_userPwd2.setText("密码输入不一致");
                    }else{
                        tips_userPwd2.setVisibility(View.INVISIBLE);//输入正确，隐藏提示文字
                        checkPwd2 = true;
                    }
                }
            }
        });
    }

    public void register() {//接口回调函数
        EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE)//回调完成//
                {
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                        checkCode = true;
                        handler.sendEmptyMessage(2);
                    }
                    else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {    //如果获取到验证码
                        handler.sendEmptyMessage(3);
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    }    //返回支持发送验证码的国家列表
                }else {
                    ((Throwable) data).printStackTrace();
                }

            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调

    }


    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            sendCode.setClickable(false);
            sendCode.setBackgroundColor(Color.parseColor("#bfbfbf"));
            sendCode.setText(l / 1000 + "秒");
        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            ViewStructure getCord;
            sendCode.setText("重新获取");
            //设置可点击
            sendCode.setClickable(true);
        }
    }

    public void  onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.gc();
    }
}