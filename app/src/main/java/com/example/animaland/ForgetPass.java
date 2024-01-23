package com.example.animaland;

import static com.example.animaland.tool.Utils.isContainAll;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStructure;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.animaland.tool.JdbcUtils;
import com.example.animaland.tool.Utils;
import com.mob.MobSDK;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ForgetPass extends Activity implements View.OnClickListener {
    EditText phonenumber,passw,passw2,phoneCode;
    Button correct,sendCode;

    //new倒计时对象,总共的时间,每隔多少秒更新一次时间
    final ForgetPass.MyCountDownTimer myCountDownTimer = new ForgetPass.MyCountDownTimer(60000,1000);

    int test = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpass_tab_fragment);
        //文本编辑框
        phonenumber = (EditText) this.findViewById(R.id.phonenumber);                //手机名编辑框
        passw = (EditText) this.findViewById(R.id.passw);                //设置密码编辑框
        passw2 = (EditText) this.findViewById(R.id.passw2);                //二次输入密码编辑框
        phoneCode = (EditText) this.findViewById(R.id.phoneCode);
        //按钮
        sendCode = this.findViewById(R.id.sendCode);          //发送验证码
        correct = this.findViewById(R.id.correct);                //修改
        //按钮点击事件
        sendCode.setOnClickListener((View.OnClickListener) this);
        correct.setOnClickListener((View.OnClickListener) this);
        //短信调用
        MobSDK.init(this);
        MobSDK.submitPolicyGrantResult(true, null);//调用smssdk接口
        register();

    }

    public void register() {//接口回调函数
        EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                        if (result == SMSSDK.RESULT_COMPLETE)//回调完成//
                        {
                            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        test = 1;
                                        Toast.makeText(getApplicationContext(), "验证成功", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {    //如果获取到验证码
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        sendCode.setVisibility(View.VISIBLE);
                                        myCountDownTimer.start();
                                        Toast.makeText(getApplication(), "已发送验证码，请注意查收", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                            }    //返回支持发送验证码的国家列表
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplication(), "操作失败，重新获取验证码", Toast.LENGTH_SHORT).show();
                                }
                            });
                            ((Throwable) data).printStackTrace();
                        }
                    }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        String phone = phonenumber.getText().toString().trim();     //手机号码
        String code = phoneCode.getText().toString().trim();
        String password1 = passw.getText().toString().trim();
        String password2 = passw2.getText().toString().trim();

        switch (v.getId()) {
            case R.id.sendCode://发送手机验证码
                Thread threads = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        Connection con = null;//连接数据库
                        try {//检查手机号码是否在数据库中
                            con = JdbcUtils.getConn();
                            String sql1 = "select * from users where phoneNumber=?";
                            PreparedStatement pst1 = con.prepareStatement(sql1);
                            if (TextUtils.isEmpty(phone)) {//手机号为空
                                Toast.makeText(getApplicationContext(), "输入的手机号不能为空", Toast.LENGTH_SHORT).show();
                            } else {//手机号不为空
                                if (Utils.checkTel(phone)) {//检验手机号
                                    pst1.setString(1, phone);
                                    if (pst1.executeQuery().next()) {//如果数据库中有该手机号码，即已经注册过
                                        SMSSDK.getVerificationCode("86", phone);// 获取验证码
                                    } else {//如果没有注册过
                                        Toast.makeText(getApplicationContext(), "该手机号没有注册过，请先注册", Toast.LENGTH_SHORT).show();
                                    }
                                } else {//手机号检验失败
                                    Toast.makeText(getApplicationContext(), "请输入有效的手机号码", Toast.LENGTH_SHORT).show();
                                }
                            }
                            pst1.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } finally {
                            JdbcUtils.close(con);
                        }
                        Looper.loop();
                    }
                });
                new Thread(threads).start();//解决点击多次闪退的现象
                break;

            case R.id.correct://修改按钮
                Thread threads1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        Connection con = null;//连接数据库

                        boolean flag = false;
                        boolean flagPwd1 = false;
                        boolean flagPwd2 = false;
                        boolean flagCode = false;

                        //密码设置是否正确
                        if (password1.length() < 6) {
                            Toast.makeText(ForgetPass.this, "密码长度不得小于6", Toast.LENGTH_SHORT).show();
                        } else if (password1.length() > 15) {
                            Toast.makeText(ForgetPass.this, "密码长度不得大于15", Toast.LENGTH_SHORT).show();
                        } else {
                            if (!isContainAll(password1)) {
                                Toast.makeText(ForgetPass.this, "密码必须包含数字和英文字母的大小写", Toast.LENGTH_SHORT).show();
                            } else {
                                flagPwd1 = true;
                            }
                        }

                        //第二次密码输入是否正确
                        if (password2.equals(password1)) {
                            flagPwd2 = true;
                        } else {
                            Toast.makeText(ForgetPass.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                        }

                        //验证码是否正确
                        if (code.isEmpty()) {
                            Toast.makeText(ForgetPass.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            SMSSDK.submitVerificationCode("86", phone, code);//提交验证码
                            if (test == 1)//密码验证正确
                                flagCode = true;
                        }

                        flag = flagPwd1 && flagPwd2 && flagCode;
                        try {
                            con = JdbcUtils.getConn();
                            String sql2 = "UPDATE users SET password=? WHERE phoneNumber=?";
                            PreparedStatement pst2 = con.prepareStatement(sql2);
                            pst2.setString(1, password1);
                            pst2.setString(2, phone);
                            if (flag) {//如果密码检测成功，则把新密码更新到数据库中
                                pst2.executeUpdate();//更新数据库
                                Toast.makeText(ForgetPass.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ForgetPass.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(ForgetPass.this, "信息未完善", Toast.LENGTH_SHORT).show();
                            }
                            pst2.close();
                            JdbcUtils.close(con);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                        Looper.loop();
                    }
                });
                new Thread(threads1).start();//解决点击多次闪退的现象
                break;
        }
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
            sendCode.setText(l/1000+"秒");
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
    };

    @Override
    public void onStop() {
        super.onStop();
    }
}
