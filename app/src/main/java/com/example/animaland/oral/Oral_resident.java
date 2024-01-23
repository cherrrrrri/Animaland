package com.example.animaland.oral;

import static com.example.animaland.tool.BasicActivity.pauseMusic;
import static com.example.animaland.tool.BasicActivity.playMusic;
import static com.example.animaland.tool.ThreadHelper.cachedThreadPool;
import static io.agora.rtc2.video.VideoCanvas.RENDER_MODE_HIDDEN;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.animaland.R;
import com.example.animaland.tool.DatabaseHelper;
import com.example.animaland.tool.User;

import java.sql.SQLException;

import io.agora.rtc2.ChannelMediaOptions;
import io.agora.rtc2.Constants;
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.RtcEngineConfig;
import io.agora.rtc2.video.VideoCanvas;

public class Oral_resident extends AppCompatActivity {
    private float mLastTx = 0; // 手指的上一个位置
    private float mLastTy = 0;
    private ImageView topic;
    private ImageView back;
    private FrameLayout remoteVideoContainer;

    // 填写项目的 App ID，可在 Agora 控制台中生成。
    private String appId = "904f83356dbb4ca98da854449825bac6";
    // 填写频道id。
    private String channelName;
    private RtcEngine mRtcEngine;
    private dialog_enforcedExit enforcedExit;
    private DatabaseHelper db = new DatabaseHelper();
    private User user = new User();

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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRtcEngine.setupRemoteVideo(new VideoCanvas(null, RENDER_MODE_HIDDEN, uid));
                    remoteVideoContainer.removeAllViews();
                    showExit();
                }
            });
        }
    };

    private void setupRemoteVideo(int uid) {
        FrameLayout container = findViewById(R.id.remote_video_view_container);
        SurfaceView surfaceView = new SurfaceView(getBaseContext());
        surfaceView.setZOrderMediaOverlay(true);
        container.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
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

        // 视频默认禁用，你需要调用 enableVideo 启用视频流。
        mRtcEngine.enableVideo();
        // 开启本地视频预览。
        mRtcEngine.startPreview();

        FrameLayout container = findViewById(R.id.local_video_view_container);
        // 创建一个 SurfaceView 对象，并将其作为 FrameLayout 的子对象。
        SurfaceView surfaceView = new SurfaceView(getBaseContext());
        container.addView(surfaceView);
        // 将 SurfaceView 对象传入 Agora，以渲染本地视频。
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0));

        ChannelMediaOptions options = new ChannelMediaOptions();
        // 视频通话场景下，设置频道场景为 BROADCASTING。
        options.channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING;
        // 将用户角色设置为 BROADCASTER。
        options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER;

        // 使用临时 Token 加入频道。
        // 你需要自行指定用户 ID，并确保其在频道内的唯一性。
        mRtcEngine.joinChannel(null, channelName, 0, options);
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oral_resident);
        pauseMusic();

        channelName=user.roomId;

        topic = findViewById(R.id.topic);
        back = findViewById(R.id.back);
        remoteVideoContainer = findViewById(R.id.remote_video_view_container);
        enforcedExit= new dialog_enforcedExit(Oral_resident.this, R.style.dialogOfShowRoom);

        //如果已经授权，则初始化 RtcEngine 并加入频道。
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID)) {
            initializeAndJoinChannel();
        }

        final dialog_topic topic_dialog = new dialog_topic(Oral_resident.this, R.style.dialogOfShowRoom);
        final dialog_roomexit exit_dialog1 = new dialog_roomexit(Oral_resident.this, R.style.dialogOfShowRoom);


        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit_dialog1.setYes(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mRtcEngine.stopPreview();
                        mRtcEngine.leaveChannel();
                        playMusic();
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    db.leaveOralRoom();
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

                        startActivity(new Intent(Oral_resident.this, Oral.class));
                    }
                }).setNo(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        exit_dialog1.dismiss();

                    }
                }).show();
            }
        });

        //主题刷新
        topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topic_dialog.setRefresh(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).show();
            }
        });

        remoteVideoContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d("TAG", "down " + motionEvent);
                        mLastTx = motionEvent.getRawX();
                        mLastTy = motionEvent.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        Log.d("TAG", "move " + motionEvent);
                        float dx = motionEvent.getRawX() - mLastTx;
                        float dy = motionEvent.getRawY() - mLastTy;
                        mLastTx = motionEvent.getRawX();
                        mLastTy = motionEvent.getRawY();
                        Log.d("TAG", "  dx: " + dx + ", dy: " + dy);
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        remoteVideoContainer.layout((int) (remoteVideoContainer.getLeft() + dx), (int) (remoteVideoContainer.getTop() + dy), (int) (remoteVideoContainer.getRight() + dx), (int) (remoteVideoContainer.getBottom() + dy));
                        break;

                    case MotionEvent.ACTION_UP:
                        Log.d("TAG", "up " + motionEvent);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        Log.d("TAG", "cancel " + motionEvent);
                        return true;
                }
                return false;
            }
        });


    }
    private void showExit(){
        enforcedExit.setExit(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Oral_resident.this,Oral.class));
            }
        }).show();
    }

    public void onStop() {
        super.onStop();
    }

}
