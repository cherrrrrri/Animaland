package com.example.animaland.room;

import static com.example.animaland.tool.ThreadHelper.cachedThreadPool;
import static com.example.animaland.tool.Utils.isNumberic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.example.animaland.Adapters.MoreTypeBeanAdapter;
import com.example.animaland.Adapters.RecycleViewBaseAdapter;
import com.example.animaland.Dialogs.dialog_roomcreate;
import com.example.animaland.Dialogs.dialog_roomeditpass;
import com.example.animaland.Dialogs.dialog_roomshow;
import com.example.animaland.Map;
import com.example.animaland.R;
import com.example.animaland.selfroom.selfRoomMainActivity;
import com.example.animaland.tool.DatabaseHelper;
import com.example.animaland.tool.Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class ShellStudyRoom extends Activity {
    private RecyclerView mRecycleView;
    private ArrayList<room> mroomList = new ArrayList<>();
    private MoreTypeBeanAdapter madapter;
    private HorizontalScrollView mscrollView;
    private Button mCreate;//记录创建房间的按钮
    private Button mUp;//记录回到顶部的按钮
    private Button mBack;//记录回到自习室主题地图的按钮
    private  ImageView  check;//暂时用来确定房间号
    private EditText find_room;
    private Utils u = new Utils();
    private DatabaseHelper db = new DatabaseHelper();
    private int delay = 10000;
    private static boolean first_shell=false;
    private Handler handler = new Handler();
    private Handler mhandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if(msg.what==1)
                showStagger(false,false);
        }
    };

    private Runnable task;

    // 开启定时任务
    private void startTask() {
        stopTask(); //关闭之前的定时
        handler.postDelayed(task = new Runnable() {
            @Override
            public void run() {
                //执行任务....
                try {
                    mroomList.clear();
                    initData();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 任务执行完后再次调用postDelayed开启下一次任务
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    // 停止定时任务
    private void stopTask() {
        if (task != null) {
            handler.removeCallbacks(task);
            task = null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shell_study_room);

        //找到recycleView
        mRecycleView = findViewById(R.id.rv_shell);
        mscrollView = findViewById(R.id.sc_shell);
        mCreate = findViewById(R.id.room_create_SHELL);
        mUp = findViewById(R.id.room_up_SHELL);
        check = findViewById(R.id.check_cave);
        mBack=findViewById(R.id.room_back);
        find_room = findViewById(R.id.room_search_SHELL);//寻找房间的文本框

        if(first_shell==false) {
            NewbieGuide.with(this)
                    .setLabel("guide1")
                    .alwaysShow(true)
                    .addGuidePage(GuidePage.newInstance()
                            .setLayoutRes(R.layout.shell_guide1))
                    .addGuidePage(GuidePage.newInstance()
                            .setLayoutRes(R.layout.shell_guide3))
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLight(mUp)
                            .setLayoutRes(R.layout.shell_guide4))
                    .show();
            first_shell=true;
        }


        try {
            initData();//模拟数据
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ShellStudyRoom.this, Map.class);
                startActivity(intent);
            }
        });

        //设置监听 “创建房间“
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Thread thread = new Thread(new Runnable() {
                 Runnable runnable = new Runnable() {
                    boolean hasPwd = true;
                    @Override
                    public void run() {
                        if(Looper.myLooper() == null) {
                            Looper.prepare();
                        }
                        dialog_roomcreate roomcreate = new dialog_roomcreate(ShellStudyRoom.this, R.style.dialogOfShowRoom);
                        roomcreate.setRadioPass(new RadioGroup.OnCheckedChangeListener() {//是和否的选择按钮
                            @Override
                            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                switch (i) {
                                    case R.id.yesPass:
                                        roomcreate.roomPass.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                roomcreate.roomPass.setVisibility(View.VISIBLE);
                                                String room_pwd = roomcreate.roomPass.getText().toString();

                                            }
                                        });
                                        break;
                                    case R.id.noPass://没有密码
                                        roomcreate.roomPass.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                roomcreate.roomPass.setVisibility(View.INVISIBLE);
                                                hasPwd = false;
                                            }
                                        });
                                        break;
                                }
                            }
                        });

                        roomcreate.setRoomCreate(new dialog_roomcreate.IonCreateRoomListener() {
                            @Override
                            public void onCreateRoom(dialog_roomcreate roomcreate) {
                                String room_pwd = roomcreate.roomPass.getText().toString();
                                String room_name = roomcreate.roomName.getText().toString();

                                if (u.checkRoomName(room_name))//如果房间名字符合要求
                                {
                                    if ( u.checkRoomPwd(room_pwd)) {//存入数据库:房间密码正确
                                        try {
                                            db.createShellRoom(room_name, room_pwd);
                                            Intent intent = new Intent(ShellStudyRoom.this, selfRoomMainActivity.class);
                                            startActivity(intent);
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        } catch (InstantiationException e) {
                                            e.printStackTrace();
                                        }
                                    }else if(!hasPwd) {
                                        try {
                                            db.createShellRoom(room_name, null);
                                            Intent intent = new Intent(ShellStudyRoom.this, selfRoomMainActivity.class);
                                            startActivity(intent);
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        } catch (InstantiationException e) {
                                            e.printStackTrace();
                                        }
                                    } else {//密码设置错误
                                        Toast.makeText(ShellStudyRoom.this, "请输入四位密码（只包含英文大小写或数字）", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(ShellStudyRoom.this, "房间名必须在2-10个字符之间，且都要为中英文或数字", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        roomcreate.show();
                        Looper.loop();
                    }
                };
                cachedThreadPool.execute(runnable);

            }
        });

        //设置监听 "滑到顶部"
        mUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mscrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        mscrollView.fullScroll(View.FOCUS_UP);
                    }
                });
            }
        });

       check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Thread thread = new Thread(new Runnable() {
                  Runnable runnable = new Runnable() {

                    @Override
                    public void run() {
                        Looper.prepare();
                        //转字符
                        String find = find_room.getText().toString();
                        if (find.isEmpty()) {//如果搜索框为空
                            Toast.makeText(ShellStudyRoom.this, "请输入房间id", Toast.LENGTH_SHORT).show();
                        } else {
                            if ((find.length() == 5) && isNumberic(find)) {//如果长度为5且都为数字:可继续
                                try {
                                    if (db.findRoom(find)) {//如果在数据库找到了id
                                        if (!db.isFull(find)) {//如果人数没满
                                            //db.enterRoom(find);
                                            dialog_roomshow roomshow = new dialog_roomshow(ShellStudyRoom.this, R.style.dialogOfShowRoom);
                                            String roomName = db.findRoomName(find);
                                            String roomNotice = db.findRoomAnnouncement(find);

                                            roomshow.setName(roomName).setID(find).setAnnouncement(roomNotice).
                                                    setRoomEnter("进入房间", new dialog_roomshow.IonEnterRoomListener() {
                                                        @Override
                                                        public void onEnterRoom(dialog_roomshow roomshow) {//公告
                                                            //出现“输入密码”弹窗
                                                            try {
                                                                if(!db.hasPwd(find)){//如果没密码 直接进入房间
                                                                    db.enterRoom(find);
                                                                    Intent intent = new Intent(ShellStudyRoom.this, selfRoomMainActivity.class);
                                                                    startActivity(intent);
                                                                }else {//如果有密码 有弹窗输入密码
                                                                    dialog_roomeditpass roomeditpass = new dialog_roomeditpass(ShellStudyRoom.this, R.style.dialogOfShowRoom);
                                                                    roomeditpass.setEnterWithPass(new dialog_roomeditpass.IonEnterRoomListener() {
                                                                        @Override
                                                                        public void onEnterRoom(dialog_roomeditpass roomeditpass) {//监听进入按钮
                                                                            String roomPwd = roomeditpass.editPass.getText().toString();

                                                                            if(u.checkRoomPwd(roomPwd)){//密码格式正确
                                                                                try {
                                                                                    if(db.pwdFound(find,roomPwd)){//如果房间名和密码匹配
                                                                                        db.enterRoom(find);
                                                                                        //跳转至房间内部
                                                                                        Intent intent = new Intent(ShellStudyRoom.this, selfRoomMainActivity.class);
                                                                                        startActivity(intent);

                                                                                    }else{
                                                                                        Toast.makeText(ShellStudyRoom.this,"密码错误，请重新输入",Toast.LENGTH_SHORT).show();
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
                                                                            }else{//密码格式不正确
                                                                                Toast.makeText(ShellStudyRoom.this,"密码错误，请重新输入",Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    }).show();
                                                                    //  roomeditpass.show();
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
                                                    }).show();

                                        } else {
                                            Toast.makeText(ShellStudyRoom.this, "房间人数已满", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(ShellStudyRoom.this, "该房间不存在", Toast.LENGTH_SHORT).show();
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
                            } else {
                                Toast.makeText(ShellStudyRoom.this, "请输入正确房间id", Toast.LENGTH_SHORT).show();
                            }
                        }
                        Looper.loop();
                    }
                };
                cachedThreadPool.execute(runnable);

            }
        });

        //startTask();

    }

    //模拟数据
    private void initData() throws InterruptedException {
       // Thread thread = new Thread(new Runnable() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Random r = new Random();
                try {
                    // mroomList = (ArrayList<room>) db.existRoom().clone();//创建房间数据xistRoomNumber(集合（从数据库传入）
                    for (int i = 0; i < db.existShellRoomNumber(); i++) {//房间数量
                        room roomData = new room();
                        roomData.id = db.existShellRoom().get(i);//房间id
                        roomData.roomName = db.findRoomName(roomData.id);
                        roomData.roomMember = db.findMemberNumber(roomData.id);
                        roomData.icon = roomPictures.roomPics[1];//加载图片
                        switch (i%3){
                            case 0:
                                roomData.type=1;
                                break;
                            case 1:
                                roomData.type=0;
                                break;
                            case 2:
                                roomData.type=2;
                                break;
                        }
                        mroomList.add(roomData);//将房间数据添加到房间数据集合中
                        mhandler.sendEmptyMessage(1);
                    }
                   // Looper.loop();
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
        };

        cachedThreadPool.execute(runnable);
    }

    private void initListener() {
        madapter.setOnItemClickListener(new RecycleViewBaseAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String id = mroomList.get(position).id;
                Thread thread = new Thread(new Runnable() {
               // Runnable runnable= new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Looper.prepare();
                            if (!db.isFull(id)) {//如果人数没满
                                //出现房间信息展示弹窗
                                dialog_roomshow roomshow = new dialog_roomshow(ShellStudyRoom.this, R.style.dialogOfShowRoom);
                                roomshow.setName(mroomList.get(position).roomName).setID(mroomList.get(position).id).setAnnouncement(mroomList.get(position).announce).
                                        setRoomEnter("进入房间", new dialog_roomshow.IonEnterRoomListener() {
                                            @Override
                                            public void onEnterRoom(dialog_roomshow roomshow) {//公告
                                                //出现“输入密码”弹窗

                                                try {
                                                    if (!db.hasPwd(id)) {//如果没密码 直接进入房间
                                                        db.enterRoom(id);
                                                        Intent intent = new Intent(ShellStudyRoom.this, selfRoomMainActivity.class);
                                                        startActivity(intent);
                                                    } else {//如果有密码 有弹窗输入密码
                                                        dialog_roomeditpass roomeditpass = new dialog_roomeditpass(ShellStudyRoom.this, R.style.dialogOfShowRoom);
                                                        roomeditpass.setEnterWithPass(new dialog_roomeditpass.IonEnterRoomListener() {
                                                            @Override
                                                            public void onEnterRoom(dialog_roomeditpass roomeditpass) {//监听进入按钮
                                                                String roomPwd = roomeditpass.editPass.getText().toString();
                                                                if (u.checkRoomPwd(roomPwd)) {//密码格式正确
                                                                    try {
                                                                        if (db.pwdFound(id, roomPwd)) {//如果房间名和密码匹配
                                                                            db.enterRoom(id);
                                                                            //跳转至房间内部
                                                                            Intent intent = new Intent(ShellStudyRoom.this, selfRoomMainActivity.class);
                                                                            startActivity(intent);
                                                                        } else {
                                                                            Toast.makeText(ShellStudyRoom.this, "密码错误，请重新输入", Toast.LENGTH_SHORT).show();
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

                                                                } else {//密码格式不正确
                                                                    Toast.makeText(ShellStudyRoom.this, "密码错误，请重新输入", Toast.LENGTH_SHORT).show();
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
                                            }
                                        }).show();
                            }else{
                                Toast.makeText(ShellStudyRoom.this,"房间已满",Toast.LENGTH_SHORT).show();
                            }
                            Looper.loop();
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
               // cachedThreadPool.execute(runnable);
            }
        });
    }



    private void showStagger(boolean isVertical,boolean isReverse) {
        //设置布局管理器
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(1,isVertical?StaggeredGridLayoutManager.VERTICAL:StaggeredGridLayoutManager.HORIZONTAL);
        //LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(isVertical?LinearLayoutManager.VERTICAL:LinearLayoutManager.HORIZONTAL);

        //设置正向还是反向
        layoutManager.setReverseLayout(isReverse);
        mRecycleView.setLayoutManager(layoutManager);

        //创建适配器Adapter
        madapter = new MoreTypeBeanAdapter(mroomList,RecycleViewBaseAdapter.THEME_SHELL);
        //MoreTypeAdapter adapter=new MoreTypeAdapter(mroomList);
        //设置适配器
        mRecycleView.setAdapter(madapter);

        initListener();
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
