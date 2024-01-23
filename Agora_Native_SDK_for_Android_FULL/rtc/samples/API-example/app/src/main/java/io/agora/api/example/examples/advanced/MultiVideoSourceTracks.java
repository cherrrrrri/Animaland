package io.agora.api.example.examples.advanced;

import static io.agora.api.example.common.model.Examples.ADVANCED;
import static io.agora.rtc2.video.VideoCanvas.RENDER_MODE_FIT;
import static io.agora.rtc2.video.VideoEncoderConfiguration.STANDARD_BITRATE;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.agora.api.example.MainApplication;
import io.agora.api.example.R;
import io.agora.api.example.annotation.Example;
import io.agora.api.example.common.BaseFragment;
import io.agora.api.example.common.widget.VideoReportLayout;
import io.agora.api.example.utils.CommonUtil;
import io.agora.api.example.utils.TokenUtils;
import io.agora.api.example.utils.VideoFileReader;
import io.agora.rtc2.ChannelMediaOptions;
import io.agora.rtc2.Constants;
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcConnection;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.RtcEngineConfig;
import io.agora.rtc2.RtcEngineEx;
import io.agora.rtc2.video.VideoCanvas;
import io.agora.rtc2.video.VideoEncoderConfiguration;

@Example(
        index = 10,
        group = ADVANCED,
        name = R.string.item_multiVideoSourceTracks,
        actionId = R.id.action_mainFragment_to_MultiVideoSourceTracks,
        tipsId = R.string.multivideosourcetracks
)
public class MultiVideoSourceTracks extends BaseFragment implements View.OnClickListener {
    private static final String TAG = MultiVideoSourceTracks.class.getSimpleName();


    private VideoReportLayout[] flVideoContainer;
    private Button join;
    private EditText et_channel;
    private RtcEngineEx engine;
    private int myUid;
    private volatile boolean joined = false;

    private final List<Integer> videoTrackIds = new ArrayList<>();
    private final List<VideoFileReader> videoFileReaders = new ArrayList<>();
    private final List<RtcConnection> connections = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multi_video_source_tracks, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        join = view.findViewById(R.id.btn_join);
        et_channel = view.findViewById(R.id.et_channel);
        view.findViewById(R.id.btn_join).setOnClickListener(this);
        flVideoContainer = new VideoReportLayout[]{
                view.findViewById(R.id.fl_video_container_01),
                view.findViewById(R.id.fl_video_container_02),
                view.findViewById(R.id.fl_video_container_03),
                view.findViewById(R.id.fl_video_container_04),
        };
        view.findViewById(R.id.btn_track_create).setOnClickListener(v -> createPushingVideoTrack());
        view.findViewById(R.id.btn_track_destroy).setOnClickListener(v -> destroyLastPushingVideoTrack());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Check if the context is valid
        Context context = getContext();
        if (context == null) {
            return;
        }
        try {
            RtcEngineConfig config = new RtcEngineConfig();
            /**
             * The context of Android Activity
             */
            config.mContext = context.getApplicationContext();
            /**
             * The App ID issued to you by Agora. See <a href="https://docs.agora.io/en/Agora%20Platform/token#get-an-app-id"> How to get the App ID</a>
             */
            config.mAppId = getString(R.string.agora_app_id);
            /** Sets the channel profile of the Agora RtcEngine.
             CHANNEL_PROFILE_COMMUNICATION(0): (Default) The Communication profile.
             Use this profile in one-on-one calls or group calls, where all users can talk freely.
             CHANNEL_PROFILE_LIVE_BROADCASTING(1): The Live-Broadcast profile. Users in a live-broadcast
             channel have a role as either broadcaster or audience. A broadcaster can both send and receive streams;
             an audience can only receive streams.*/
            config.mChannelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING;
            /**
             * IRtcEngineEventHandler is an abstract class providing default implementation.
             * The SDK uses this class to report to the app on SDK runtime events.
             */
            config.mEventHandler = iRtcEngineEventHandler;
            config.mAudioScenario = Constants.AudioScenario.getValue(Constants.AudioScenario.DEFAULT);
            config.mAreaCode = ((MainApplication) getActivity().getApplication()).getGlobalSettings().getAreaCode();
            engine = (RtcEngineEx) RtcEngine.create(config);
            /**
             * This parameter is for reporting the usages of APIExample to agora background.
             * Generally, it is not necessary for you to set this parameter.
             */
            engine.setParameters("{"
                    + "\"rtc.report_app_scenario\":"
                    + "{"
                    + "\"appScenario\":" + 100 + ","
                    + "\"serviceType\":" + 11 + ","
                    + "\"appVersion\":\"" + RtcEngine.getSdkVersion() + "\""
                    + "}"
                    + "}");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        /**leaveChannel and Destroy the RtcEngine instance*/
        if (engine != null) {
            destroyAllPushingVideoTrack();
            engine.leaveChannel();
            engine.stopPreview();
        }
        engine = null;
        super.onDestroy();
        handler.post(RtcEngine::destroy);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_join) {
            if (!joined) {
                CommonUtil.hideInputBoard(getActivity(), et_channel);
                // call when join button hit
                String channelId = et_channel.getText().toString();
                // Check permission
                if (AndPermission.hasPermissions(this, Permission.Group.STORAGE, Permission.Group.MICROPHONE, Permission.Group.CAMERA)) {
                    joinChannel(channelId);
                    return;
                }
                // Request permission
                AndPermission.with(this).runtime().permission(
                        Permission.Group.STORAGE,
                        Permission.Group.MICROPHONE,
                        Permission.Group.CAMERA
                ).onGranted(permissions ->
                {
                    // Permissions Granted
                    joinChannel(channelId);
                }).start();
            } else {
                joined = false;
                join.setText(getString(R.string.join));
                resetAllVideoLayout();
                engine.leaveChannel();
                engine.stopPreview();
                myUid = 0;
            }
        }
    }

    private void joinChannel(String channelId) {
        // Check if the context is valid
        Context context = getContext();
        if (context == null) {
            return;
        }

        /**Set up to play remote sound with receiver*/
        engine.setDefaultAudioRoutetoSpeakerphone(true);

        /**In the demo, the default is to enter as the anchor.*/
        engine.setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
        // Enables the video module.
        engine.enableVideo();
        // Setup video encoding configs
        engine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
                ((MainApplication) getActivity().getApplication()).getGlobalSettings().getVideoEncodingDimensionObject(),
                VideoEncoderConfiguration.FRAME_RATE.valueOf(((MainApplication) getActivity().getApplication()).getGlobalSettings().getVideoEncodingFrameRate()),
                STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.valueOf(((MainApplication) getActivity().getApplication()).getGlobalSettings().getVideoEncodingOrientation())
        ));

        /**Please configure accessToken in the string_config file.
         * A temporary token generated in Console. A temporary token is valid for 24 hours. For details, see
         *      https://docs.agora.io/en/Agora%20Platform/token?platform=All%20Platforms#get-a-temporary-token
         * A token generated at the server. This applies to scenarios with high-security requirements. For details, see
         *      https://docs.agora.io/en/cloud-recording/token_server_java?platform=Java*/
        TokenUtils.gen(requireContext(), channelId, 0, accessToken -> {
            /** Allows a user to join a channel.
             if you do not specify the uid, we will generate the uid for you*/

            ChannelMediaOptions option = new ChannelMediaOptions();
            option.clientRoleType = Constants.CLIENT_ROLE_AUDIENCE;
            option.autoSubscribeAudio = true;
            option.autoSubscribeVideo = true;
            option.publishCameraTrack = false;
            option.publishMicrophoneTrack = false;
            int res = engine.joinChannel(accessToken, channelId, 0, option);
            if (res != 0) {
                // Usually happens with invalid parameters
                // Error code description can be found at:
                // en: https://docs.agora.io/en/Voice/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_i_rtc_engine_event_handler_1_1_error_code.html
                // cn: https://docs.agora.io/cn/Voice/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_i_rtc_engine_event_handler_1_1_error_code.html
                showAlert(RtcEngine.getErrorDescription(Math.abs(res)));
                return;
            }
            // Prevent repeated entry
            join.setEnabled(false);
        });
    }

    private void createPushingVideoTrack() {
        if(!joined || videoTrackIds.size() >= 4){
            return;
        }
        int videoTrack = engine.createCustomVideoTrack();

        String channelId = et_channel.getText().toString();
        int uid = new Random().nextInt(1000) + 20000;
        RtcConnection connection = new RtcConnection(channelId, uid);

        TokenUtils.gen(requireContext(), channelId, uid, accessToken -> {
            ChannelMediaOptions option = new ChannelMediaOptions();
            option.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER;
            option.autoSubscribeAudio = true;
            option.autoSubscribeVideo = true;
            option.publishCameraTrack = false;
            option.publishCustomVideoTrack = true;
            option.customVideoTrackId = videoTrack;
            int res = engine.joinChannelEx(accessToken, connection, option, new IRtcEngineEventHandler() {
            });
            if (res != 0) {
                engine.destroyCustomVideoTrack(videoTrack);
                showAlert(RtcEngine.getErrorDescription(Math.abs(res)));
            } else {
                VideoFileReader videoFileReader = new VideoFileReader(requireContext(), videoFrame -> {
                    if (engine != null && joined) {
                        engine.pushExternalVideoFrameEx(videoFrame, videoTrack);
                    }
                });
                videoFileReader.start();
                videoTrackIds.add(videoTrack);
                videoFileReaders.add(videoFileReader);
                connections.add(connection);
            }
        });
    }

    private int destroyLastPushingVideoTrack() {
        int lastIndex = videoTrackIds.size() - 1;
        if(lastIndex < 0){
            return lastIndex;
        }
        int videoTrack = videoTrackIds.remove(lastIndex);
        VideoFileReader videoFileReader = videoFileReaders.remove(lastIndex);
        RtcConnection connection = connections.remove(lastIndex);

        videoFileReader.stop();
        engine.destroyCustomVideoTrack(videoTrack);
        engine.leaveChannelEx(connection);
        return lastIndex;
    }

    private void destroyAllPushingVideoTrack(){
        int index = videoTrackIds.size() - 1;
        while (index >= 0){
            index = destroyLastPushingVideoTrack();
        }
    }

    private VideoReportLayout getIdleVideoContainer() {
        for (VideoReportLayout frameLayout : flVideoContainer) {
            if (frameLayout.getReportUid() == -1) {
                return frameLayout;
            }
        }
        return null;
    }

    private View createVideoView(int uid) {
        VideoReportLayout videoContainer = getVideoLayoutByUid(uid);
        if (videoContainer == null) {
            videoContainer = getIdleVideoContainer();
        }
        if (videoContainer == null) {
            return null;
        }
        videoContainer.removeAllViews();
        SurfaceView videoView = new SurfaceView(requireContext());
        videoContainer.addView(videoView);
        videoContainer.setReportUid(uid);
        return videoView;
    }

    private void resetVideoLayout(int uid) {
        VideoReportLayout videoContainer = getVideoLayoutByUid(uid);
        if (videoContainer == null) {
            return;
        }
        videoContainer.removeAllViews();
        videoContainer.setReportUid(-1);
    }

    private void resetAllVideoLayout() {
        for (VideoReportLayout frameLayout : flVideoContainer) {
            if (frameLayout.getReportUid() != -1) {
                frameLayout.removeAllViews();
                frameLayout.setReportUid(-1);
            }
        }
    }

    private VideoReportLayout getVideoLayoutByUid(int uid) {
        for (VideoReportLayout frameLayout : flVideoContainer) {
            if (frameLayout.getReportUid() == uid) {
                return frameLayout;
            }
        }
        return null;
    }

    /**
     * IRtcEngineEventHandler is an abstract class providing default implementation.
     * The SDK uses this class to report to the app on SDK runtime events.
     */
    private final IRtcEngineEventHandler iRtcEngineEventHandler = new IRtcEngineEventHandler() {
        /**Reports a warning during SDK runtime.
         * Warning code: https://docs.agora.io/en/Voice/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_i_rtc_engine_event_handler_1_1_warn_code.html*/
        @Override
        public void onWarning(int warn) {
            Log.w(TAG, String.format("onWarning code %d message %s", warn, RtcEngine.getErrorDescription(warn)));
        }

        /**Occurs when a user leaves the channel.
         * @param stats With this callback, the application retrieves the channel information,
         *              such as the call duration and statistics.*/
        @Override
        public void onLeaveChannel(RtcStats stats) {
            super.onLeaveChannel(stats);
            Log.i(TAG, String.format("local user %d leaveChannel!", myUid));
            showLongToast(String.format("local user %d leaveChannel!", myUid));
        }

        /**Occurs when the local user joins a specified channel.
         * The channel name assignment is based on channelName specified in the joinChannel method.
         * If the uid is not specified when joinChannel is called, the server automatically assigns a uid.
         * @param channel Channel name
         * @param uid User ID
         * @param elapsed Time elapsed (ms) from the user calling joinChannel until this callback is triggered*/
        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            Log.i(TAG, String.format("onJoinChannelSuccess channel %s uid %d", channel, uid));
            showLongToast(String.format("onJoinChannelSuccess channel %s uid %d", channel, uid));
            myUid = uid;
            joined = true;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    join.setEnabled(true);
                    join.setText(getString(R.string.leave));
                }
            });
        }

        /**Occurs when a remote user (Communication)/host (Live Broadcast) joins the channel.
         * @param uid ID of the user whose audio state changes.
         * @param elapsed Time delay (ms) from the local user calling joinChannel/setClientRole
         *                until this callback is triggered.*/
        @Override
        public void onUserJoined(int uid, int elapsed) {
            super.onUserJoined(uid, elapsed);
            Log.i(TAG, "onUserJoined->" + uid);
            showLongToast(String.format("user %d joined!", uid));
            /**Check if the context is correct*/
            Context context = getContext();
            if (context == null) {
                return;
            }
            handler.post(() ->
            {
                /**Display remote video stream*/
                View videoView = createVideoView(uid);
                // Setup remote video to render
                engine.setupRemoteVideo(new VideoCanvas(videoView, RENDER_MODE_FIT, uid));
            });
        }

        /**Occurs when a remote user (Communication)/host (Live Broadcast) leaves the channel.
         * @param uid ID of the user whose audio state changes.
         * @param reason Reason why the user goes offline:
         *   USER_OFFLINE_QUIT(0): The user left the current channel.
         *   USER_OFFLINE_DROPPED(1): The SDK timed out and the user dropped offline because no data
         *              packet was received within a certain period of time. If a user quits the
         *               call and the message is not passed to the SDK (due to an unreliable channel),
         *               the SDK assumes the user dropped offline.
         *   USER_OFFLINE_BECOME_AUDIENCE(2): (Live broadcast only.) The client role switched from
         *               the host to the audience.*/
        @Override
        public void onUserOffline(int uid, int reason) {
            Log.i(TAG, String.format("user %d offline! reason:%d", uid, reason));
            showLongToast(String.format("user %d offline! reason:%d", uid, reason));
            runOnUIThread(() -> {
                /**Clear render view
                 Note: The video will stay at its last frame, to completely remove it you will need to
                 remove the SurfaceView from its parent*/
                resetVideoLayout(uid);
            });
        }

        @Override
        public void onRemoteVideoStats(RemoteVideoStats stats) {
            super.onRemoteVideoStats(stats);
            VideoReportLayout videoLayoutByUid = getVideoLayoutByUid(stats.uid);
            if(videoLayoutByUid != null){
                videoLayoutByUid.setRemoteVideoStats(stats);
            }
        }

        @Override
        public void onRemoteAudioStats(RemoteAudioStats stats) {
            super.onRemoteAudioStats(stats);
            VideoReportLayout videoLayoutByUid = getVideoLayoutByUid(stats.uid);
            if(videoLayoutByUid != null){
                videoLayoutByUid.setRemoteAudioStats(stats);
            }
        }
    };

}
