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

public class Register extends Activity implements View.OnClickListener {
    private EditText usename, usepwd, usepwd2, phoneNumber, code;
    private Button submit, previous, sendCode;

    //new倒计时对象,总共的时间,每隔多少秒更新一次时间
    final Register.MyCountDownTimer myCountDownTimer = new Register.MyCountDownTimer(60000, 1000);

    int test = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_tab_fragment);
        //文本编辑框
        usename = (EditText) this.findViewById(R.id.usename);                //用户名编辑框
        usepwd = (EditText) this.findViewById(R.id.usepwd);                //设置初始密码编辑框
        usepwd2 = (EditText) this.findViewById(R.id.usepwd2);                //二次输入密码编辑框
        phoneNumber = (EditText) this.findViewById(R.id.phoneNumber);       //电话号码
        code = (EditText) this.findViewById(R.id.code);                     //验证码
        //按钮
        submit = this.findViewById(R.id.submit);                //注册按钮
        previous = this.findViewById(R.id.previous);     //返回按钮
        sendCode = this.findViewById(R.id.sendCode);      //发送验证码按钮
        submit.setOnClickListener((View.OnClickListener) this);
        previous.setOnClickListener((View.OnClickListener) this);
        sendCode.setOnClickListener((View.OnClickListener) this);

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
        String phone = phoneNumber.getText().toString().trim();           //手机号码
        String phoneCode = code.getText().toString().trim();              //手机验证码
        String name = usename.getText().toString().trim();                //用户名
        String pwd01 = usepwd.getText().toString().trim();                //密码
        String pwd02 = usepwd2.getText().toString().trim();            //二次输入的密码

        switch (v.getId()) {
            case R.id.sendCode://发送验证码
                Thread threads = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        Connection con = null;//连接数据库
                        if (TextUtils.isEmpty(phone)) {//手机号为空
                            Toast.makeText(getApplicationContext(), "输入的手机号不能为空", Toast.LENGTH_SHORT).show();
                        } else {//手机号码不为空
                            if (Utils.checkTel(phone)) {//检验手机号
                                //手机号码有效时：查找数据库中有没有手机号码
                                try {
                                    con = JdbcUtils.getConn();
                                    String sql1 = "select * from users where phoneNumber=?";
                                    PreparedStatement pst1 = con.prepareStatement(sql1);
                                    pst1.setString(1, phone);
                                    if (pst1.executeQuery().next()) {//如果找到了手机号码，则不需要注册
                                        Toast.makeText(getApplicationContext(), "该手机已注册过，请前往登录界面登录", Toast.LENGTH_SHORT).show();
                                    } else {//如果没注册过，发送验证码
                                        SMSSDK.getVerificationCode("86", phone);// 获取验证码
                                        Toast.makeText(getApplicationContext(), "验证码已发送", Toast.LENGTH_SHORT).show();
                                    }
                                    pst1.close();
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InstantiationException e) {
                                    e.printStackTrace();
                                } finally {
                                    JdbcUtils.close(con);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "请输入有效的手机号码", Toast.LENGTH_SHORT).show();
                            }
                        }
                        Looper.loop();
                    }
                });
                new Thread(threads).start();//解决点击多次闪退的现象
                break;
            case R.id.submit://提交
                Thread threads2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean flag = false;//判断输入是否符合要求
                        boolean flagName = false;
                        boolean flagPwd1 = false;
                        boolean flagPwd2 = false;
                        boolean flagCode = false;
                        Looper.prepare();
                        Connection con = null;//连接数据库

                        //用户名验证
                        if (usename.length() < 2) {
                            Toast.makeText(Register.this, "用户名长度不得小于2", Toast.LENGTH_SHORT).show();
                        } else if (usename.length() > 10) {
                            Toast.makeText(Register.this, "用户名长度不得大于10", Toast.LENGTH_SHORT).show();
                        } else {
                            flagName = true;
                        }

                        //密码设置是否正确
                        if (usepwd.length() < 6) {
                            Toast.makeText(Register.this, "密码长度不得小于6", Toast.LENGTH_SHORT).show();
                        } else if (usepwd.length() > 15) {
                            Toast.makeText(Register.this, "密码长度不得大于15", Toast.LENGTH_SHORT).show();
                        } else {
                            if (!isContainAll(pwd01)) {
                                Toast.makeText(Register.this, "密码必须包含数字和英文字母的大小写", Toast.LENGTH_SHORT).show();
                            } else {
                                flagPwd1 = true;
                            }
                        }

                        //第二次密码输入是否正确
                        if (pwd02.equals(pwd01)) {
                            flagPwd2 = true;
                        } else {
                            Toast.makeText(Register.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                        }

                        //检验验证码正不正确
                        if (phoneCode.isEmpty()) {
                            Toast.makeText(Register.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            SMSSDK.submitVerificationCode("86", phone, phoneCode);//提交验证码
                            if(test == 1){
                            flagCode = true;
                            }
                        }

                        flag = flagName && flagPwd2 && flagPwd1 && flagCode;

                        //获取数据库数据，判断用户名是否已存在
                        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pwd01) && !TextUtils.isEmpty(pwd02) && flag) {//全部都填好
                            try {
                                con = JdbcUtils.getConn();
                                String sql1 = "select * from users where username=?";
                                PreparedStatement pst1 = con.prepareStatement(sql1);
                                pst1.setString(1, name);
                                String sql2 = "insert into users(username,password,phoneNumber) values(?,?,?)";
                                PreparedStatement pst2 = con.prepareStatement(sql2);
                                if (pst1.executeQuery().next()) {//如果找到用户名，则重新输入
                                    Toast.makeText(Register.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                                } else {//用户名不存在，把手机号、密码、用户名存入数据库
                                    pst2.setString(1, name);
                                    pst2.setString(2, pwd01);
                                    pst2.setString(3, phone);
                                    pst2.executeUpdate();
                                    Intent intent2 = new Intent(Register.this, MainActivity.class);
                                    startActivity(intent2);
                                    finish();
                                    Utils u = new Utils();
                                    Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
                                }
                                pst1.close();
                                pst2.close();
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } finally {
                                JdbcUtils.close(con);
                            }
                        } else {
                            Toast.makeText(Register.this, "未完善信息，注册失败", Toast.LENGTH_SHORT).show();
                        }
                        Looper.loop();
                    }
                });
                new Thread(threads2).start();//解决点击多次闪退的现象
                break;

            case R.id.previous://返回按钮
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
                finish();
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

    @Override
    public void onStop() {
        super.onStop();
    }
}

