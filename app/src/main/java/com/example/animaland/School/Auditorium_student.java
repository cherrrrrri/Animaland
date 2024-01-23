package com.example.animaland.School;


import static com.example.animaland.tool.BasicActivity.pauseMusic;
import static com.example.animaland.tool.BasicActivity.playMusic;
import static com.example.animaland.tool.ThreadHelper.cachedThreadPool;
import static io.agora.rtc2.Constants.RENDER_MODE_HIDDEN;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
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
import com.tencent.imsdk.v2.V2TIMDownloadCallback;
import com.tencent.imsdk.v2.V2TIMElem;
import com.tencent.imsdk.v2.V2TIMFileElem;
import com.tencent.imsdk.v2.V2TIMGroupListener;
import com.tencent.imsdk.v2.V2TIMGroupMemberInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMSendCallback;
import com.tencent.imsdk.v2.V2TIMTextElem;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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

public class Auditorium_student<arrayAdapter> extends AppCompatActivity {

    private ImageView like;
   // private ImageView shot;
    private ImageView question;
    private ImageView comment;
    private ImageView bulletSwitch;
    private ImageView board;
    private ImageView back;
    private ImageView download;
    private EditText question_hint;
    private DrawerLayout drawerLayout;
    private Spinner spinner;
    private FrameLayout fl_screen;
    private int commentsType=QUESTION;//记录用户在评论区选择的留言类型，默认为提问类
    //弹幕相关
    public static int QUESTION=0;
    public static int ANSWER=1;
    public static int OTHER=2;
    private boolean isBulletShow=true;//记录弹幕开关状态，默认为开
    private IDanmakuView bulletsView;
    private BaseDanmakuParser mParser;
    private DanmakuContext mContext;

    private User user=new User();

    //视频相关
    // 填写项目的 App ID，可在 Agora 控制台中生成。
    private String appId = "904f83356dbb4ca98da854449825bac6";
    // 填写频道名称。
    private String ROOMID;
    private String ROOMNAME;
    private RtcEngine mRtcEngine;

    private RecyclerView recyclerView;
    private static int pos;
    private static int temp;
    private String Mystring;
    public static int position=0;
    private int num=0;
    public static int click_num=0;
    private TextView click;
    private FloatLikeView likeView;
    private dialog_download download1;
    private DatabaseHelper db=new DatabaseHelper();
    private  classroom classroom;
    private static boolean first_student=false;
    private TextView textView;

    ArrayList<talkModel> talks =new ArrayList<>();/*聊天记录就传入这个数组*/
    RecycleViewAdapter recycleViewAdapter=new RecycleViewAdapter(Auditorium_student.this,talks);
    public static int getPos()
    {return pos;}



    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        // 监听频道内的远端主播，获取主播的 uid 信息。
        public void onUserJoined(int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 从 onUserJoined 回调获取 uid 后，调用 setupRemoteVideo，设置远端视频视图。
                    setupRemoteVideo(uid);
                }
            });
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            super.onUserOffline(uid, reason);
            /**Clear render view
             Note: The video will stay at its last frame, to completely remove it you will need to
             remove the SurfaceView from its parent*/
            mRtcEngine.setupRemoteVideo(new VideoCanvas(null, RENDER_MODE_HIDDEN, uid));
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

        mRtcEngine.enableAudio();
        ChannelMediaOptions options = new ChannelMediaOptions();
        // 直播场景下，设置频道场景为 BROADCASTING。
        options.channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING;
        // 根据场景将用户角色设置为 BROADCASTER 或 AUDIENCE。
        options.clientRoleType = Constants.CLIENT_ROLE_AUDIENCE;
        // 极速直播下的观众，需设置用户级别为 AUDIENCE_LATENCY_LEVEL_LOW_LATENCY。
        options.audienceLatencyLevel = Constants.AUDIENCE_LATENCY_LEVEL_LOW_LATENCY;

        // 使用临时 Token 加入频道。
        // 你需要自行指定用户 ID，并确保其在频道内的唯一性。
        mRtcEngine.joinChannel(null, ROOMID, 0, options);
    }

    private void setupRemoteVideo(int uid) {
        FrameLayout container = findViewById(R.id.local_video_view_container);
        SurfaceView surfaceView = new SurfaceView (getBaseContext());
        surfaceView.setZOrderMediaOverlay(true);
        container.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.auditorium_student);
        pauseMusic();

        ROOMID = user.roomId;
        ROOMNAME=user.roomName;
        Auditorium_teacher.isRunning=0;
        //获取MainActivity中LayoutInflater （上下文参数）
        LayoutInflater factorys = LayoutInflater.from(Auditorium_student.this);
        //获取View 对象
        View view= factorys.inflate(R.layout.item4, null);
        //获取控件
        textView =  view.findViewById(R.id.num);


        like=findViewById(R.id.like);
        question=findViewById(R.id.question);
        comment=findViewById(R.id.comment);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);/*重点，获取主界面的布局，因为没有这句话我才报错*/
        spinner=findViewById(R.id.spinner);
        question_hint=findViewById(R.id.question_hint);
        bulletsView=findViewById(R.id.BulletsView);
        fl_screen=findViewById(R.id.local_video_view_container);
        recyclerView=findViewById(R.id.talklayout);
        board=findViewById(R.id.board);
        back=findViewById(R.id.back);
        bulletSwitch=findViewById(R.id.bullet);
        likeView=findViewById(R.id.like_anim);
        click=findViewById(R.id.click_num);
        download=findViewById(R.id.download);


        download1=new dialog_download(Auditorium_student.this,R.style.dialogOfShowRoom);


        // 如果已经授权，则初始化 RtcEngine 并加入频道。
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID)) {
            initializeAndJoinChannel();
        }

        // 添加图片
        likeView.setLikeDrawables(R.drawable.heart_01, R.drawable.heart_02, R.drawable.heart_03
                , R.drawable.heart_04, R.drawable.heart_05, R.drawable.heart_06);


        setBulletsView();//设置弹幕显示参数


        joinRoom_chat();
        joinRoom_answer();
        joinRoom_question();
        getMessage();

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


        ArrayAdapter arrayAdapter= new ArrayAdapter<String>(this,R.layout.custom_spinner_item, getResources().getStringArray(R.array.Data3));
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_stytle);
        recyclerView.setAdapter(recycleViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Auditorium_student.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(0,true);
        /*在下面这个函数监听选了哪项*/
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelection(position,true);
                pos=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        question_hint.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND) {
                    Mystring= String.valueOf(question_hint.getText());

                    if(pos==0){
                        sendMessage_chat(Mystring);
                    }
                    else if(pos==1){
                        sendMessage_question(Mystring);
                        addDanmaKuShowTextAndImage(true,Mystring,QUESTION);
                    }
                    else{
                        sendMessage_answer(Mystring);
                    }

                    talks.add(position,new talkModel(Mystring));
                    recycleViewAdapter.notifyItemInserted(position);
                    recyclerView.scrollToPosition(position);
                    position++;

                }
                return false;
            }
        });



        final  dialog_question questions=new dialog_question(Auditorium_student.this,R.style.dialogOfShowRoom);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 单次点击 出一个爱心
                likeView.clickLikeView();
                click_num++;
                click.setText(String.valueOf(click_num));
                String msgID = V2TIMManager.getInstance().sendGroupCustomMessage("likes".getBytes(), ROOMID+"question", V2TIMMessage.V2TIM_PRIORITY_NORMAL, new V2TIMValueCallback<V2TIMMessage>() {
                    @Override
                    public void onSuccess(V2TIMMessage message) {
                        // 发送群聊自定义消息成功
                        Log.i("腾讯云即时通信IM","发送点赞成功");
                        // 单次点击 出一个爱心
                        likeView.clickLikeView();
                        click_num++;
                        click.setText(String.valueOf(click_num));
                    }

                    @Override
                    public void onError(int code, String desc) {
                        // 发送群聊自定义消息失败
                        Log.e("腾讯云即时通信IM",code+","+desc);
                    }
                });
            }
        });

       /* shot.setOnClickListener(new View.OnClickListener() {//截图
            @Override
            public void onClick(View view) {
              //  Bitmap bitmap = Screenshot.shotRecyclerView(bulletsView);
              //  Screenshot.saveBitmapToSdCard(Auditorium_student.this, bitmap, "测试截图");
            }
        });*/

        //提问短按
        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDanmaKuShowImage(true);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            db.addQuestion();
                            Log.i("问题数量监测","问题+1");
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

        //提问长按
        question.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                questions.setClose(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        questions.clear();
                    }
                }).setButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addDanmaKuShowTextAndImage(true, questions.getQuestion(),QUESTION);
                        sendMessage_question(questions.getQuestion());
                        questions.dismiss();
                        //Toast.makeText(Auditorium_student.this,"send",Toast.LENGTH_SHORT).show();
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
                    bulletSwitch.setImageResource(R.drawable.save);
                }
            }
        });

        board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classroom room=new classroom();//TODO:下面的弹窗需要传入当前房间classroom，此处新建一个测试用
                dialog_classroomInformationCheck dialog=new dialog_classroomInformationCheck(Auditorium_student.this,R.style.dialogOfShowRoom,true,room);
                dialog.setClassroom(classroom);
                dialog.show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {mRtcEngine.leaveChannel();
                leaveGroup();
                playMusic();
                startActivity(new Intent(Auditorium_student.this,Student.class));
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            db.leaveLivingroom();
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

        V2TIMManager.getInstance().addGroupListener(new V2TIMGroupListener() {
            @Override
            public void onGroupDismissed(String groupID, V2TIMGroupMemberInfo opUser) {
                // 群被解散回调
                mRtcEngine.leaveChannel();
                Toast.makeText(Auditorium_student.this,"课程已结束",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Auditorium_student.this,Student.class));
            }

            @Override
            public void onGroupRecycled(String groupID, V2TIMGroupMemberInfo opUser) {
                // 群被回收回调
            }
        });

       if(first_student==false) {
           NewbieGuide.with(this)
                   .setLabel("guide1")
                   .alwaysShow(true)
                   .addGuidePage(GuidePage.newInstance()
                           .setLayoutRes(R.layout.auditorium_student_guide))
                   .addGuidePage(GuidePage.newInstance()
                           .addHighLight(like)
                           .setLayoutRes(R.layout.auditorium_student_guide2))
                   .addGuidePage(GuidePage.newInstance()
                           .setLayoutRes(R.layout.auditorium_student_guide3))
                   .addGuidePage(GuidePage.newInstance()
                           .addHighLight(question)
                           .setLayoutRes(R.layout.auditorium_guide_student4))
                   .addGuidePage(GuidePage.newInstance()
                           .addHighLight(comment)
                           .setLayoutRes(R.layout.auditorium_student_guide5))
                   .addGuidePage(GuidePage.newInstance()
                           .addHighLight(board)
                           .setLayoutRes(R.layout.auditorium_student_guide6))
                   .show();
          first_student=true;
       }

    }



    public void onClick(View view) {
        switch (view.getId()){
            case R.id.num:
                num++;
                Toast.makeText(Auditorium_student.this,"疑问增加",Toast.LENGTH_SHORT).show();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }}


    /**
     * 加入一个房间
     */
    private void joinRoom_chat() {
        Log.i("腾讯云即时通信IM", "你即将加入的房间号为：" + ROOMID+"chat");
        IMManager.joinToLiveRoom(ROOMID+"chat", ROOMNAME, new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {
                Log.i("腾讯云即时通信IM", "加入房间失败。错误码为：" + i + "，错误信息为：" + s);
                Toast.makeText(Auditorium_student.this,"加入房间失败",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Auditorium_student.this,Student.class));
            }
            @Override
            public void onSuccess() {
                Log.i("腾讯云即时通信IM", "加入房间成功");
            }
        });
    }

    private void joinRoom_question(){
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
                    if (Objects.equals(groupID, ROOMID + "question")) {
                        addDanmaKuShowTextAndImage(true,text,QUESTION);
                        Log.i("腾讯即时通讯IM", "text+question:" + text);
                        pos=3;
                        talks.add(position,new talkModel(text));
                        recycleViewAdapter.notifyItemInserted(position);
                        recyclerView.scrollToPosition(position);
                        position++;


                    }
                    else if(Objects.equals(groupID, ROOMID + "answer")){
                        Log.i("腾讯即时通讯IM", "text+answer:" + text);
                        pos=4;
                        talks.add(position,new talkModel(text));
                        recycleViewAdapter.notifyItemInserted(position);
                        recyclerView.scrollToPosition(position);
                        position++;
                    }
                    else if(Objects.equals(groupID, ROOMID + "chat")){
                        Log.i("腾讯即时通讯IM", "text+chat:" + text);
                        pos=5;
                        talks.add(position,new talkModel(text));
                        recycleViewAdapter.notifyItemInserted(position);
                        recyclerView.scrollToPosition(position);
                        position++;
                    }
                }
                // 解析出 msg 中的自定义消息
                if (msg.getElemType() == V2TIMMessage.V2TIM_ELEM_TYPE_CUSTOM) {
                    V2TIMCustomElem customElem = msg.getCustomElem();
                    String data = new String(customElem.getData());
                    Log.i("腾讯即时通讯IM", "点赞customData:" + data);

                }

                if (msg.getElemType() == V2TIMMessage.V2TIM_ELEM_TYPE_FILE) {
                    // 文件消息
                    V2TIMFileElem v2TIMFileElem = msg.getFileElem();
                    // 文件 ID,内部标识，可用于外部缓存 key
                    String uuid = v2TIMFileElem.getUUID();
                    // 文件名称
                    String fileName = v2TIMFileElem.getFileName();
                    // 文件大小
                    int fileSize = v2TIMFileElem.getFileSize();
                    // 设置文件路径，这里可以用 uuid 作为标识，避免重复下载
                    String filePath = "/sdcard/im/file/" + "myUserID" + uuid;
                    File file = new File(filePath);
                    download1.setYes(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            download1.setHint(fileName);
                            if (!file.exists()) {
                                v2TIMFileElem.downloadFile(filePath, new V2TIMDownloadCallback() {
                                    @Override
                                    public void onProgress(V2TIMElem.V2ProgressInfo progressInfo) {
                                        // 下载进度回调：已下载大小 v2ProgressInfo.getCurrentSize()；总文件大小 v2ProgressInfo.getTotalSize()
                                    }
                                    @Override
                                    public void onError(int code, String desc) {
                                        // 下载失败
                                        Toast.makeText(Auditorium_student.this,"failed to download the file, "+desc,Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onSuccess() {
                                        // 下载完成
                                        Toast.makeText(Auditorium_student.this,"文件下载完成",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                // 文件已存在
                                Toast.makeText(Auditorium_student.this,"文件已存在！",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).show();

                }

            }

        });
    }
    private void sendMessage_question(String text){
        // 创建文本消息
        V2TIMMessage v2TIMMessage = V2TIMManager.getMessageManager().createTextMessage(text);
        // 发送消息
        V2TIMManager.getMessageManager().sendMessage(v2TIMMessage, null, ROOMID+"question", V2TIMMessage.V2TIM_PRIORITY_NORMAL, false, null, new V2TIMSendCallback<V2TIMMessage>() {
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

    private void sendMessage_answer(String text){
        // 创建文本消息
        V2TIMMessage v2TIMMessage = V2TIMManager.getMessageManager().createTextMessage(text);
        // 发送消息
        V2TIMManager.getMessageManager().sendMessage(v2TIMMessage, null, ROOMID+"answer", V2TIMMessage.V2TIM_PRIORITY_NORMAL, false, null, new V2TIMSendCallback<V2TIMMessage>() {
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

    private void sendMessage_chat(String text){
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



    private void leaveGroup(){
        V2TIMManager.getInstance().quitGroup(ROOMID+"question", new V2TIMCallback() {
            @Override
            public void onSuccess() {
                // 退出群组成功
                Log.i("腾讯云即时通信IM","退出question成功");
            }

            @Override
            public void onError(int code, String desc) {
                // 退出群组失败
                Log.e("腾讯云即时通信IM","退出question失败"+code+","+desc);
            }
        });
        V2TIMManager.getInstance().quitGroup(ROOMID+"answer", new V2TIMCallback() {
            @Override
            public void onSuccess() {
                // 退出群组成功
                Log.i("腾讯云即时通信IM","退出answer成功");
            }

            @Override
            public void onError(int code, String desc) {
                // 退出群组失败
                Log.e("腾讯云即时通信IM","退出answer失败"+code+","+desc);
            }
        });
        V2TIMManager.getInstance().quitGroup(ROOMID+"chat", new V2TIMCallback() {
            @Override
            public void onSuccess() {
                // 退出群组成功
                Log.i("腾讯云即时通信IM","退出answer成功");
                startActivity(new Intent(Auditorium_student.this,Student.class));
            }

            @Override
            public void onError(int code, String desc) {
                // 退出群组失败
                Log.e("腾讯云即时通信IM","退出chat失败"+code+","+desc);
            }
        });
    }



    @Override
    protected void onPause() {
        super.onPause();
        if (bulletsView != null && bulletsView.isPrepared()) {
            bulletsView.pause();
        }
        System.gc();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bulletsView != null && bulletsView.isPrepared() && bulletsView.isPaused()) {
            bulletsView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bulletsView != null) {
            // dont forget release!
            bulletsView.release();
            bulletsView = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (bulletsView != null) {
            // dont forget release!
            bulletsView.release();
            bulletsView = null;
        }
    }

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
                drawable = getResources().getDrawable(R.drawable.ic_baseline_priority_high_24);
                break;
            case 1://回答类弹幕
                drawable = getResources().getDrawable(R.drawable.ic_baseline_message_24);
                break;
            default://其他类弹幕
                drawable = getResources().getDrawable(R.drawable.ic_baseline_sentiment_satisfied_alt_24);
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
            mParser= BiliDanmukuParser.getDefaultDanmakuParser();
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
                    Toast.makeText(Auditorium_student.this,"点击", Toast.LENGTH_SHORT).show();
                    return false;
                }

                @Override
                public boolean onDanmakuLongClick(IDanmakus danmakus) {
                    //弹幕的长按事件 长按出现输入回答的弹窗
                    dialog_answer dialog_answer=new dialog_answer(Auditorium_student.this,R.style.dialogOfShowRoom);
                    dialog_answer.setClose(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog_answer.clear();
                        }
                    }).setButton(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(dialog_answer.getAnswer().isEmpty())
                                Toast.makeText(Auditorium_student.this, "回答不能为空", Toast.LENGTH_SHORT).show();
                            else {
                                addDanmaKuShowTextAndImage(true, dialog_answer.getAnswer(),ANSWER);
                                dialog_answer.dismiss();
                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            db.addQuestion();
                                            Log.i("问题数量监测","问题+1");
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

    @Override
    protected void onStop() {
        super.onStop();
    }


}