package com.example.animaland.School;

import static com.example.animaland.School.BiliDanmukuParser.getDefaultDanmakuParser;
import static com.example.animaland.tool.BasicActivity.pauseMusic;
import static com.example.animaland.tool.BasicActivity.playMusic;
import static com.example.animaland.tool.ThreadHelper.cachedThreadPool;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.example.animaland.Adapters.RecycleViewAdapter;
import com.example.animaland.IMManager;
import com.example.animaland.R;
import com.example.animaland.tool.DatabaseHelper;
import com.example.animaland.tool.User;
import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMCustomElem;
import com.tencent.imsdk.v2.V2TIMGroupInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMSendCallback;
import com.tencent.imsdk.v2.V2TIMTextElem;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import io.agora.rtc2.ChannelMediaOptions;
import io.agora.rtc2.Constants;
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.RtcEngineConfig;
import io.agora.rtc2.video.VideoCanvas;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.util.IOUtils;

public class Auditorium_teacher extends AppCompatActivity {
    private ImageView micro_phone;
    private ImageView camera_change;
    private ImageView upload;
    private ImageView all_silent;
    private ImageView camera_close;
    private ImageView bulletSwitch;
    private CountTextView countTextView;
    private ImageView comment;
    private DrawerLayout drawerLayout;
    private MediaUtil mediaUtil;
    private EditText hint;
    private RecyclerView recyclerView;
    private String Mystring;
    private ImageView type;
    private ImageView board;
    private ImageView back;
    private TextView likenum;
    private TextView audiencenum;
    private  classroom classroom;


    //弹幕相关
    public static int QUESTION=0;
    public static int ANSWER=1;
    public static int OTHER=2;
    private IDanmakuView bulletsView;
    private BaseDanmakuParser mParser;
    private DanmakuContext mContext;
    private boolean isBulletShow=true;//记录弹幕开关状态，默认为开
    ArrayList<talkModel> talks =new ArrayList<>();/*聊天记录就传入这个数组*/
    RecycleViewAdapter recycleViewAdapter=new RecycleViewAdapter(Auditorium_teacher.this,talks);
    public static int state=1;

    //点赞相关
    static int likes=0;

    private User user=new User();

    //获取群在线人数
    private Timer timer;

    // 填写项目的 App ID，可在 Agora 控制台中生成。
    private String appId = "904f83356dbb4ca98da854449825bac6";
    // 填写频道名称。
    private String ROOMID;
    private String ROOMNAME;
    private RtcEngine mRtcEngine;
    private ChannelMediaOptions options = new ChannelMediaOptions();
    private FrameLayout fl_screen;
    private static boolean first_teacher=false;
    int i1=0;
    int i2=0;
    int i3=0;
    private DatabaseHelper db = new DatabaseHelper();
    private Handler handler=new Handler();
    static int objective=20;/*疑问阈值*/

    public static int isRunning=0;
    MyThread my = new MyThread()
    {
        public void run() {
            while(true){
                judge();
            }
        }
    };

    private Handler handler1=new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if(msg.what==0){//清零
                countTextView.requestLayout();
                countTextView.invalidate();
                countTextView.draw(0);
                Log.i("handler","message=0");
            }else if(msg.what==1){//更新问题数
                countTextView.requestLayout();
                countTextView.invalidate();
                countTextView.draw((int)msg.obj);
                Log.i("handler","message=1"+msg.obj);
            }else if(msg.what==2){

                // 如果已经授权，则初始化 RtcEngine 并加入频道。
                if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                        checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID)) {
                    initializeAndJoinChannel();
                }

                joinRoom_chat();
                joinRoom_answer();
                joinRoom_question();
                getMessage();
            }else if(msg.what==3){
                Toast.makeText(getContext(),classroom.roomName +" "+classroom.instructor.name,Toast.LENGTH_SHORT).show();
            }
        }
    };

    //定时器1：每五秒返回一次提问人数
    private int delay1 = 5000;
    private Runnable task1;
    private void startReturnQuestion() {
        stopReturnQuestion(); //关闭之前的定时
        handler.postDelayed(task1 = new Runnable() {
            @Override
            public void run() {
                //执行任务....
                Runnable runnable=new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int question=db.getQuestion();
                            Log.i("问题数量监测","问题数量:"+question);
                            Message message = new Message();
                            message.what=1;
                            message.obj=question;
                            handler1.sendMessage(message);

                            //如果超过阈值
                            if(question>objective) {
                                judge();
                                db.updateQuestion();//清空问题数量
                                handler1.sendEmptyMessage(0);
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

                // 任务执行完后再次调用postDelayed开启下一次任务
                handler.postDelayed(this, delay1);
            }
        }, delay1);
    }

    // 停止定时任务1
    private void stopReturnQuestion() {
        if (task1 != null) {
            handler.removeCallbacks(task1);
            task1 = null;
        }
    }

    //定时器2：每五分钟清空一次提问
    private int delay2 = 300000;
    private Runnable task2;
    private void startUpdateQuestion() {
        stopUpdateQuestion(); //关闭之前的定时
        handler.postDelayed(task2 = new Runnable() {
            @Override
            public void run() {
                //执行任务....
                Runnable runnable=new Runnable() {
                    @Override
                    public void run() {
                        try {
                            db.updateQuestion();
                            Log.i("问题数量监测","清空");

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
                // 任务执行完后再次调用postDelayed开启下一次任务
                handler.postDelayed(this, delay2);
            }
        }, delay2);
    }
    // 停止定时任务2
    private void stopUpdateQuestion() {
        if (task2 != null) {
            handler.removeCallbacks(task2);
            task2 = null;
        }
    }

    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onUserJoined(int uid, int elapsed) {
            super.onUserJoined(uid, elapsed);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("agora","jianting");
                }
            });
        }
    };

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

        // 直播场景下，设置频道场景为 BROADCASTING。
        options.channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING;
        // 根据场景将用户角色设置为 BROADCASTER 或 AUDIENCE。
        options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER;

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


    public void OnLocalAudioMuteOff(View view){
        mRtcEngine.muteLocalAudioStream(true);
        //muted 是否取消发布本地音频流。true: 取消发布。false:（默认）发布。
    }

    public void OnLocalAudioMuteOn(View view){
        mRtcEngine.muteLocalAudioStream(false);
        //muted 是否取消发布本地音频流。true: 取消发布。false:（默认）发布。
    }

    public Context getContext() {
        Activity activity = Auditorium_teacher.this;
        return activity;
    }

    private BaseCacheStuffer.Proxy mCacheStufferAdapter = new BaseCacheStuffer.Proxy() {
        private Drawable mDrawable;

        @Override
        public void prepareDrawing(final BaseDanmaku danmaku, boolean fromWorkerThread) {
            if (danmaku.text instanceof Spanned) { // 根据你的条件检查是否需要需要更新弹幕
                // FIXME 这里只是简单启个线程来加载远程url图片，请使用你自己的异步线程池，最好加上你的缓存池
                new Thread() {
                    @Override
                    public void run() {
                        String url = "http://www.bilibili.com/favicon.ico";
                        InputStream inputStream = null;
                        Drawable drawable = mDrawable;
                        if(drawable == null) {
                            try {
                                URLConnection urlConnection = new URL(url).openConnection();
                                inputStream = urlConnection.getInputStream();
                                drawable = BitmapDrawable.createFromStream(inputStream, "bitmap");
                                mDrawable = drawable;
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                IOUtils.closeQuietly(inputStream);
                            }
                        }
                        if (drawable != null) {
                            drawable.setBounds(0, 0, 100, 100);
                            SpannableStringBuilder spannable = createSpannable(drawable,"测试");
                            danmaku.text = spannable;
                            if(bulletsView != null) {
                                bulletsView.invalidateDanmaku(danmaku, false);
                            }
                            return;
                        }
                    }
                }.start();
            }
        }

        @Override
        public void releaseResource(BaseDanmaku danmaku) {
            // TODO 重要:清理含有ImageSpan的text中的一些占用内存的资源 例如drawable
        }
    };


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auditorium_teacher);
        pauseMusic();

        micro_phone=findViewById(R.id.micro_phone);
        camera_change=findViewById(R.id.camera_change);
        all_silent=findViewById(R.id.all_silent);
        camera_close=findViewById(R.id.camera_close);
        countTextView=findViewById(R.id.question_num);
        comment=findViewById(R.id.comment);
        drawerLayout=findViewById(R.id.drawerLayout2);
        hint=findViewById(R.id.hint);
        fl_screen = this.findViewById(R.id.local_video_view_container);
        bulletsView=findViewById(R.id.BulletsView);
        bulletSwitch=findViewById(R.id.bullet);
        board=findViewById(R.id.board);
        back=findViewById(R.id.back);
        recyclerView=findViewById(R.id.talklayout);
        upload=findViewById(R.id.upload);
        likenum=findViewById(R.id.likenum);
        audiencenum=findViewById(R.id.audience_num);


        LayoutInflater inflater = LayoutInflater.from(Auditorium_teacher.this) ;                         //先获取当前布局的填充器
        View itemView = inflater.inflate(R.layout.items, null);   //通过填充器获取另外一个布局的对象
        type=itemView.findViewById(R.id.type);

        isRunning=1;

        recyclerView.setAdapter(recycleViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Auditorium_teacher.this));

        ROOMID=user.roomId;
        ROOMNAME=user.roomName;
        System.out.println("Auditorium,roomid"+ROOMID);
        System.out.println("Auditorium,roomNAME"+ROOMNAME);


        for(int i = 0;i<2;i++)
            System.out.println(user.roomId+" "+user.roomName+" " + ROOMID+" "+ROOMNAME);
        handler1.sendEmptyMessage(2);

        setBulletsView();//设置弹幕显示参数

        //传进classroom数据
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    classroom = db.getClassroom(user.roomId);
                    System.out.println(classroom.roomName +" "+classroom.instructor.name);
                   // handler1.sendEmptyMessage(3);
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


        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                V2TIMManager.getGroupManager().getGroupOnlineMemberCount(ROOMID+"chat", new V2TIMValueCallback<Integer>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        // 获取直播群在线人数成功
                        Log.i("腾讯即时通讯IM","获取直播群在线人数成功");
                        audiencenum.setText("在线观众："+integer);
                    }

                    @Override
                    public void onError(int code, String desc) {
                        // 获取直播群在线人数失败
                        Log.e("腾讯即时通讯IM","获取直播群在线人数失败"+code+","+desc);
                    }
                });
            }
        },0,1000*60);//每隔60秒执行一次,一直重复执行


        hint.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND) {
                    Mystring= String.valueOf(hint.getText());
                    sendMessage(Mystring);
                    state=1;

                    talks.add(Auditorium_student.position,new talkModel(Mystring));
                    recycleViewAdapter.notifyItemInserted(Auditorium_student.position);
                    recyclerView.scrollToPosition(Auditorium_student.position);
                    Auditorium_student.position++;
                    // Toast.makeText(Auditorium_teacher.this,"send",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        final dialog_questionNum num=new dialog_questionNum(Auditorium_teacher.this,R.style.dialogOfShowRoom);
        countTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                num.setCancelable(true);
                num.setBtn(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        num.getEditText();
                        if(num.getText()!=""){
                            objective=Integer.parseInt(num.getText());
                            Toast.makeText(Auditorium_teacher.this, "设置成功", Toast.LENGTH_SHORT).show();
                            num.dismiss();
                        }
                        else {
                            Toast.makeText(Auditorium_teacher.this, "数值为空，设置失败", Toast.LENGTH_SHORT).show();
                            num.dismiss();
                        }
                    }
                }).show();
                return false;
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        /*单击图标疑问清零*/
        countTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                countTextView.requestLayout();
                countTextView.invalidate();
                countTextView.draw(0);
                my.resumeThread();
                Toast.makeText(Auditorium_teacher.this,"疑问清零",Toast.LENGTH_SHORT).show();
                Runnable runnable= new Runnable() {
                    @Override
                    public void run() {
                        try {
                            db.updateQuestion();
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
        });

        final dialog_upload upload1=new dialog_upload(Auditorium_teacher.this,R.style.dialogOfShowRoom);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(Auditorium_teacher.this,"文件发送成功",Toast.LENGTH_SHORT).show();
                upload1.setYes(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String filename=upload1.upload_hint.getText().toString();
                        String url="/sdcard/Download/Weixin/"+filename;
                        // 创建文件消息
                        V2TIMMessage v2TIMMessage = V2TIMManager.getMessageManager().createFileMessage(url, filename);
                        // 发送消息
                        V2TIMManager.getMessageManager().sendMessage(v2TIMMessage, null, ROOMID+"answer", V2TIMMessage.V2TIM_PRIORITY_NORMAL, false, null, new V2TIMSendCallback<V2TIMMessage>() {
                            @Override
                            public void onProgress(int progress) {
                                // 文件上传进度，progress 取值 [0, 100]
                            }
                            @Override
                            public void onSuccess(V2TIMMessage message) {
                                // 文件消息发送成功
                                Toast.makeText(Auditorium_teacher.this,"文件发送成功",Toast.LENGTH_SHORT).show();
                                upload1.dismiss();
                            }

                            @Override
                            public void onError(int code, String desc) {
                                // 文件消息发送失败
                                Toast.makeText(Auditorium_teacher.this,"文件发送成功",Toast.LENGTH_SHORT).show();
                                upload1.dismiss();
                            }
                        });
                    }
                }).show();
            }
        });

        all_silent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i1++;
                V2TIMGroupInfo info = new V2TIMGroupInfo();
                if (i1%2==0){
                    all_silent.setImageResource(R.drawable.ic_baseline_sentiment_very_dissatisfied_24);
                    // 全员禁言
                    info.setGroupID(ROOMID+"chat");
                    info.setAllMuted(true);
                    V2TIMManager.getGroupManager().setGroupInfo(info, new V2TIMCallback() {
                        @Override
                        public void onSuccess() {
                            // 全员禁言成功
                            Log.i("腾讯即时通讯IM","全员禁言成功");
                        }

                        @Override
                        public void onError(int code, String desc) {
                            // 全员禁言失败
                            Log.e("腾讯即时通讯IM","全员禁言失败"+code+","+desc);
                        }
                    });
                }else {
                    all_silent.setImageResource(R.drawable.ic_baseline_sentiment_satisfied_alt_24);
                    info.setGroupID(ROOMID+"chat");
                    info.setAllMuted(false);
                    V2TIMManager.getGroupManager().setGroupInfo(info, new V2TIMCallback() {
                        @Override
                        public void onSuccess() {
                            Log.i("腾讯即时通讯IM","解除全员禁言");
                        }

                        @Override
                        public void onError(int code, String desc) {
                            Log.e("腾讯即时通讯IM","解除禁言失败"+code+","+desc);
                        }
                    });
                }
            }
        });


        micro_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i2++;
                if (i2%2==0){
                    micro_phone.setImageResource(R.drawable.micro_of);
                    OnLocalAudioMuteOff(view);
                }else {
                    micro_phone.setImageResource(R.drawable.micro_on);
                    OnLocalAudioMuteOn(view);
                }


            }
        });

        camera_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i3++;
                if (i3%2==0){
                    camera_close.setImageResource(R.drawable.ic_baseline_desktop_access_disabled_24);
                    fl_screen.removeAllViews();
                    mRtcEngine.leaveChannel();
                }else {
                    camera_close.setImageResource(R.drawable.ic_baseline_desktop_windows_24);
                    // 视频默认禁用，你需要调用 enableVideo 启用视频流。
                    mRtcEngine.enableVideo();
                    // 开启本地视频预览。
                    mRtcEngine.startPreview();

                    FrameLayout container = findViewById(R.id.local_video_view_container);
                    SurfaceView surfaceView = new SurfaceView(getBaseContext());
                    container.addView(surfaceView);
                    // 将 SurfaceView 对象传入 Agora，以渲染本地视频。
                    mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
                    // 使用临时 Token 加入频道。
                    // 你需要自行指定用户 ID，并确保其在频道内的唯一性。
                    mRtcEngine.joinChannel(null, ROOMID, 0, options);
                    mRtcEngine.muteLocalAudioStream(true);

                }
            }
        });

        my.start();

        camera_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRtcEngine.switchCamera();
            }
        });

        bulletSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isBulletShow){
                    //更新为隐藏弹幕
                    bulletsView.hide();
                    isBulletShow=false;
                    bulletSwitch.setImageResource(R.drawable.bullet_false);
                }
                else{
                    //更新为显示弹幕
                    bulletsView.show();
                    isBulletShow=true;
                    bulletSwitch.setImageResource(R.drawable.bullet_true);

                }
            }
        });

        board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classroom room=new classroom();//TODO:下面的弹窗需要传入当前房间classroom，此处新建一个测试用
                dialog_classroomInformationCheck dialog=new dialog_classroomInformationCheck(Auditorium_teacher.this,R.style.dialogOfShowRoom,true,room);
                dialog.setClassroom(classroom);
                dialog.show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mRtcEngine!=null) {
                    mRtcEngine.stopPreview();
                    mRtcEngine.leaveChannel();
                    dismissGroup();
                    playMusic();
                    startActivity(new Intent(Auditorium_teacher.this, Teacher.class));
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                db.deleteLivingroom();
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
            }
        });

        //定时任务
        startReturnQuestion();
        startUpdateQuestion();


        if(first_teacher==false) {
            //新手引导
            NewbieGuide.with(this)
                    .setLabel("guide2")
                    .alwaysShow(true)
                    .addGuidePage(GuidePage.newInstance()
                            .setLayoutRes(R.layout.auditorium_teacher_guide))
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLight(all_silent)
                            .setLayoutRes(R.layout.auditorium_teacher_guide2))
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLight(countTextView)
                            .setLayoutRes(R.layout.auditorium_teacher_guide3))
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLight(countTextView)
                            .setLayoutRes(R.layout.auditorium_teacher_guide4)).show();
            first_teacher=true;
        }


    }

    /**
     * 加入一个房间
     */
    private void joinRoom_chat() {
        Log.i("腾讯云即时通信IM", "你即将加入的房间号为：" + ROOMID+"chat");
        IMManager.joinToLiveRoom(ROOMID+"chat", ROOMNAME, new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {
                Log.i("腾讯云即时通信IM", "加入房间失败。错误码为：" + i + "，错误信息为：" + s);
            }

            @Override
            public void onSuccess() {
                Log.i("腾讯云即时通信IM", "加入房间成功");
            }
        });
    }
    private void joinRoom_question() {
        Log.i("腾讯云即时通信IM", "你即将加入的房间号为：" + ROOMID+"question");
        IMManager.joinToLiveRoom(ROOMID+"question", ROOMNAME, new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {
                Log.i("腾讯云即时通信IM", "加入房间失败。错误码为：" + i + "，错误信息为：" + s);
            }

            @Override
            public void onSuccess() {
                Log.i("腾讯云即时通信IM", "加入房间成功");
            }
        });
    }
    private void joinRoom_answer() {
        Log.i("腾讯云即时通信IM", "你即将加入的房间号为：" + ROOMID+"answer");
        IMManager.joinToLiveRoom(ROOMID+"answer", ROOMNAME, new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {
                Log.i("腾讯云即时通信IM", "加入房间失败。错误码为：" + i + "，错误信息为：" + s);
            }

            @Override
            public void onSuccess() {
                Log.i("腾讯云即时通信IM", "加入房间成功");
            }
        });
    }

    private void sendMessage(String text)
    {
        // 创建文本消息
        V2TIMMessage v2TIMMessage = V2TIMManager.getMessageManager().createTextMessage(text);
        // 发送消息
        V2TIMManager.getMessageManager().sendMessage(v2TIMMessage, null, ROOMID+"chat", V2TIMMessage.V2TIM_PRIORITY_NORMAL, false, null, new V2TIMSendCallback<V2TIMMessage>() {
            @Override
            public void onProgress(int progress) {
                // 文本消息不会回调进度
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(V2TIMMessage message) {
                // 发送群聊文本消息成功
                Log.i("腾讯云即时通信IM","信息发送成功"+text);
            }

            @Override
            public void onError(int code, String desc) {
                // 发送群聊文本消息失败
                Log.e("腾讯云即时通信IM","信息发送失败"+code+","+desc);
            }
        });
    }

    /**
     * 接收消息
     */
    private void getMessage(){
        //接收消息
        IMManager.receiveMsg(new V2TIMAdvancedMsgListener(){
            /**
             * 收到新消息
             * @param msg 消息
             */
            public void onRecvNewMessage(V2TIMMessage msg) {
                // 解析出 groupID 和 userID
                String groupID = msg.getGroupID();
                //String userID = msg.getUserID();
                // 判断当前是单聊还是群聊：
                // 如果 groupID 不为空，表示此消息为群聊；如果 userID 不为空，表示此消息为单聊

                // 解析出 msg 中的文本消息
                if (msg.getElemType() == V2TIMMessage.V2TIM_ELEM_TYPE_TEXT) {
                    V2TIMTextElem textElem = msg.getTextElem();
                    String text = textElem.getText();
                    state=2;

                    talks.add(Auditorium_student.position,new talkModel(text));
                    recycleViewAdapter.notifyItemInserted(Auditorium_student.position);
                    recyclerView.scrollToPosition(Auditorium_student.position);
                    Auditorium_student.position++;

                    if (Objects.equals(groupID, ROOMID + "question")) {
                        //Log.i("腾讯即时通讯IM", "text+question:" + text);
                        //提问中的操作
                        addDanmaKuShowTextAndImage(true,text,QUESTION);
                    }
                    else if(Objects.equals(groupID, ROOMID + "answer")){
                        //Log.i("腾讯即时通讯IM", "text+answer:" + text);
                        //回答中的操作

                    }
                    else{
                        //水群中的操作

                    }
                }
                // 解析出 msg 中的自定义消息
                if (msg.getElemType() == V2TIMMessage.V2TIM_ELEM_TYPE_CUSTOM) {
                    V2TIMCustomElem customElem = msg.getCustomElem();
                    String data = new String(customElem.getData());
                    Log.i("腾讯即时通讯IM", "点赞customData:" + data);
                    likes++;
                    likenum.setText("点赞数:"+likes);

                }



            }

        });
    }

    private void dismissGroup(){
        V2TIMManager.getInstance().dismissGroup(ROOMID+"answer", new V2TIMCallback() {
            @Override
            public void onSuccess() {
                // 解散群组成功
                Log.i("腾讯即时通讯IM","解散群组成功");

            }

            @Override
            public void onError(int code, String desc) {
                // 解散群组失败
                Log.i("腾讯即时通讯IM","解散群组失败");
            }
        });
        V2TIMManager.getInstance().dismissGroup(ROOMID+"question", new V2TIMCallback() {
            @Override
            public void onSuccess() {
                // 解散群组成功
                Log.i("腾讯即时通讯IM","解散群组成功");

            }

            @Override
            public void onError(int code, String desc) {
                // 解散群组失败
                Log.i("腾讯即时通讯IM","解散群组失败");
            }
        });
        V2TIMManager.getInstance().dismissGroup(ROOMID+"chat", new V2TIMCallback() {
            @Override
            public void onSuccess() {
                // 解散群组成功
                Log.i("腾讯即时通讯IM","解散群组成功");
                Toast.makeText(Auditorium_teacher.this,"课堂已解散",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Auditorium_teacher.this,Teacher.class));
            }

            @Override
            public void onError(int code, String desc) {
                // 解散群组失败
                Log.i("腾讯即时通讯IM","解散群组失败");
            }
        });

    }


    public void judge()/*当数量达到预定的阈值时响铃*/
    {  if(countTextView.getNUM()>=objective) {
        mediaUtil.playRing(getApplicationContext());
        try {
            Thread.sleep(1500);
            mediaUtil.stopRing();
            my.onPause();


        } catch (InterruptedException e) {
            e.printStackTrace();
        }} }

    private void addDanmaKuShowImage(boolean islive) {
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_search_24);
        drawable.setBounds(0, 0, 100, 100);
        SpannableStringBuilder spannable = createSpannable(drawable);
        danmaku.text = spannable;
        danmaku.padding = 5;
        danmaku.priority = 1;  // 一定会显示, 一般用于本机发送的弹幕
        danmaku.isLive = islive;
        danmaku.setTime(bulletsView.getCurrentTime() + 1200);
        danmaku.textSize = 25f * (mParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = Color.RED;
        danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
        danmaku.underlineColor = Color.GREEN;
        bulletsView.addDanmaku(danmaku);
    }

    private void addDanmaKuShowTextAndImage(boolean islive,String text,int type) {
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        Drawable drawable = null;
        switch (type){
            case 0://提问类弹幕
                drawable = getResources().getDrawable(R.drawable.ic_baseline_search_24);
                break;
            case 1://回答类弹幕
                drawable = getResources().getDrawable(R.drawable.ic_baseline_priority_high_24);
                break;
            default://其他类弹幕
                drawable = getResources().getDrawable(R.drawable.ic_baseline_message_24);
                break;
        }
        drawable.setBounds(0, 0, 100, 100);
        SpannableStringBuilder spannable = text.isEmpty()?createSpannable(drawable):createSpannable(drawable,text);
        danmaku.text = spannable;
        danmaku.padding = 5;
        danmaku.priority = 1;  // 一定会显示, 一般用于本机发送的弹幕
        danmaku.isLive = islive;
        danmaku.setTime(bulletsView.getCurrentTime() + 1200);
        danmaku.textSize = 25f * (mParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = Color.RED;
        danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
        danmaku.underlineColor = Color.GREEN;
        bulletsView.addDanmaku(danmaku);
    }

    private SpannableStringBuilder createSpannable(Drawable drawable) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("图片");
        ImageSpan span = new ImageSpan(drawable);//ImageSpan.ALIGN_BOTTOM);
        spannableStringBuilder.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new BackgroundColorSpan(Color.parseColor("#8A2233B1")), 0, spannableStringBuilder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }

    private SpannableStringBuilder createSpannable(Drawable drawable,String text) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        ImageSpan span = new ImageSpan(drawable);//ImageSpan.ALIGN_BOTTOM);
        spannableStringBuilder.setSpan(span, 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(text);
        spannableStringBuilder.setSpan(new BackgroundColorSpan(Color.parseColor("#8A2233B1")), 0, spannableStringBuilder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }

    private void setBulletsView() {
        // VideoView
        //VideoView mVideoView = (VideoView) findViewById(R.id.videoview);

        // DanmakuView
        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示5行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

        bulletsView = (IDanmakuView) findViewById(R.id.BulletsView);
        mContext = DanmakuContext.create();
        mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3).
                setDuplicateMergingEnabled(false).//是否重复合并
                setScrollSpeedFactor(1.2f).
                setScaleTextSize(1.2f).//设置文字的比例
                setCacheStuffer(new SpannedCacheStuffer(), mCacheStufferAdapter).// 图文混排使用SpannedCacheStuffer
                //.setCacheStuffer(new BackgroundCacheStuffer())  // 绘制背景使用BackgroundCacheStuffer
                        setMaximumLines(maxLinesPair).
                preventOverlapping(overlappingEnablePair).setDanmakuMargin(40);

        if (bulletsView != null) {
            mParser= getDefaultDanmakuParser();
            //mParser = (BiliDanmukuParser) createParser((this.getResources().openRawResource(R.raw.comments)));

            bulletsView.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
                @Override
                public void updateTimer(DanmakuTimer timer) {
                }

                @Override
                public void drawingFinished() {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {
//                    Log.d("DFM", "danmakuShown(): text=" + danmaku.text);
                }

                @Override
                public void prepared() {
                    bulletsView.start();
                }
            });

            bulletsView.setOnDanmakuClickListener(new IDanmakuView.OnDanmakuClickListener() {
                @Override
                public boolean onDanmakuClick(IDanmakus danmakus) {
                    //弹幕的点击事件
                    Toast.makeText(Auditorium_teacher.this,"点击", Toast.LENGTH_SHORT).show();
                    return false;
                }

                @Override
                public boolean onDanmakuLongClick(IDanmakus danmakus) {
                    //弹幕的长按事件
                    dialog_answer dialog_answer=new dialog_answer(Auditorium_teacher.this,R.style.dialogOfShowRoom);
                    dialog_answer.setClose(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog_answer.clear();
                        }
                    }).setButton(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(dialog_answer.getAnswer().isEmpty())
                                Toast.makeText(Auditorium_teacher.this, "回答不能为空", Toast.LENGTH_SHORT).show();
                            else {
                                addDanmaKuShowTextAndImage(true, dialog_answer.getAnswer(),ANSWER);
                                dialog_answer.dismiss();
                            }
                        }
                    }).show();
                    return false;
                }

                @Override
                public boolean onViewClick(IDanmakuView view) {
                    //mMediaController.setVisibility(View.VISIBLE);//隐藏
                    return false;
                }
            });

            bulletsView.prepare(mParser, mContext);
            bulletsView.showFPS(false);
            bulletsView.enableDanmakuDrawingCache(true);
        }

        /**
         if (mVideoView != null) {
         mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        }
        });
         mVideoView.setVideoPath(Environment.getExternalStorageDirectory() + "/1.flv");
         }
         **/

    }

    private BaseDanmakuParser createParser(InputStream stream) {

        if (stream == null) {
            return new BaseDanmakuParser() {
                @Override
                protected Danmakus parse() {
                    return new Danmakus();
                }
            };
        }

        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);

        try {
            loader.load(stream);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        BaseDanmakuParser parser = new BiliDanmukuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;

    }

    public void onStop() {
        super.onStop();
    }
}