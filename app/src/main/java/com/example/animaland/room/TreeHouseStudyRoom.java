package com.example.animaland.room;

import static com.example.animaland.tool.ThreadHelper.cachedThreadPool;
import static com.example.animaland.tool.Utils.isNumberic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.List;
import java.util.Random;

public class TreeHouseStudyRoom extends AppCompatActivity{

    private RecyclerView mRecycleView;
    private List<room> mroomList = new ArrayList<>();
    private MoreTypeBeanAdapter madapter;
    private ScrollView mscrollView;
    private Button mCreate;//记录创建房间的按钮
    private Button mDown;//记录回到底部的按钮
    private Button mBack;//记录回到自习室主题地图的按钮
    private  ImageView  check;//暂时用来确定房间号
    private EditText find_room;
    private DatabaseHelper db = new DatabaseHelper();
    private Utils u = new Utils();
    private int delay = 10000;
    private Handler handler = new Handler();
    private Runnable task;
    private static boolean first_treeHouse=false;
    private Handler handler1 = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                Intent intent = new Intent(TreeHouseStudyRoom.this, selfRoomMainActivity.class);
                startActivity(intent);
            }else if(msg.what == 1){
                showStagger(true,true);
            }
        }
    };



    // 开启定时任务
    private void startTask() {
        stopTask(); //关闭之前的定时
        handler.postDelayed(task = new Runnable() {
            @Override
            public void run() {
                //执行任务....
                    mroomList.clear();
                try {
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
        setContentView(R.layout.treehouse_study_room);

        //找到控件
        mRecycleView =findViewById(R.id.rv_treehouse);
        mscrollView=findViewById(R.id.sc_tree);
        mCreate=findViewById(R.id.room_create);
        mDown=findViewById(R.id.room_down);
        check=findViewById(R.id.check_cave);
        mBack=findViewById(R.id.room_back);
        find_room = findViewById(R.id.room_search);//寻找房间的文本框

        if(first_treeHouse==false) {
            NewbieGuide.with(this)
                    .setLabel("guide1")
                    .alwaysShow(true)
                    .addGuidePage(GuidePage.newInstance()
                            .setLayoutRes(R.layout.tree_guide))
                    .addGuidePage(GuidePage.newInstance()
                            .setLayoutRes(R.layout.tree_guide1))
                    .addGuidePage(GuidePage.newInstance()
                            .setLayoutRes(R.layout.tree_guide2))
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLight(mDown)
                            .setLayoutRes(R.layout.tree_guide3))
                    .show();
            first_treeHouse=true;
        }


        //页面自动定位至底端
        mscrollView.post(new Runnable() {
            @Override
            public void run() {
                mscrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

        try {
            initData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TreeHouseStudyRoom.this, Map.class);
                startActivity(intent);
            }
        });

        //设置监听 “创建房间“
        mCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Thread thread = new Thread(new Runnable() {
                Runnable runnable = new Runnable() {
                    boolean hasPwd = true;
                    @Override
                    public void run() {
                        if(Looper.myLooper() == null) {
                            Looper.prepare();
                        }
                        dialog_roomcreate roomcreate = new dialog_roomcreate(TreeHouseStudyRoom.this, R.style.dialogOfShowRoom);
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
                            public void onCreateRoom(dialog_roomcreate roomcreate) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
                                String room_pwd = roomcreate.roomPass.getText().toString();
                                String room_name = roomcreate.roomName.getText().toString();

                                // if (db.existName(room_name)) {
                                if (u.checkRoomName(room_name))//如果房间名字符合要求
                                {
                                    if ( u.checkRoomPwd(room_pwd)) {//存入数据库:房间密码正确
                                        try {
                                            db.createTreeRoom(room_name, room_pwd);
                                            Intent intent = new Intent(TreeHouseStudyRoom.this, selfRoomMainActivity.class);
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
                                            db.createUndergroundRoom(room_name, null);
                                            Intent intent = new Intent(TreeHouseStudyRoom.this, selfRoomMainActivity.class);
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
                                        Toast.makeText(TreeHouseStudyRoom.this, "请输入四位密码（只包含英文大小写或数字）", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(TreeHouseStudyRoom.this, "房间名必须在2-10个字符之间，且都要为中英文或数字", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        roomcreate.show();
                        Looper.loop();
                    }
                };
                cachedThreadPool.execute(runnable);
//thread.start();
            }
        });

        //设置监听 "滑到底部"
        mDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mscrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        mscrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });

        //监听确定按钮
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Thread thread = new Thread(new Runnable() {
                 Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        //转字符
                        String find = find_room.getText().toString();
                        if (find.isEmpty()) {//如果搜索框为空
                            Toast.makeText(TreeHouseStudyRoom.this, "请输入房间id", Toast.LENGTH_SHORT).show();
                        } else {
                            if ((find.length() == 5) && isNumberic(find)) {//如果长度为5且都为数字:可继续
                                try {
                                    if (db.findRoom(find)) {//如果在数据库找到了id
                                        if (!db.isFull(find)) {//如果人数没满
                                            //db.enterRoom(find);
                                            dialog_roomshow roomshow = new dialog_roomshow(TreeHouseStudyRoom.this, R.style.dialogOfShowRoom);
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
                                                                    Intent intent = new Intent(TreeHouseStudyRoom.this, selfRoomMainActivity.class);
                                                                    startActivity(intent);
                                                                }else {//如果有密码 有弹窗输入密码
                                                                    dialog_roomeditpass roomeditpass = new dialog_roomeditpass(TreeHouseStudyRoom.this, R.style.dialogOfShowRoom);
                                                                    roomeditpass.setEnterWithPass(new dialog_roomeditpass.IonEnterRoomListener() {
                                                                        @Override
                                                                        public void onEnterRoom(dialog_roomeditpass roomeditpass) {//监听进入按钮
                                                                            String roomPwd = roomeditpass.editPass.getText().toString();

                                                                            if(u.checkRoomPwd(roomPwd)){//密码格式正确
                                                                                try {
                                                                                    if(db.pwdFound(find,roomPwd)){//如果房间名和密码匹配
                                                                                        db.enterRoom(find);
                                                                                        //跳转至房间内部
                                                                                        Intent intent = new Intent(TreeHouseStudyRoom.this, selfRoomMainActivity.class);
                                                                                        startActivity(intent);

                                                                                    }else{
                                                                                        Toast.makeText(TreeHouseStudyRoom.this,"密码错误，请重新输入",Toast.LENGTH_SHORT).show();
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
                                                                                Toast.makeText(TreeHouseStudyRoom.this,"密码错误，请重新输入",Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(TreeHouseStudyRoom.this, "房间人数已满", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(TreeHouseStudyRoom.this, "该房间不存在", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(TreeHouseStudyRoom.this, "请输入正确房间id", Toast.LENGTH_SHORT).show();
                            }
                        }
                      //  Looper.loop();
                    }
                };
                cachedThreadPool.execute(runnable);

                //thread.start();
            }
        });

        /**
        startTask();
         */
    }

    //模拟数据
    private void initData() throws InterruptedException {
         Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Random r = new Random();
                try {
                    for (int i = 0; i < db.existTreeRoomNumber(); i++) {//房间数量
                        room roomData = new room();
                        roomData.id = db.existTreeRoom().get(i);//房间id
                        roomData.roomName = db.findRoomName(roomData.id);
                        roomData.roomMember = db.findMemberNumber(roomData.id);
                        roomData.icon=roomPictures.roomPics[r.nextInt(3)];//加载图片
                        switch (i%2){
                            case 0:
                                roomData.type=3;
                                break;
                            case 1:
                                roomData.type=4;
                                break;
                        }
                        mroomList.add(roomData);//将房间数据添加到房间数据集合中
                        handler1.sendEmptyMessage(1);
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
        };
        cachedThreadPool.execute(runnable);

    }

    private void initListener() {
        madapter.setOnItemClickListener(new RecycleViewBaseAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String id = mroomList.get(position).id;
              Thread thread = new Thread(new Runnable() {
                // Runnable runnable = new Runnable() {
                @Override
                    public void run() {
                        try {
                            Looper.prepare();
                            if (!db.isFull(id)) {//如果人数没满
                                //出现房间信息展示弹窗
                                dialog_roomshow roomshow = new dialog_roomshow(TreeHouseStudyRoom.this, R.style.dialogOfShowRoom);
                                roomshow.setName(mroomList.get(position).roomName).setID(mroomList.get(position).id).setAnnouncement(mroomList.get(position).announce).
                                        setRoomEnter("进入房间", new dialog_roomshow.IonEnterRoomListener() {
                                            @Override
                                            public void onEnterRoom(dialog_roomshow roomshow) {//公告
                                                //出现“输入密码”弹窗
                                                try {
                                                    if (!db.hasPwd(id)) {//如果没密码 直接进入房间
                                                        db.enterRoom(id);
                                                        Intent intent = new Intent(TreeHouseStudyRoom.this, selfRoomMainActivity.class);
                                                        startActivity(intent);
                                                    } else {//如果有密码 有弹窗输入密码
                                                        dialog_roomeditpass roomeditpass = new dialog_roomeditpass(TreeHouseStudyRoom.this, R.style.dialogOfShowRoom);
                                                        roomeditpass.setEnterWithPass(new dialog_roomeditpass.IonEnterRoomListener() {
                                                            @Override
                                                            public void onEnterRoom(dialog_roomeditpass roomeditpass) {//监听进入按钮
                                                                String roomPwd = roomeditpass.editPass.getText().toString();
                                                                if (u.checkRoomPwd(roomPwd)) {//密码格式正确
                                                                    try {
                                                                        if (db.pwdFound(id, roomPwd)) {//如果房间名和密码匹配
                                                                            db.enterRoom(id);
                                                                            //跳转至房间内部
                                                                            Intent intent = new Intent(TreeHouseStudyRoom.this, selfRoomMainActivity.class);
                                                                            startActivity(intent);
                                                                        } else {
                                                                            Toast.makeText(TreeHouseStudyRoom.this, "密码错误，请重新输入", Toast.LENGTH_SHORT).show();
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
                                                                    Toast.makeText(TreeHouseStudyRoom.this, "密码错误，请重新输入", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(TreeHouseStudyRoom.this,"房间已满",Toast.LENGTH_SHORT).show();
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
                //cachedThreadPool.execute(runnable);
                thread.start();
            }
        });
    }


    private void showStagger(boolean isVertical, boolean isReverse) {
        //设置布局管理器
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,isVertical?StaggeredGridLayoutManager.VERTICAL:StaggeredGridLayoutManager.HORIZONTAL);

        //设置水平还是垂直
        //layoutManager.setOrientation(isVertical?GridLayoutManager.VERTICAL:GridLayoutManager.HORIZONTAL);

        //设置正向还是反向
        layoutManager.setReverseLayout(isReverse);
        mRecycleView.setLayoutManager(layoutManager);

        //创建适配器Adapter
        madapter=new MoreTypeBeanAdapter(mroomList,RecycleViewBaseAdapter.THEME_TREEHOUSE);

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
