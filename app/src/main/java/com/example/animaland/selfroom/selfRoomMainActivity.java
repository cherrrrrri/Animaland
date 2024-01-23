package com.example.animaland.selfroom;

import static com.example.animaland.tool.ThreadHelper.cachedThreadPool;
import static io.agora.rtc2.video.VideoCanvas.RENDER_MODE_HIDDEN;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.example.animaland.Dialogs.dialog_dictionary;
import com.example.animaland.Dialogs.dialog_endTiming;
import com.example.animaland.Dialogs.dialog_focus;
import com.example.animaland.Dialogs.dialog_music;
import com.example.animaland.Dialogs.dialog_roomInformationCheck;
import com.example.animaland.Dialogs.dialog_roomexit;
import com.example.animaland.Dialogs.dialog_stopTiming;
import com.example.animaland.Map;
import com.example.animaland.R;
import com.example.animaland.SeatFolder.dialog_todo;
import com.example.animaland.netConnect.netConnect.NetworkService;
import com.example.animaland.tool.BasicActivity;
import com.example.animaland.tool.DatabaseHelper;
import com.example.animaland.tool.User;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;

import java.sql.SQLException;

import io.agora.rtc2.ChannelMediaOptions;
import io.agora.rtc2.Constants;
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.RtcEngineConfig;
import io.agora.rtc2.video.VideoCanvas;

public class selfRoomMainActivity extends BasicActivity {
    // 填写项目的 App ID，可在 Agora 控制台中生成。
    private String appId = "904f83356dbb4ca98da854449825bac6";
    // 填写频道名称。
    private String channelName = "animaland";
    private RtcEngine mRtcEngine;
    private ImageView camera, micro_phone;
    private static MediaPlayer mediaPlayer = null;
    DatabaseHelper db = new DatabaseHelper();//数据库
    int memberNumber;//现在房间里有的人数
    long recordingtime;
    Object mCallbackObj;
    String seat[] = new String[4];//可以先不要理它
    private PopupWindow mpop;
    private dialog_focus focusDialog;
    private dialog_todo todoDialog;
    private dialog_stopTiming stopTimingDialog;
    private dialog_roomInformationCheck checkDialog;
    private static int btn_state;
    private FrameLayout frameLayout;
    private static int state;
    private EditText focus_studyTime;
    private ViewPager vp_content;

    private CircleMenu circleMenu;
    int i=0;
    int i_camera=0;
    private Runnable task;

    private static boolean first_selfRoom=false;
    private Handler handler = new Handler(Looper.myLooper()) {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            User u = new User();
            if (msg.what == 0) {
                memberNumber = u.getSeat();

                // 如果已经授权，则初始化 RtcEngine 并加入频道。
                if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                        checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID)) {
                    initializeAndJoinChannel();
                }

            }
    }
    };
    private int delay = 5000;

    // 开启定时任务：监听房主id是否变化
    private void startTask() {
        stopTask(); //关闭之前的定时
        handler.postDelayed(task = new Runnable() {
            @Override
            public void run() {
                //执行任务....
                User u = new User();
                if (!u.isMaster) {
                   // Thread thread = new Thread(new Runnable() {
                     Runnable runnable = new Runnable() {
                        boolean hasPwd = true;

                        @Override
                        public void run() {
                            try {
                                if (u.tel==db.getMasterId(u.getRoomId())) {
                                    u.setMaster(true);
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
                   // thread.start();
                    // 任务执行完后再次调用postDelayed开启下一次任务
                    handler.postDelayed(this, delay);
                }
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

    public void getMumberNumber() throws InterruptedException {
        handler.sendEmptyMessage(0);
    }


    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onUserOffline(int uid, int reason)
        {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    /**Clear render view
                     Note: The video will stay at its last frame, to completely remove it you will need to
                     remove the SurfaceView from its parent*/
                    mRtcEngine.setupRemoteVideo(new VideoCanvas(null, RENDER_MODE_HIDDEN, uid));
                    getFrame(uid).removeAllViews();
                }
            });
        }
        @Override
        // 监听频道内的远端主播，获取主播的 uid 信息。
        //直播场景下，该回调提示有主播加入了频道。如果加入之前，已经有主播在频道中了，新加入的用户也会收到已有主播加入频道的回调。
        public void onUserJoined(int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @SuppressLint("SuspiciousIndentation")
                @Override// 从 onUserJoined 回调获取 uid 后，调用 setupRemoteVideo，设置远端视频视图。
                public void run() {
                    if (uid == 1)
                    {
                        setupRemoteVideo1(uid);
                    }
                    if (uid == 2) {
                        setupRemoteVideo2(uid);
                    }
                    if (uid == 3) {
                        setupRemoteVideo3(uid);
                    }
                    if (uid == 4) {
                        setupRemoteVideo4(uid);
                    }
                }
            });
        }
    };

    private FrameLayout getFrame(int memberNumber) {
        FrameLayout temp = null;
        if (memberNumber == 1) {
            temp = findViewById(R.id.video_view_container1);
        } else if (memberNumber == 2) {
            temp = findViewById(R.id.video_view_container2);
        } else if (memberNumber == 3) {
            temp = findViewById(R.id.video_view_container3);
        } else if (memberNumber == 4) {
            temp = findViewById(R.id.video_view_container4);
        } else {
            temp = findViewById(R.id.video_view_container2);
        }
        return temp;
    }

    private void initializeAndJoinChannel() {
        try {
            RtcEngineConfig config = new RtcEngineConfig();
            config.mContext = getBaseContext();
            config.mAppId = appId;
            config.mEventHandler = mRtcEventHandler;
            mRtcEngine = RtcEngine.create(config);
        } catch (Exception e) {
            throw new RuntimeException("Check the error.");
        }

        ChannelMediaOptions options = new ChannelMediaOptions();
        // 视频通话场景下，设置频道场景为 BROADCASTING。
        options.channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING;
        // 将用户角色设置为 BROADCASTER。
        options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER;
        // 使用临时 Token 加入频道。
        // 你需要自行指定用户 ID，并确保其在频道内的唯一性。
        //uid可设置默认为0，则声网会自动给予用户一个uid
        mRtcEngine.joinChannel(null, channelName, memberNumber, options);
        mRtcEngine.muteLocalAudioStream(true);
    }

    private void setupRemoteVideo1(int uid) {
        FrameLayout container = findViewById(R.id.video_view_container1);
        SurfaceView surfaceView=new SurfaceView(getBaseContext());
        surfaceView.setZOrderMediaOverlay(true);
        container.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid));
    }

    private void setupRemoteVideo2(int uid) {
        FrameLayout container = findViewById(R.id.video_view_container2);
        SurfaceView surfaceView=new SurfaceView(getBaseContext());
        surfaceView.setZOrderMediaOverlay(true);
        container.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid));
    }

    private void setupRemoteVideo3(int uid) {
        FrameLayout container = findViewById(R.id.video_view_container3);
        SurfaceView surfaceView=new SurfaceView(getBaseContext());
        surfaceView.setZOrderMediaOverlay(true);
        container.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid));
    }

    private void setupRemoteVideo4(int uid) {
        FrameLayout container = findViewById(R.id.video_view_container4);
        SurfaceView surfaceView=new SurfaceView(getBaseContext());
        surfaceView.setZOrderMediaOverlay(true);
        container.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid));
    }

    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }

    public void onSwitchCameraClicked(View view) {
        mRtcEngine.switchCamera();
    }

    public void OnLocalAudioMuteOff(View view){
        mRtcEngine.muteLocalAudioStream(true);
        //muted 是否取消发布本地音频流。true: 取消发布。false:（默认）发布。
    }

    public void OnLocalAudioMuteOn(View view){
        mRtcEngine.muteLocalAudioStream(false);
        //muted 是否取消发布本地音频流。true: 取消发布。false:（默认）发布。
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selfroom_activity_main);
        pauseMusic();
        try {
            getMumberNumber();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        User u = new User();
        vp_content=findViewById(R.id.vp_content);
        frameLayout=getFrame(u.getSeat());
        camera = findViewById(R.id.camera);
        micro_phone=findViewById(R.id.micro_phone);
        circleMenu = (CircleMenu) findViewById(R.id.circle_menu);
        focusDialog=new dialog_focus(selfRoomMainActivity.this,R.style.dialogOfShowRoom);
        todoDialog=new dialog_todo(selfRoomMainActivity.this,R.style.dialogOfShowRoom);
        checkDialog = new dialog_roomInformationCheck(selfRoomMainActivity.this,R.style.dialogOfShowRoom);
        stopTimingDialog = new dialog_stopTiming(selfRoomMainActivity.this,R.style.dialogOfShowRoom);
        btn_state=1;

        //新手引导


        if(first_selfRoom==false) {
            NewbieGuide.with(this)
                    .setLabel("guide1")
                    .alwaysShow(true)
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLight(circleMenu)
                            .setLayoutRes(R.layout.steward_guide))
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLight(circleMenu)
                            .setLayoutRes(R.layout.steward_guide1))
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLight(circleMenu)
                            .setLayoutRes(R.layout.steward_guide2))
                    .show();
            first_selfRoom=true;
        }




        final Chronometer timer = (Chronometer) findViewById(R.id.timer);
        timer.setFormat("%s");

        Intent intent2=new Intent(this,Dress.class);
        Intent intent = new Intent();
        intent.setClass(selfRoomMainActivity.this, NetworkService.class);
        startService(intent);



        //Thread thread = new Thread(new Runnable() {
          Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    checkDialog.setRoomAnnouncement(db.findRoomAnnouncement(u.getRoomId()));
                    checkDialog.setRoomName(db.findRoomName(u.getRoomId()));
                    checkDialog.setRoomID(u.getRoomId());
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
        //thread.start();

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i_camera++;
                if(i_camera%2==0){
                    camera.setImageResource(R.drawable.camera_off);
                    getFrame(memberNumber).removeAllViews();
                    mRtcEngine.leaveChannel();
                }
                else{
                    camera.setImageResource(R.drawable.camera_on);
                    // 视频默认禁用，你需要调用 enableVideo 启用视频流。
                    mRtcEngine.enableVideo();
                    // 开启本地视频预览。
                    mRtcEngine.startPreview();
                    SurfaceView surfaceView = new SurfaceView(getBaseContext());
                    // 创建一个 SurfaceView 对象，并将其作为 FrameLayout 的子对象。
                    getFrame(memberNumber).addView(surfaceView);
                    // 将 SurfaceView 对象传入 Agora，以渲染本地视频。
                    mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, RENDER_MODE_HIDDEN, memberNumber));
                }

            }
        });



        micro_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                if (i%2==0){
                    micro_phone.setImageResource(R.drawable.micro_of);
                    OnLocalAudioMuteOff(view);
                }else {
                    micro_phone.setImageResource(R.drawable.micro_on);
                    OnLocalAudioMuteOn(view);
                }
            }
        });
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v=getLayoutInflater().inflate(R.layout.folder,null);

                Button focus=v.findViewById(R.id.bt_focus);
                Button todo=v.findViewById(R.id.bt_todo);

                //设置“FOCUS”的点击事件
                focus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mpop.dismiss();
                        focusDialog.setState(btn_state).setStart( new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //设置"开始专注“按钮的点击事件
                                if(focusDialog.checkBeforeStart()) {
                                    if(state==2)
                                    {
                                        recordingtime=0;
                                        timer.setBase(SystemClock.elapsedRealtime()-recordingtime);
                                    }
                                    if(state==1){
                                        String text= focusDialog.et_studyTime.getText().toString();
                                        int min=Integer.parseInt(text);
                                        int total=min*60000;
                                        timer.setBase(SystemClock.elapsedRealtime()+total);
                                    }
                                    timer.start();
                                    Toast.makeText(selfRoomMainActivity.this, "计时开始~", Toast.LENGTH_SHORT).show();
                                    focusDialog.setState(dialog_focus.STATE_STOP);//将下一个界面设为界面2
                                    btn_state = focusDialog.getState();
                                    focusDialog.dismiss();//弹窗消失
                                }
                                else
                                    Toast.makeText(selfRoomMainActivity.this, "请完善专注计划~", Toast.LENGTH_SHORT).show();
                            }
                        }).setEndEarly(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //设置“暂停计时”按钮的点击事件
                                timer.stop();//停止计时
                                recordingtime=SystemClock.elapsedRealtime()-timer.getBase();  //getBase():返回时间
                                if(dialog_stopTiming.isOver==true){
                                    Toast.makeText(selfRoomMainActivity.this,"暂停时间已耗尽",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    stopTimingDialog.setCanceledOnTouchOutside(false);//将“继续计时”弹窗外的界面设为不可点击
                                    stopTimingDialog.setGoon(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //设置“继续计时”按钮的点击事件
                                            dialog_stopTiming.fiveMin.stop();
                                            timer.setBase(SystemClock.elapsedRealtime() - recordingtime);
                                            timer.start();
                                            stopTimingDialog.dismiss();//弹窗消失
                                            focusDialog.dismiss();//弹窗消失
                                        }
                                    }).show();
                                }
                            }
                        }).setEnd(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //设置“结束计时”按钮的点击事件
                                dialog_endTiming endTimingDialog=new dialog_endTiming(selfRoomMainActivity.this,R.style.dialogOfShowRoom);
                                endTimingDialog.setEndEarly(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        recordingtime=SystemClock.elapsedRealtime()-timer.getBase();//传入数据库时间
                                        timer.stop();
                                        timer.setBase(SystemClock.elapsedRealtime());
                                        recordingtime=0;
                                        //设置“提前结束计时”按钮的点击事件
                                        Toast.makeText(selfRoomMainActivity.this, "计划已经提前结束", Toast.LENGTH_SHORT).show();
                                        focusDialog.setState(dialog_focus.STATE_START);//将界面设置为界面1
                                        btn_state=focusDialog.getState();
                                        dialog_stopTiming.isOver=false;
                                        endTimingDialog.dismiss();
                                        focusDialog.dismiss();//弹窗消失
                                    }
                                }).setEndAbandon(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //设置“放弃当前计时”按钮的点击事件
                                        recordingtime=0;
                                        timer.setBase(SystemClock.elapsedRealtime());
                                        focusDialog.setState(dialog_focus.STATE_START);//将界面设置为界面1
                                        btn_state=focusDialog.getState();
                                        dialog_stopTiming.isOver=false;
                                        endTimingDialog.dismiss();
                                        focusDialog.dismiss();//弹窗消失
                                    }
                                }).setBack(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //设置“取消”按钮的点击事件
                                        Toast.makeText(selfRoomMainActivity.this, "计时已恢复", Toast.LENGTH_SHORT).show();
                                        endTimingDialog.dismiss();
                                    }
                                }).show();
                            }
                        }).setTimingMode(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                switch (i){
                                    case R.id.timing_forward:
                                        state=2;
                                        focusDialog.et_studyTime.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                focusDialog.et_studyTime.setVisibility(View.INVISIBLE);//选择”正计时”时隐藏"专注时长“
                                                focusDialog.minute.setVisibility(View.INVISIBLE);//隐藏“分钟”
                                                focusDialog.setMode(dialog_focus.MODE_FORWARD);//将计时模式设置为“正计时”
                                                timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                                                    @Override
                                                    public void onChronometerTick(Chronometer chronometer) {
                                                        if(SystemClock.elapsedRealtime()-timer.getBase()>3600*1000)//若计时超过了3600s=1 h即停止计时
                                                        {
                                                            timer.stop();
                                                            dialog_stopTiming.isOver=false;
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                        break;
                                    case R.id.timing_backward:
                                        state=1;
                                        focusDialog.et_studyTime.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                focusDialog.et_studyTime.setVisibility(View.VISIBLE);//选择”倒计时”时显示"专注时长“
                                                focusDialog.minute.setVisibility(View.VISIBLE);//显示“分钟”
                                                focusDialog.setMode(dialog_focus.MODE_BACKFORWARD);//将计时模式设置为“倒计时”

                                                timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                                                    @Override
                                                    public void onChronometerTick(Chronometer chronometer) {
                                                        timer.setText(timer.getText().toString().substring(1));
                                                        if (SystemClock.elapsedRealtime()-timer.getBase()>=0){
                                                            timer.stop();
                                                            dialog_stopTiming.isOver=false;
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                        break;
                                }
                            }
                        }).show();
                    }
                });


                todo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mpop.dismiss();
                        todoDialog.show();
                    }
                });

                mpop=new PopupWindow(v,frameLayout.getWidth()/2, frameLayout.getHeight()/2);
                mpop.setFocusable(true);
                mpop.setOutsideTouchable(true);

                //获取父按钮的坐标
                int[] location = new int[2];
                frameLayout.getLocationOnScreen(location);
                int X = location[0];
                int Y = location[1];
                //计算出mpop的坐标使得其居中显示
                int x=X+frameLayout.getWidth()/2-mpop.getWidth()/2;
                int y=Y+frameLayout.getHeight()/2-mpop.getHeight()/2;
                mpop.showAtLocation(v, Gravity.NO_GRAVITY,x,y);//设置mpop位置
            }
        });

        startTask();

        RelativeLayout relativeLayout =findViewById(R.id.relative_layout);
        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.drawable.cheese1, R.drawable.cheese2)
                .addSubMenu(Color.parseColor("#258CFF"), R.drawable.music)
                .addSubMenu(Color.parseColor("#30A400"), R.drawable.cloth)
                .addSubMenu(Color.parseColor("#8A39FF"), R.drawable.settings)
                .addSubMenu(Color.parseColor("#F23C68"), R.drawable.back)
                .addSubMenu(Color.parseColor("#FFB336"), R.drawable.ic_baseline_dictionary);

        circleMenu.setOnMenuSelectedListener(new OnMenuSelectedListener() {
            public final dialog_dictionary dictionary=new dialog_dictionary(selfRoomMainActivity.this,R.style.dialogOfShowRoom);
            public final  dialog_music music=new dialog_music(selfRoomMainActivity.this,R.style.dialogOfShowRoom);
            String info1 = null;
            String info2 = null;
            String inter = null;
            boolean find = false;

            private Handler handler = new Handler(Looper.myLooper()){
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);

                    if(msg.what==4){

                        System.out.println(22222222);

                        Toast.makeText(selfRoomMainActivity.this,"无该词解释，请检查输入单词有无错误~",Toast.LENGTH_SHORT).show();
                    }else if(msg.what==3){

                        System.out.println(22222222);

                        dictionary.changeText0();
                        dictionary.changeText2((String) msg.obj);
                    }
                }
            };
            //第二个参数是让弹窗背景透明，不然弹窗四角被削去的部分会是白色背景
            @Override
            public void onMenuSelected(int index) {

                switch (index) {
                    case 0://音乐
                        relativeLayout.setBackgroundColor(Color.parseColor("#ecfffb"));
                        music.setI11(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //点击i11后的效果：
                                if (music.getState1() == 0) {
                                    music.change_image1();
                                    if(mediaPlayer!=null)
                                        mediaPlayer.reset();
                                    mediaPlayer=MediaPlayer.create(selfRoomMainActivity.this,R.raw.sea);
                                    mediaPlayer.start();
                                    mediaPlayer.setLooping(true);
                                } else {
                                    music.change_image2();
                                    if(music.getState1() == 0&&music.getState2() == 0&&music.getState3() == 0&&music.getState4() == 0){
                                        mediaPlayer.pause();
                                    }
                                }
                            }
                        });
                        music.setI22(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //点击i22后的效果：
                                if (music.getState2() == 0) {
                                    music.change_image3();
                                    if(mediaPlayer!=null)
                                        mediaPlayer.reset();
                                    mediaPlayer=MediaPlayer.create(selfRoomMainActivity.this,R.raw.forest);
                                    mediaPlayer.start();
                                    mediaPlayer.setLooping(true);
                                } else {
                                    music.change_image4();
                                    if(music.getState1() == 0&&music.getState2() == 0&&music.getState3() == 0&&music.getState4() == 0){
                                        mediaPlayer.pause();
                                    }
                                }
                            }
                        });
                        music.setI33(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //点击i33后的效果：
                                if (music.getState3() == 0) {
                                    music.change_image5();
                                    if(mediaPlayer!=null)
                                        mediaPlayer.reset();
                                    mediaPlayer=MediaPlayer.create(selfRoomMainActivity.this,R.raw.rain);
                                    mediaPlayer.start();
                                    mediaPlayer.setLooping(true);
                                } else {
                                    music.change_image6();
                                    if(music.getState1() == 0&&music.getState2() == 0&&music.getState3() == 0&&music.getState4() == 0){
                                        mediaPlayer.pause();}
                                }
                            }
                        });
                        music.setI44(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //点击i44后的效果：
                                if (music.getState4() == 0) {
                                    music.change_image7();
                                    if(mediaPlayer!=null)
                                        mediaPlayer.reset();
                                    mediaPlayer=MediaPlayer.create(selfRoomMainActivity.this,R.raw.sky);
                                    mediaPlayer.start();
                                    mediaPlayer.setLooping(true);
                                } else {
                                    music.change_image8();
                                    if(music.getState1() == 0&&music.getState2() == 0&&music.getState3() == 0&&music.getState4() == 0){
                                        mediaPlayer.pause();}
                                }
                            }
                        });
                        music.setClose(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                music.hide();
                            }
                        });
                        music.show();
                        break;

                    case 1://换装
                        relativeLayout.setBackgroundColor(Color.parseColor("#96f7d2"));
                        Handler handler2 = new Handler();
                        handler2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(intent2);
                            }
                        }, 700);
                        break;

                    case 2://设置
                        relativeLayout.setBackgroundColor(Color.parseColor("#d3cdef"));
                        checkDialog.show();
                        break;

                    case 3://返回
                        relativeLayout.setBackgroundColor(Color.parseColor("#d3cdef"));//颜色随便改
                        dialog_roomexit roomexit = new dialog_roomexit(selfRoomMainActivity.this, R.style.dialogOfShowRoom);
                        roomexit.setYes(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                music.dismiss();
                                if(mediaPlayer!=null)
                                {mediaPlayer.release();}
                                mRtcEngine.leaveChannel();
                                mRtcEngine.stopPreview();
                                RtcEngine.destroy();
                                mRtcEngine=null;
                                dialog_stopTiming.isOver=false;
                                playMusic();
                                Intent intent4 = new Intent(selfRoomMainActivity.this, Map.class);
                                startActivity(intent4);
                            }
                        }).setNo(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                roomexit.dismiss();
                            }
                        }).show();
                        //退出房间
                       // Thread thread = new Thread(new Runnable() {//执行数据库操作
                         Runnable runnable1 = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    db.exitRoom();
                                    db.deleteRoom();
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
                         cachedThreadPool.execute(runnable1);
                      //  thread.start();
                        break;
                    case 4://字典
                        relativeLayout.setBackgroundColor(Color.parseColor("#EFEAE0"));
                        dictionary.setText(new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                                String search = dictionary.dictionary_hint.getText().toString();

                                if(info1.equals("日语")){
                                    if(info2.equals("中文")){
                                        Runnable runnable2 = new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    System.out.println(1111111111);
                                                    if(db.findInterpre(2,search)==null){
                                                        find = false;
                                                        handler.sendEmptyMessage(4);
                                                    }else{
                                                       inter = db.findInterpre(2,search);
                                                       find = true;
                                                        Message msg = new Message();
                                                        msg.what=3;
                                                        msg.obj = inter;
                                                        handler.sendMessage(msg);
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
                                       cachedThreadPool.execute(runnable2);
                                    }else{
                                        Toast.makeText(selfRoomMainActivity.this,"暂无该词典，敬请期待~",Toast.LENGTH_SHORT).show();
                                    }
                                }else if(info1.equals("英语")){
                                    if(info2.equals("中文")){
                                        Runnable runnable2 = new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    if(db.findInterpre(1,search)==null) {
                                                        System.out.println(1111111111 + " " + inter + " "+search);

                                                        find = false;
                                                        handler.sendEmptyMessage(4);

                                                    } else{
                                                       inter = db.findInterpre(1,search);
                                                       System.out.println(1111111111 + " " + inter + " "+search);
                                                       find = true;
                                                       Message msg = new Message();
                                                       msg.what=3;
                                                       msg.obj = inter;
                                                       handler.sendMessage(msg);

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
                                        cachedThreadPool.execute(runnable2);
                                    }else{
                                        Toast.makeText(selfRoomMainActivity.this,"暂无该词典，敬请期待~",Toast.LENGTH_SHORT).show();

                                    }
                                }else{
                                    Toast.makeText(selfRoomMainActivity.this,"暂无该词典，敬请期待~",Toast.LENGTH_SHORT).show();

                                }


                                return false;
                            }
                        }).setClose(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dictionary.clear();

                            }
                        }).setSpinner1(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                info1 = adapterView.getItemAtPosition(i).toString();//获取i所在的文本
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        }).setSpinner2(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                info2 = adapterView.getItemAtPosition(i).toString();//获取i所在的文本
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        }).show();

                        break;
                }
            }
        });
        circleMenu.setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {
            @Override
            public void onMenuOpened() {
            }

            @Override
            public void onMenuClosed() {
            }
        });


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