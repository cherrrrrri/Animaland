package com.example.animaland.LoginAndSignUp;


import static com.example.animaland.tool.Utils.isContainAll;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.animaland.R;
import com.example.animaland.tool.DatabaseHelper;
import com.example.animaland.tool.ThreadHelper;
import com.example.animaland.tool.User;
import com.example.animaland.tool.Utils;
import com.mob.MobSDK;

import java.sql.SQLException;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class SignUpTabFragment extends Fragment {

    private static EditText userPhone,identifyCode,userName,userPwd1,userPwd2;
    private static Button sendCode,signUp;
    private static TextView tips_number,tips_identifyCode,tips_userName,tips_userPwd1,tips_userPwd2;
    private static float v=0;
    Utils u = new Utils();

    final SignUpTabFragment.MyCountDownTimer myCountDownTimer = new SignUpTabFragment.MyCountDownTimer(60000, 1000);

    boolean checkPhone2 = false;
    boolean checkCode = false;
    boolean checkName = false;
    boolean checkPwd1 = false;
    boolean checkPwd2 = false;

    DatabaseHelper db = new DatabaseHelper();

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                tips_number.setVisibility(View.VISIBLE);
                tips_number.setText("手机号码已注册");
            }else if(msg.what ==1){
                tips_number.setVisibility(View.INVISIBLE);//输入正确，隐藏提示文字
            }else if(msg.what==2){
                tips_identifyCode.setVisibility(View.VISIBLE);
                tips_identifyCode.setText("验证成功");
            }else if(msg.what == 3){
                myCountDownTimer.start();
                tips_identifyCode.setVisibility(View.VISIBLE);
                tips_identifyCode.setText("已发送验证码，请注意查收");
            }else if(msg.what == 4){
                tips_identifyCode.setVisibility(View.VISIBLE);
                tips_identifyCode.setText("操作失败，重新获取验证码");
            }else if(msg.what == 5) {
                tips_identifyCode.setVisibility(View.VISIBLE);
                tips_identifyCode.setText("验证码长度不正确");
            }else if(msg.what == 6){
                tips_identifyCode.setVisibility(View.INVISIBLE);//输入正确，隐藏提示文字
            }else if(msg.what == 7){
                Intent intent = new Intent(getActivity(), LoginAndSignUp.class);
                startActivity(intent);
            }else if(msg.what == 8){
                tips_identifyCode.setVisibility(View.VISIBLE);
                tips_identifyCode.setText("验证码错误");
            }else if(msg.what == 9){
                tips_number.setVisibility(View.INVISIBLE);//输入正确，隐藏提示文字
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root= (ViewGroup) inflater.inflate(R.layout.page_signup,container,false);

        findViewById(root);//找到控件
        anim();//浮动动画
        showInputTips();//提示文字

        //MobSDK.init(this);
        //短信
        MobSDK.submitPolicyGrantResult(true, null);//调用smssdk接口
        register();

        //设置注册按钮的监听，进行最终检查
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //检查手机号
                if(userPhone.getText().toString().isEmpty()){
                    tips_number.setVisibility(View.VISIBLE);
                    tips_number.setText("手机号码不能为空");
                }else {//如果不为空
                    if (!u.checkTel(userPhone.getText().toString())) {//手机格式不正确
                        tips_number.setVisibility(View.VISIBLE);
                        tips_number.setText("手机号码格式错误");
                    } else {//手机格式正确
                      /*  Thread threads = new Thread(new Runnable() {
                            @Override*/

                            Runnable runnable = new Runnable(){
                            @Override
                                public void run() {
                                    try {
                                        if (db.existPhone(userPhone.getText().toString())) {//手机号已存在
                                            handler.sendEmptyMessage(0);
                                        }else{
                                            handler.sendEmptyMessage(9);
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
                        //threads.start();
                    }
                }
               // 检查验证码
                if (identifyCode.hasFocus() == false) {
                    if (identifyCode.getText().toString().length() != 6) {
                        handler.sendEmptyMessage(5);
                    } else {
                        // handler.sendEmptyMessage(6);
                      //  Thread threads = new Thread(new Runnable() {
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    SMSSDK.submitVerificationCode("86", userPhone.getText().toString(), identifyCode.getText().toString());//提交验证码

                                }
                            };
                        ThreadHelper.cachedThreadPool.submit(runnable);

                       if(!checkCode)
                            handler.sendEmptyMessage(8);
                    }
                }

                //检查用户名
                if(userName.getText().toString().isEmpty()){
                    tips_userName.setVisibility(View.VISIBLE);
                    tips_userName.setText("！用户名不能为空");
                }else {
                    if (userName.getText().toString().length() > 10) {
                        tips_userName.setVisibility(View.VISIBLE);
                        tips_userName.setText("用户名长度不得大于10");
                    } else if (userName.getText().toString().length() < 2) {
                        tips_userName.setVisibility(View.VISIBLE);
                        tips_userName.setText("用户名长度不得小于2");
                    } else {
                        tips_userName.setVisibility(View.INVISIBLE);//输入正确，隐藏提示文字
                        checkName = true;
                    }
                }
                //检查密码1
                if(userPwd1.getText().toString().length() == 0){
                    tips_userPwd1.setVisibility(View.VISIBLE);
                    tips_userPwd1.setText("密码不能为空");
                }else {
                    if (userPwd1.getText().toString().length() < 6) {
                        tips_userPwd1.setVisibility(View.VISIBLE);
                        tips_userPwd1.setText("密码长度不能小于6");
                    } else if (userPwd1.getText().toString().length() > 15) {
                        tips_userPwd1.setVisibility(View.VISIBLE);
                        tips_userPwd1.setText("密码长度不能大于15");
                    } else {
                        if (!isContainAll(userPwd1.getText().toString())) {//没有包含大小写和数字
                            tips_userPwd1.setVisibility(View.VISIBLE);
                            tips_userPwd1.setText("密码必须包含数字和英文字母大小写");
                        } else {
                            tips_userPwd1.setVisibility(View.INVISIBLE);//输入正确，隐藏提示文字
                            checkPwd1 = true;
                        }
                    }
                }
                //检查密码2
                if (!userPwd2.getText().toString().equals(userPwd1.getText().toString())) {//确定密码与密码设置不符
                    tips_userPwd2.setVisibility(View.VISIBLE);
                    tips_userPwd2.setText("密码输入不一致");
                }else{
                    tips_userPwd2.setVisibility(View.INVISIBLE);//输入正确，隐藏提示文字
                    checkPwd2 = true;
                }


                if ( checkName && checkPhone2  && checkPwd2) {//注册信息更新到数据库

                 //   Thread threads = new Thread(new Runnable() {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                db.signUp(userName.getText().toString(), userPwd1.getText().toString(), userPhone.getText().toString());
                                User user = new User();
                                user.tel =userPhone.getText().toString();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (java.lang.InstantiationException e) {
                                e.printStackTrace();
                            }
                           handler.sendEmptyMessage(7);
                        }
                    };
                    ThreadHelper.cachedThreadPool.submit(runnable);
                }
            }

            });

        return root;
    }  //onCreateView的末尾


    private void findViewById(ViewGroup root){
        //输入框
        userPhone=root.findViewById(R.id.signUp_phoneNumber);
        identifyCode=root.findViewById(R.id.identifyCode);
        userName=root.findViewById(R.id.users_name);
        userPwd1=root.findViewById(R.id.users_pwd1);
        userPwd2=root.findViewById(R.id.users_pwd2);
        //按钮
        sendCode=root.findViewById(R.id.code_sent);
        signUp=root.findViewById(R.id.submit);
        //提示文字
        tips_number=root.findViewById(R.id.tips_phoneNumber);
        tips_identifyCode=root.findViewById(R.id.tips_identifyCode);
        tips_userName=root.findViewById(R.id.tips_userName);
        tips_userPwd1=root.findViewById(R.id.tips_userPwd1);
        tips_userPwd2=root.findViewById(R.id.tips_userPwd2);

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
                            Runnable runnable= new Runnable(){
                                @Override
                                public void run() {
                                    try {
                                        if (db.existPhone(userPhone.getText().toString())) {//手机号已存在
                                            handler.sendEmptyMessage(0);
                                        }else{
                                            handler.sendEmptyMessage(9);
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
                    //    signUp.setEnabled(false);//将注册按钮设为不可点击
                    } else {//手机格式正确
                        //如果手机格式正确
                        // Thread threads = new Thread(new Runnable() {
                        Runnable runnable= new Runnable() {
                        @Override
                            public void run() {
                                try {
                                    if (db.existPhone(userPhone.getText().toString())) {//手机号已存在
                                        handler.sendEmptyMessage(0);
                                    } else {//手机号不存在:
                                        SMSSDK.getVerificationCode("86", userPhone.getText().toString());// 获取验证码
                                        handler.sendEmptyMessage(3);
                                        checkPhone2 = true;
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


        //检查验证码
        identifyCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
              //  Thread threads = new Thread(new Runnable() {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (identifyCode.hasFocus() == false) {
                            if (identifyCode.getText().toString().length() != 6) {
                                handler.sendEmptyMessage(5);
                            } else {
                               // handler.sendEmptyMessage(6);
                               Thread threads = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SMSSDK.submitVerificationCode("86", userPhone.getText().toString(), identifyCode.getText().toString());//提交验证码
                                    }
                                });
                                threads.start();
                                if(!checkCode)
                                    handler.sendEmptyMessage(8);
                            }
                        }
                    }
                };
                ThreadHelper.cachedThreadPool.submit(runnable);

            }
        });



        //检查用户名
        userName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(userName.hasFocus()==false){
                    if(userName.getText().toString().isEmpty()){
                        tips_userName.setVisibility(View.VISIBLE);
                        tips_userName.setText("！用户名不能为空");
                    }else {
                        if (userName.getText().toString().length() > 10) {
                            tips_userName.setVisibility(View.VISIBLE);
                            tips_userName.setText("用户名长度不得大于10");
                        } else if (userName.getText().toString().length() < 2) {
                            tips_userName.setVisibility(View.VISIBLE);
                            tips_userName.setText("用户名长度不得小于2");
                        } else {
                            tips_userName.setVisibility(View.INVISIBLE);//输入正确，隐藏提示文字
                            checkName = true;
                        }
                    }
                }
            }
        });

        //检查密码1
        userPwd1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (userPwd1.hasFocus() == false) {
                    if (userPwd1.getText().toString().length() == 0) {
                        tips_userPwd1.setVisibility(View.VISIBLE);
                        tips_userPwd1.setText("密码不能为空");
                    } else {
                        if (userPwd1.getText().toString().length() < 6) {
                            tips_userPwd1.setVisibility(View.VISIBLE);
                            tips_userPwd1.setText("密码长度不能小于6");
                        } else {
                            if (userPwd1.getText().toString().length() > 15) {
                                tips_userPwd1.setVisibility(View.VISIBLE);
                                tips_userPwd1.setText("密码长度不能大于15");
                            } else {
                                if (!isContainAll(userPwd1.getText().toString())) {//没有包含大小写和数字
                                    tips_userPwd1.setVisibility(View.VISIBLE);
                                    tips_userPwd1.setText("密码必须包含数字和英文字母大小写");
                                } else {
                                    tips_userPwd1.setVisibility(View.INVISIBLE);//输入正确，隐藏提示文字
                                    checkPwd1 = true;
                                }
                            }
                        }
                    }
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




    public static void anim(){
        //设置动画
        userPhone.setTranslationY(800);
        identifyCode.setTranslationY(800);
        sendCode.setTranslationY(800);
        userName.setTranslationY(800);
        userPwd1.setTranslationY(800);
        userPwd2.setTranslationY(800);
        signUp.setTranslationY(800);
        tips_number.setTranslationY(800);
        tips_identifyCode.setTranslationY(800);
        tips_userName.setTranslationY(800);
        tips_userPwd1.setTranslationY(800);
        tips_userPwd2.setTranslationY(800);

        userPhone.setAlpha(v);
        identifyCode.setAlpha(v);
        sendCode.setAlpha(v);
        userName.setAlpha(v);
        userPwd1.setAlpha(v);
        userPwd2.setAlpha(v);
        signUp.setAlpha(v);
        tips_number.setAlpha(v);
        tips_identifyCode.setAlpha(v);
        tips_userName.setAlpha(v);
        tips_userPwd1.setAlpha(v);
        tips_userPwd2.setAlpha(v);

        userPhone.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(0).start();
        tips_number.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(0).start();

        identifyCode.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(200).start();
        tips_identifyCode.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(200).start();
        sendCode.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(300).start();

        userName.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        tips_userName.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

        userPwd1.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        tips_userPwd1.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();

        userPwd2.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        tips_userPwd2.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();

        signUp.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1000).start();

    }

    public void onDestroyView() {
        super.onDestroyView();
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
}