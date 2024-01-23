package com.example.animaland.oral;

import static com.example.animaland.School.Language.ENGLISH;
import static com.example.animaland.oral.oralUser.HANGOUT;
import static com.example.animaland.tool.ThreadHelper.cachedThreadPool;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animaland.Dialogs.dialog_roomeditpass;
import com.example.animaland.Island;
import com.example.animaland.R;
import com.example.animaland.School.Language;
import com.example.animaland.tool.DatabaseHelper;
import com.example.animaland.tool.Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Oral extends AppCompatActivity {

    private List<oralroom> oralroomList;
    private OralRVAdapter madapter;
    private RecyclerView mRecycleView;
    private ImageView button,random,create,data,back,clear,search,sift;
    private TextView state;
    private EditText searchBox;
    private dialog_language dialog_basicData;
    private boolean isPop=false;
    //private List<Language[]> languages;//用户在弹窗中设置的基本信息
    private oralUser newUser;
    private oralroom newRoom=new oralroom();

    private DatabaseHelper db = new DatabaseHelper();
    private Utils utils = new Utils();
    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if(msg.what==0){
                Toast.makeText(Oral.this, "密码格式错误，请重新输入", Toast.LENGTH_SHORT).show();
            }else if(msg.what==1){
                Toast.makeText(Oral.this, "费用不符合要求，请重新输入", Toast.LENGTH_SHORT).show();
            }else if(msg.what==2){
                startActivity(new Intent(Oral.this,Oral_resident.class));
            }else if(msg.what==3){
                Toast.makeText(Oral.this, "找不到房间id", Toast.LENGTH_SHORT).show();
            }else if(msg.what==4){
                Toast.makeText(Oral.this, "金币不足，支付失败", Toast.LENGTH_SHORT).show();
            }else if(msg.what==5){
                Toast.makeText(Oral.this, "密码错误", Toast.LENGTH_SHORT).show();
            }

        }
    };
    private int delay = 10000;
    private Handler handler1 = new Handler();
    private Runnable task;

    // 开启定时任务
    private void startTask() {
        stopTask(); //关闭之前的定时
        handler1.postDelayed(task = new Runnable() {
            @Override
            public void run() {
                //执行任务....
                oralroomList.clear();
                try {
                    initData();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                initView();
                initListener();
                System.out.println("刷新");
                // 任务执行完后再次调用postDelayed开启下一次任务
                handler1.postDelayed(this, delay);
            }
        }, delay);
    }

    // 停止定时任务
    private void stopTask() {
        if (task != null) {
            handler1.removeCallbacks(task);
            task = null;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oral);
        try {
            initData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //找到控件
        button=findViewById(R.id.button);
        random=findViewById(R.id.random);
        create=findViewById(R.id.create);
        data=findViewById(R.id.data);
        back=findViewById(R.id.back);
        searchBox =findViewById(R.id.searchBox);
        clear =findViewById(R.id.clear);
        search =findViewById(R.id.search);
        sift=findViewById(R.id.sift);
        state=findViewById(R.id.state);
        searchBox=findViewById(R.id.searchBox);
        mRecycleView=findViewById(R.id.rv_oralRooms);


        initView();
        initListener();

        //定时刷新
        startTask();

        //唯一的basicDataDialog,便于记忆用户的选择
        dialog_basicData=new dialog_language(Oral.this,R.style.dialogOfShowRoom);

        //设置监听
        //返回按钮的监听
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:跳转至大岛界面
                Intent intent=new Intent(Oral.this, Island.class);
                startActivity(intent);
            }
        });

        //搜索框键盘确定按钮的监听
        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
              // Runnable runnable = new Runnable() {
                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        Looper.prepare();
                        try {
                            if(db.findOralRoom(searchBox.getText().toString())){//找到房间
                                System.out.println("找到房间");
                                //TODO:搜索框键盘上"搜索"键的监听
                                if(db.getOralPass(searchBox.getText().toString())==null){//没有密码
                                    if(db.getOralFee(searchBox.getText().toString())==0) {//免费
                                        if(db.enterOralRoom(searchBox.getText().toString())) //人数没满
                                            startActivity(new Intent(Oral.this,Oral_resident.class));
                                        else//人数满了
                                            Toast.makeText(getBaseContext(),"房间人数已满",Toast.LENGTH_SHORT).show();
                                    }else{//收费
                                        int fee = db.getOralFee(searchBox.getText().toString());
                                        dialog_pay dialog=new dialog_pay(Oral.this,R.style.dialogOfShowRoom,fee);
                                        dialog.setYes(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                try {
                                                    if(db.payFee(fee)) {//支付成功
                                                        Toast.makeText(Oral.this, "已支付" + dialog.getFee() + "金币", Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();
                                                        if(db.enterOralRoom(searchBox.getText().toString())) //人数没满
                                                            startActivity(new Intent(Oral.this,Oral_resident.class));
                                                        else//人数满了
                                                            Toast.makeText(getBaseContext(),"房间人数已满",Toast.LENGTH_SHORT).show();
                                                    }else{//不够钱
                                                        handler.sendEmptyMessage(4);
                                                    }
                                                } catch (SQLException e) {
                                                    e.printStackTrace();
                                                } catch (ClassNotFoundException e) {
                                                    e.printStackTrace();
                                                } catch (IllegalAccessException e) {
                                                    e.printStackTrace();
                                                } catch (InstantiationException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).setNo(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Toast.makeText(Oral.this, "取消支付", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }).show();
                                    }
                                }else{//有密码
                                    String pass = db.getOralPass(searchBox.getText().toString());
                                    dialog_roomeditpass roomeditpass = new dialog_roomeditpass(Oral.this, R.style.dialogOfShowRoom);
                                    roomeditpass.setEnterWithPass(new dialog_roomeditpass.IonEnterRoomListener() {
                                        @Override
                                        public void onEnterRoom(dialog_roomeditpass roomeditpass) {//监听进入按钮
                                            if(pass.equals(roomeditpass.editPass.getText().toString())){//密码正确
                                                try {
                                                    if(db.getOralFee(searchBox.getText().toString())==0) {//免费
                                                        if(db.enterOralRoom(searchBox.getText().toString())) //人数没满
                                                            startActivity(new Intent(Oral.this,Oral_resident.class));
                                                        else//人数满了
                                                            Toast.makeText(getBaseContext(),"房间人数已满",Toast.LENGTH_SHORT).show();
                                                    }else{//收费
                                                        int fee = db.getOralFee(searchBox.getText().toString());
                                                        dialog_pay dialog=new dialog_pay(Oral.this,R.style.dialogOfShowRoom,fee);
                                                        dialog.setYes(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                try {
                                                                    if(db.payFee(fee)) {//支付成功
                                                                        Toast.makeText(Oral.this, "已支付" + dialog.getFee() + "金币", Toast.LENGTH_SHORT).show();
                                                                        dialog.dismiss();
                                                                        if(db.enterOralRoom(searchBox.getText().toString())) //人数没满
                                                                            startActivity(new Intent(Oral.this,Oral_resident.class));
                                                                        else//人数满了
                                                                            Toast.makeText(getBaseContext(),"房间人数已满",Toast.LENGTH_SHORT).show();
                                                                    }else{//不够钱
                                                                        handler.sendEmptyMessage(4);
                                                                    }
                                                                } catch (SQLException e) {
                                                                    e.printStackTrace();
                                                                } catch (ClassNotFoundException e) {
                                                                    e.printStackTrace();
                                                                } catch (IllegalAccessException e) {
                                                                    e.printStackTrace();
                                                                } catch (InstantiationException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }).setNo(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                Toast.makeText(Oral.this, "取消支付", Toast.LENGTH_SHORT).show();
                                                                dialog.dismiss();
                                                            }
                                                        }).show();
                                                    }
                                                } catch (SQLException e) {
                                                    e.printStackTrace();
                                                } catch (ClassNotFoundException e) {
                                                    e.printStackTrace();
                                                } catch (IllegalAccessException e) {
                                                    e.printStackTrace();
                                                } catch (InstantiationException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                            else{
                                                handler.sendEmptyMessage(5);
                                            }
                                        }
                                    }).show();
                                }
                            }else{//找不到房间
                                handler.sendEmptyMessage(3);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
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
                thread.start();
             //   cachedThreadPool.execute(runnable);

                return false;
            }
        });

        //放大镜按钮的监听
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:“搜索”按钮的监听（同上）
                /**传入的newRoom是根据用户在基本信息弹窗处填写的信息而生成的用户与房间，测试用*/
               /* newRoom.user=newUser;
                dialog_oralRoom dialog=new dialog_oralRoom(Oral.this,R.style.dialogOfShowRoom, newRoom);
                dialog.setOnEnterListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO:进入房间 密码/支付
                        Toast.makeText(Oral.this, "进入", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();*/
                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        Looper.prepare();
                        try {
                            if(db.findOralRoom(searchBox.getText().toString())){//找到房间
                                System.out.println("找到房间");
                                //TODO:搜索框键盘上"搜索"键的监听
                                if(db.getOralPass(searchBox.getText().toString())==null){//没有密码
                                    if(db.getOralFee(searchBox.getText().toString())==0) {//免费
                                        if(db.enterOralRoom(searchBox.getText().toString())) //人数没满
                                            startActivity(new Intent(Oral.this,Oral_resident.class));
                                        else//人数满了
                                            Toast.makeText(getBaseContext(),"房间人数已满",Toast.LENGTH_SHORT).show();
                                    }else{//收费
                                        int fee = db.getOralFee(searchBox.getText().toString());
                                        dialog_pay dialog=new dialog_pay(Oral.this,R.style.dialogOfShowRoom,fee);
                                        dialog.setYes(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                try {
                                                    if(db.payFee(fee)) {//支付成功
                                                        Toast.makeText(Oral.this, "已支付" + dialog.getFee() + "金币", Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();
                                                        if(db.enterOralRoom(searchBox.getText().toString())) //人数没满
                                                            startActivity(new Intent(Oral.this,Oral_resident.class));
                                                        else//人数满了
                                                            Toast.makeText(getBaseContext(),"房间人数已满",Toast.LENGTH_SHORT).show();
                                                    }else{//不够钱
                                                        handler.sendEmptyMessage(4);
                                                    }
                                                } catch (SQLException e) {
                                                    e.printStackTrace();
                                                } catch (ClassNotFoundException e) {
                                                    e.printStackTrace();
                                                } catch (IllegalAccessException e) {
                                                    e.printStackTrace();
                                                } catch (InstantiationException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).setNo(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Toast.makeText(Oral.this, "取消支付", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }).show();
                                    }
                                }else{//有密码
                                    String pass = db.getOralPass(searchBox.getText().toString());
                                    dialog_roomeditpass roomeditpass = new dialog_roomeditpass(Oral.this, R.style.dialogOfShowRoom);
                                    roomeditpass.setEnterWithPass(new dialog_roomeditpass.IonEnterRoomListener() {
                                        @Override
                                        public void onEnterRoom(dialog_roomeditpass roomeditpass) {//监听进入按钮
                                            if(pass.equals(roomeditpass.editPass.getText().toString())){//密码正确
                                                try {
                                                    if(db.getOralFee(searchBox.getText().toString())==0) {//免费
                                                        if(db.enterOralRoom(searchBox.getText().toString())) //人数没满
                                                            startActivity(new Intent(Oral.this,Oral_resident.class));
                                                        else//人数满了
                                                            Toast.makeText(getBaseContext(),"房间人数已满",Toast.LENGTH_SHORT).show();
                                                    }else{//收费
                                                        int fee = db.getOralFee(searchBox.getText().toString());
                                                        dialog_pay dialog=new dialog_pay(Oral.this,R.style.dialogOfShowRoom,fee);
                                                        dialog.setYes(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                try {
                                                                    if(db.payFee(fee)) {//支付成功
                                                                        Toast.makeText(Oral.this, "已支付" + dialog.getFee() + "金币", Toast.LENGTH_SHORT).show();
                                                                        dialog.dismiss();
                                                                        if(db.enterOralRoom(searchBox.getText().toString())) //人数没满
                                                                            startActivity(new Intent(Oral.this,Oral_resident.class));
                                                                        else//人数满了
                                                                            Toast.makeText(getBaseContext(),"房间人数已满",Toast.LENGTH_SHORT).show();
                                                                    }else{//不够钱
                                                                        handler.sendEmptyMessage(4);
                                                                    }
                                                                } catch (SQLException e) {
                                                                    e.printStackTrace();
                                                                } catch (ClassNotFoundException e) {
                                                                    e.printStackTrace();
                                                                } catch (IllegalAccessException e) {
                                                                    e.printStackTrace();
                                                                } catch (InstantiationException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }).setNo(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                Toast.makeText(Oral.this, "取消支付", Toast.LENGTH_SHORT).show();
                                                                dialog.dismiss();
                                                            }
                                                        }).show();
                                                    }
                                                } catch (SQLException e) {
                                                    e.printStackTrace();
                                                } catch (ClassNotFoundException e) {
                                                    e.printStackTrace();
                                                } catch (IllegalAccessException e) {
                                                    e.printStackTrace();
                                                } catch (InstantiationException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                            else{
                                                handler.sendEmptyMessage(5);
                                            }
                                        }
                                    }).show();
                                }
                            }else{//找不到房间
                                handler.sendEmptyMessage(3);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
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
                thread.start();
            }
        });

        //清空按钮的监听
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //清空搜索框
                searchBox.setText("");
            }
        });

        sift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:筛选功能
                Toast.makeText(Oral.this, "筛选功能，敬请期待~", Toast.LENGTH_SHORT).show();
            }
        });

        //主按钮的监听
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopButtons(!isPop);
            }
        });

        //子按钮的监听
        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:随机匹配
                dialog_random dialog1=new dialog_random(Oral.this,R.style.dialogOfShowRoom);
                dialog1.setOnMatchListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        oralroom matchroom=matchRoom(dialog1.isChat(),dialog1.chooseLanguage());
                        if(matchroom==null){
                            Toast.makeText(Oral.this, "暂无符合条件的房间", Toast.LENGTH_SHORT).show();
                        }else{
                            dialog_oralRoom dialog2=new dialog_oralRoom(Oral.this,R.style.dialogOfShowRoom,matchRoom(dialog1.isChat(),dialog1.chooseLanguage()));
                            dialog2.setOnEnterListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //TODO:进入随机匹配的房间
                                    Toast.makeText(Oral.this, "随机匹配进入", Toast.LENGTH_SHORT).show();
                                    Thread thread = new Thread(new Runnable(){
                                        @Override
                                        public void run() {
                                            String roomId = matchRoom(dialog1.isChat(),dialog1.chooseLanguage()).id;
                                            Looper.prepare();
                                            try {
                                                Toast.makeText(Oral.this, "进线程"+roomId, Toast.LENGTH_SHORT).show();
                                                if(db.getOralPass(roomId)==null){//没有密码
                                                    if(db.getOralFee(roomId)==0) {//免费
                                                        if(db.enterOralRoom(roomId)) //人数没满
                                                            startActivity(new Intent(Oral.this,Oral_resident.class));
                                                        else//人数满了
                                                            Toast.makeText(getBaseContext(),"房间人数已满",Toast.LENGTH_SHORT).show();
                                                    }else{//收费
                                                        int fee = db.getOralFee(roomId);
                                                        dialog_pay dialog=new dialog_pay(Oral.this,R.style.dialogOfShowRoom,fee);
                                                        dialog.setYes(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                try {
                                                                    if(db.payFee(fee)) {//支付成功
                                                                        Toast.makeText(Oral.this, "已支付" + dialog.getFee() + "金币", Toast.LENGTH_SHORT).show();
                                                                        dialog.dismiss();
                                                                        if(db.enterOralRoom(roomId)) //人数没满
                                                                            startActivity(new Intent(Oral.this,Oral_resident.class));
                                                                        else//人数满了
                                                                            Toast.makeText(getBaseContext(),"房间人数已满",Toast.LENGTH_SHORT).show();
                                                                    }else{//不够钱
                                                                        handler.sendEmptyMessage(4);
                                                                    }
                                                                } catch (SQLException e) {
                                                                    e.printStackTrace();
                                                                } catch (ClassNotFoundException e) {
                                                                    e.printStackTrace();
                                                                } catch (IllegalAccessException e) {
                                                                    e.printStackTrace();
                                                                } catch (InstantiationException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }).setNo(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                Toast.makeText(Oral.this, "取消支付", Toast.LENGTH_SHORT).show();
                                                                dialog.dismiss();
                                                            }
                                                        }).show();
                                                    }
                                                }else{//有密码
                                                    String pass = db.getOralPass(roomId);
                                                    dialog_roomeditpass roomeditpass = new dialog_roomeditpass(Oral.this, R.style.dialogOfShowRoom);
                                                    roomeditpass.setEnterWithPass(new dialog_roomeditpass.IonEnterRoomListener() {
                                                        @Override
                                                        public void onEnterRoom(dialog_roomeditpass roomeditpass) {//监听进入按钮
                                                            if(pass.equals(roomeditpass.editPass.getText().toString())){//密码正确
                                                                try {
                                                                    if(db.getOralFee(roomId)==0) {//免费
                                                                        if(db.enterOralRoom(roomId)) //人数没满
                                                                            startActivity(new Intent(Oral.this,Oral_resident.class));
                                                                        else//人数满了
                                                                            Toast.makeText(getBaseContext(),"房间人数已满",Toast.LENGTH_SHORT).show();
                                                                    }else{//收费
                                                                        int fee = db.getOralFee(roomId);
                                                                        dialog_pay dialog=new dialog_pay(Oral.this,R.style.dialogOfShowRoom,fee);
                                                                        dialog.setYes(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View view) {
                                                                                try {
                                                                                    if(db.payFee(fee)) {//支付成功
                                                                                        Toast.makeText(Oral.this, "已支付" + dialog.getFee() + "金币", Toast.LENGTH_SHORT).show();
                                                                                        dialog.dismiss();
                                                                                        if(db.enterOralRoom(roomId)) //人数没满
                                                                                            startActivity(new Intent(Oral.this,Oral_resident.class));
                                                                                        else//人数满了
                                                                                            Toast.makeText(getBaseContext(),"房间人数已满",Toast.LENGTH_SHORT).show();
                                                                                    }else{//不够钱
                                                                                        handler.sendEmptyMessage(4);
                                                                                    }
                                                                                } catch (SQLException e) {
                                                                                    e.printStackTrace();
                                                                                } catch (ClassNotFoundException e) {
                                                                                    e.printStackTrace();
                                                                                } catch (IllegalAccessException e) {
                                                                                    e.printStackTrace();
                                                                                } catch (InstantiationException e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                        }).setNo(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View view) {
                                                                                Toast.makeText(Oral.this, "取消支付", Toast.LENGTH_SHORT).show();
                                                                                dialog.dismiss();
                                                                            }
                                                                        }).show();
                                                                    }
                                                                } catch (SQLException e) {
                                                                    e.printStackTrace();
                                                                } catch (ClassNotFoundException e) {
                                                                    e.printStackTrace();
                                                                } catch (IllegalAccessException e) {
                                                                    e.printStackTrace();
                                                                } catch (InstantiationException e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }
                                                            else{
                                                                handler.sendEmptyMessage(5);
                                                            }
                                                        }
                                                    }).show();
                                                }

                                            } catch (SQLException e) {
                                                e.printStackTrace();
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
                                    thread.start();

                                }
                            });
                            dialog2.show();
                        }

                    }
                });
                dialog1.show();
            }
        });

        //创建房间
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:创建房间
                dialog_createOral dialog=new dialog_createOral(Oral.this,R.style.dialogOfShowRoom);
                dialog.setOnCreateListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //根据用户在当前创建房间弹窗设置的信息生成room
                        newRoom=new oralroom();
                        newRoom.user=new oralUser(R.drawable.logo, new Language[]{ENGLISH},new Language[]{ENGLISH},new Language[]{ENGLISH},HANGOUT);
                        newRoom.isCourse=dialog.isChat()?false:true;
                        //newRoom.roomIntroduction=dialog_basicData.getStrings(4);
                        newRoom.password=dialog.getPass();
                        newRoom.fee=dialog.getFee(); /**如果无密码，dialog.getPass()返回“”，如果无费用，dialog.getFee返回0*/

                        //更新画面与总表
                        addRoom(newRoom);
                        /**
                         * 因为基本信息设置弹窗那的bug 这边如果用到那边的信息也会报错
                         * 如果自己随便写一个 功能是没问题的
                         * */


                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                //聊天
                                if(dialog.tag_chat.isChecked()){
                                    if(dialog.pass_no.isChecked()){//没有密码
                                        //进入房间
                                        try {
                                            db.creatOralRoom(1,null,0);
                                            handler.sendEmptyMessage(2);
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        } catch (InstantiationException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if(dialog.pass_yes.isChecked()){//有密码
                                        if(utils.checkRoomPwd(dialog.passEdit.getText().toString())){//密码正确
                                            //进入房间
                                            try {
                                                db.creatOralRoom(1,dialog.passEdit.getText().toString(),0);
                                                handler.sendEmptyMessage(2);
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            } catch (ClassNotFoundException e) {
                                                e.printStackTrace();
                                            } catch (IllegalAccessException e) {
                                                e.printStackTrace();
                                            } catch (InstantiationException e) {
                                                e.printStackTrace();
                                            }
                                        }else{//密码设置错误
                                            handler.sendEmptyMessage(0);//密码格式错误
                                        }
                                    }
                                }

                                //课程
                                if(dialog.tag_course.isChecked()){
                                    if(dialog.pass_no.isChecked()){//没有密码
                                        if(dialog.fee_no.isChecked()){//免费
                                            //进入房间
                                            try {
                                                db.creatOralRoom(2,null,0);
                                                handler.sendEmptyMessage(2);
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            } catch (ClassNotFoundException e) {
                                                e.printStackTrace();
                                            } catch (IllegalAccessException e) {
                                                e.printStackTrace();
                                            } catch (InstantiationException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if(dialog.fee_yes.isChecked()){//收费
                                            //费用符合要求
                                            if(utils.isFee(dialog.feeEdit.getText().toString())){
                                                System.out.println(dialog.feeEdit.getText().toString());
                                                //进入房间
                                                try {
                                                    db.creatOralRoom(2,null,Integer.parseInt(dialog.feeEdit.getText().toString()));
                                                    handler.sendEmptyMessage(2);
                                                } catch (SQLException e) {
                                                    e.printStackTrace();
                                                } catch (ClassNotFoundException e) {
                                                    e.printStackTrace();
                                                } catch (IllegalAccessException e) {
                                                    e.printStackTrace();
                                                } catch (InstantiationException e) {
                                                    e.printStackTrace();
                                                }
                                            }else{//费用不符合
                                                handler.sendEmptyMessage(1);
                                            }
                                        }
                                    }
                                    if(dialog.pass_yes.isChecked()) {//有密码
                                        if (utils.checkRoomPwd(dialog.passEdit.getText().toString())) {//密码正确
                                            if(dialog.fee_no.isChecked()){//免费
                                                //进入房间
                                                try {
                                                    db.creatOralRoom(2,dialog.passEdit.getText().toString(),0);
                                                    handler.sendEmptyMessage(2);
                                                } catch (SQLException e) {
                                                    e.printStackTrace();
                                                } catch (ClassNotFoundException e) {
                                                    e.printStackTrace();
                                                } catch (IllegalAccessException e) {
                                                    e.printStackTrace();
                                                } catch (InstantiationException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            if(dialog.fee_yes.isChecked()){//收费
                                                //费用符合要求
                                                if(utils.isFee(dialog.feeEdit.getText().toString())){
                                                    //进入房间
                                                    try {
                                                        db.creatOralRoom(2,dialog.passEdit.getText().toString(),Integer.parseInt(dialog.feeEdit.getText().toString()));
                                                        handler.sendEmptyMessage(2);
                                                    } catch (SQLException e) {
                                                        e.printStackTrace();
                                                    } catch (ClassNotFoundException e) {
                                                        e.printStackTrace();
                                                    } catch (IllegalAccessException e) {
                                                        e.printStackTrace();
                                                    } catch (InstantiationException e) {
                                                        e.printStackTrace();
                                                    }
                                                }else{//费用不符合
                                                    handler.sendEmptyMessage(1);
                                                }
                                            }
                                        } else {//密码设置错误
                                            handler.sendEmptyMessage(0);//密码格式错误
                                        }
                                    }
                                }
                            }
                        };
                        cachedThreadPool.execute(runnable);



                    }
                });
                dialog.show();
            }
        });

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:修改信息
                dialog_basicData.setCanceledOnTouchOutside(false);


                /*dialog_basicData.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        //TODO:关闭弹窗后的监听事件
                        dialog_basicData.Update();//同步用户所设置的数据

                        //根据用户在基本信息弹窗设置的信息生成user并与房间绑定
                        /**我真的要疯了啊啊啊啊啊啊啊 我不知道为什么我在根据用户设置的信息这里传不过去
                         * 报错说在空对象上执行函数 但我用toast显示该对象时，不是空的
                         * 测试了很多个地方 每个都是正常的 合起来就报错
                         * 希望用数据库来写不会报错 QAQ 下面两行是获得数据的方法
                         * languages=dialog_basicData.getLanguages();//获得一个包含数组的数组
                         * languages.get(0)返回母语数组，1返回擅长数组，2返回学习数组
                         *
                        //languages=dialog_basicData.getLanguages();
                        Language[] l1=languages.get(0);
                        Language[] l2=languages.get(1);
                        Language[] l3=languages.get(2);
                        //Toast.makeText(Oral.this, languages.get(0)[0].getText(), Toast.LENGTH_SHORT).show();
                        //newUser=new oralUser(R.drawable.logo,languages.get(0),languages.get(1),languages.get(2),HANGOUT);
                        newUser=new oralUser(R.drawable.logo,l1,l2,l3,HANGOUT);
                        newRoom.user=newUser;
                    }
                });*/

                dialog_basicData.show();
            }
        });
    }

    private void initData() throws InterruptedException {
        Thread thread = new Thread(new Runnable(){
            // Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    oralroomList=new ArrayList<>();//此List将会传递给adapter，作为广场上显示的房间
                    for (int i = 0; i < db.getOralRoom().size(); i++) {
                        oralroomList.add(db.getOralRoom().get(i));
                        System.out.println("传 " +db.getOralRoom().get(i));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        thread.join();
        //cachedThreadPool.execute(runnable);

     /*   int numOfRooms=10;//假设有10个口语房间

        Language[] mother={ENGLISH};//测试用
        Language[] good={FRENCH,JAPANESE};//测试用
        Language[] learn={OTHERS};//测试用

        for(int i=0;i<numOfRooms;i++){
            oralroom room=new oralroom();
            oralUser user=new oralUser(R.drawable.logo,mother,good,learn,HANGOUT);
            user.setRoomId("1000"+i);
            user.setName("用户"+i);
            room.user=user;
            room.isCourse=i%2==1?true:false;//设置口语房间类型（聊天类/课程类）
            room.roomIntroduction="爱口语的小姐姐一枚吖~";
            room.roomMember=1;
            room.password="1010";
            oralroomList.add(room);
        }*/
    }

    private void initView(){
        //设置布局管理器
        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        mRecycleView.setLayoutManager(layoutManager);

        //创建适配器Adapter
        madapter = new OralRVAdapter(oralroomList);

        //设置适配器
        mRecycleView.setAdapter(madapter);
    }

    private void initListener(){
        madapter.setOnOralRoomClickListener(new OralRVAdapter.onOralRoomClickListener() {
            @Override
            public void onOralRoomClick(int position) {

                String roomId = oralroomList.get(position).id;
                //TODO:口语室的点击事件
               // Toast.makeText(Oral.this, ""+position, Toast.LENGTH_SHORT).show();


                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        Looper.prepare();
                        try {
                                if(db.getOralPass(roomId)==null){//没有密码
                                    if(db.getOralFee(roomId)==0) {//免费
                                        if(db.enterOralRoom(roomId)) //人数没满
                                            startActivity(new Intent(Oral.this,Oral_resident.class));
                                        else//人数满了
                                            Toast.makeText(getBaseContext(),"房间人数已满",Toast.LENGTH_SHORT).show();
                                    }else{//收费
                                        int fee = db.getOralFee(roomId);
                                        dialog_pay dialog=new dialog_pay(Oral.this,R.style.dialogOfShowRoom,fee);
                                        dialog.setYes(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                try {
                                                    if(db.payFee(fee)) {//支付成功
                                                        Toast.makeText(Oral.this, "已支付" + dialog.getFee() + "金币", Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();
                                                        if(db.enterOralRoom(roomId))//人数没满
                                                            startActivity(new Intent(Oral.this,Oral_resident.class));
                                                        else//人数满了
                                                            Toast.makeText(getBaseContext(),"房间人数已满",Toast.LENGTH_SHORT).show();
                                                    }else{//不够钱
                                                        handler.sendEmptyMessage(4);
                                                    }
                                                } catch (SQLException e) {
                                                    e.printStackTrace();
                                                } catch (ClassNotFoundException e) {
                                                    e.printStackTrace();
                                                } catch (IllegalAccessException e) {
                                                    e.printStackTrace();
                                                } catch (InstantiationException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).setNo(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Toast.makeText(Oral.this, "取消支付", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }).show();
                                    }
                                }else{//有密码
                                    String pass = db.getOralPass(roomId);
                                    dialog_roomeditpass roomeditpass = new dialog_roomeditpass(Oral.this, R.style.dialogOfShowRoom);
                                    roomeditpass.setEnterWithPass(new dialog_roomeditpass.IonEnterRoomListener() {
                                        @Override
                                        public void onEnterRoom(dialog_roomeditpass roomeditpass) {//监听进入按钮
                                            if(pass.equals(roomeditpass.editPass.getText().toString())){//密码正确
                                                try {
                                                    if(db.getOralFee(roomId)==0) {//免费
                                                        if(db.enterOralRoom(roomId)) //人数没满
                                                            startActivity(new Intent(Oral.this,Oral_resident.class));
                                                        else//人数满了
                                                            Toast.makeText(getBaseContext(),"房间人数已满",Toast.LENGTH_SHORT).show();
                                                    }else{//收费
                                                        int fee = db.getOralFee(roomId);
                                                        dialog_pay dialog=new dialog_pay(Oral.this,R.style.dialogOfShowRoom,fee);
                                                        dialog.setYes(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                try {
                                                                    if(db.payFee(fee)) {//支付成功
                                                                        Toast.makeText(Oral.this, "已支付" + dialog.getFee() + "金币", Toast.LENGTH_SHORT).show();
                                                                        dialog.dismiss();
                                                                        if(db.enterOralRoom(roomId)) //人数没满
                                                                            startActivity(new Intent(Oral.this,Oral_resident.class));
                                                                        else//人数满了
                                                                            Toast.makeText(getBaseContext(),"房间人数已满",Toast.LENGTH_SHORT).show();
                                                                    }else{//不够钱
                                                                        handler.sendEmptyMessage(4);
                                                                    }
                                                                } catch (SQLException e) {
                                                                    e.printStackTrace();
                                                                } catch (ClassNotFoundException e) {
                                                                    e.printStackTrace();
                                                                } catch (IllegalAccessException e) {
                                                                    e.printStackTrace();
                                                                } catch (InstantiationException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }).setNo(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                Toast.makeText(Oral.this, "取消支付", Toast.LENGTH_SHORT).show();
                                                                dialog.dismiss();
                                                            }
                                                        }).show();
                                                    }
                                                } catch (SQLException e) {
                                                    e.printStackTrace();
                                                } catch (ClassNotFoundException e) {
                                                    e.printStackTrace();
                                                } catch (IllegalAccessException e) {
                                                    e.printStackTrace();
                                                } catch (InstantiationException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                            else{
                                                handler.sendEmptyMessage(5);
                                            }
                                        }
                                    }).show();
                                }

                        } catch (SQLException e) {
                            e.printStackTrace();
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
                thread.start();

            }
        });
    }

    private void PopButtons(boolean isPop){
        this.isPop=isPop;
        if(isPop){
            random.setVisibility(View.VISIBLE);
            create.setVisibility(View.VISIBLE);
            data.setVisibility(View.VISIBLE);
            button.setImageResource(R.drawable.cheese2);
        }
        else{
            random.setVisibility(View.INVISIBLE);
            create.setVisibility(View.INVISIBLE);
            data.setVisibility(View.INVISIBLE);
            button.setImageResource(R.drawable.cheese1);
        }
    }

    private oralroom matchRoom(boolean isChat,Language language){
        List<oralroom> roomMatched=new ArrayList<>();

        //在总表中筛选
        for(int i=0;i<oralroomList.size();i++){
            if(oralroomList.get(i).isCourse!=isChat){
                boolean isFind=false;

                //查找母语
                Language[] mother= oralroomList.get(i).user.getMotherLanguage();
                for(int j=0;!isFind&&j<mother.length;j++)
                    if(mother[j]==language){
                        roomMatched.add(oralroomList.get(i));//添加到符合条件的room数组中
                        isFind=true;//停止查找
                    }

                //母语中查无，查找擅长语言
                if(!isFind){
                    Language[] good= oralroomList.get(i).user.getLanguagesGoodAt();
                    for(int j=0;!isFind&&j<good.length;j++)
                        if(mother[j]==language){
                            roomMatched.add(oralroomList.get(i));//添加到符合条件的room数组中
                            isFind=true;//停止查找
                        }
                }
            }
        }


        if(roomMatched.size()!=0){
            //有匹配房间 随机返回一个
            Random r=new Random();
            return roomMatched.get(r.nextInt(roomMatched.size()));
        }
        return null;//无匹配房间，返回空
    }

    private void addRoom(oralroom room){
        oralroomList.add(room);//添加到总表

        //更新画面
        madapter.setOralRoomList(oralroomList);
        mRecycleView.setAdapter(madapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.gc();
    }
}