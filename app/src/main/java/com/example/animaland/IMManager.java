package com.example.animaland;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.imsdk.v2.V2TIMSDKListener;
import com.tencent.imsdk.v2.V2TIMValueCallback;

/**
 * 这是我整合的IM的管理类
 */
public class IMManager {

    /**
     * 是否已经登录到 im 服务器
     *
     * 用户已经处于已登录和登录中状态,则认为用户已经登录,无需在进行登录
     */
    public static boolean isLoginIMService(){
        int loginStatus = V2TIMManager.getInstance().getLoginStatus();
        return loginStatus == V2TIMManager.V2TIM_STATUS_LOGINED ||
                loginStatus == V2TIMManager.V2TIM_STATUS_LOGINING;
    }

    /**
     * 用户登录到IM服务器
     *
     * @param userID    用户编号
     * @param userSig   用户签名
     * @param callback  登录成功与否回调
     */
    public static void login(String userID, String userSig, V2TIMCallback callback) {
        if (isLoginIMService()) {
            callback.onSuccess();
            return;
        }
        V2TIMManager.getInstance().login(userID, userSig, callback);

    }

    /**
     * 初始化 IM 连接腾讯服务器
     *
     * @param context               上下文对象
     * @param imSDKConfig           imSdk配置
     * @param listener              连接腾讯服务器回调
     */
    public static void initConnectTXService(
            Context context,
            int imSdkAppId,
            V2TIMSDKConfig imSDKConfig,
            V2TIMSDKListener listener
    ) {
        V2TIMManager.getInstance()
                .initSDK(context, imSdkAppId, imSDKConfig, listener);
    }

    /**
     * 创建一个直播聊天室
     *
     * @param groupType         群类型(V2TIMManager.GROUP_TYPE_AVCHATROOM)
     * <p>
     *  "Work" ：工作群
     *  "Public" ：公开群
     *  "Meeting" ：会议群
     *  "AVChatRoom" ：直播群
     * </p>
     *
     * @param groupId          聊天室 id
     * @param groupName        聊天室名称
     * @param callback         创建状态回调
     */
    public static void createLiveRoom(
            String groupType,
            String groupId,
            @NonNull String groupName,
            V2TIMValueCallback<String> callback
    ) {
        V2TIMManager.getInstance().createGroup(groupType, groupId, groupName, callback);
    }

    /**
     * 加入到某一个直播间 IM
     *
     * @param groupId           聊天室 id
     * @param msg               聊天室名称
     * @param listener          是否成功接入回调
     */
    public static void joinToLiveRoom(String groupId, String msg, V2TIMCallback listener) {
        V2TIMManager.getInstance().joinGroup(groupId, msg, listener);
    }

    /**
     * 退出直播直播间
     *
     * @param groupId           聊天室 id
     */
    public static void exitLiveRoom(String groupId) {
        V2TIMManager.getInstance().quitGroup(groupId, null);
    }

    /**
     * 发送纯文本消息到聊天群组
     * @param msg                发送的聊天内容
     * @param groupId            聊天室 ID
     * @param priority           发送消息的优先级 (V2TIMMessage.V2TIM_PRIORITY_NORMAL)
     * <p>
     *     V2TIMMessage.V2TIM_PRIORITY_HIGH：云端会优先传输，适用于在群里发送重要消息，比如主播发送的文本消息等。
     *     V2TIMMessage.V2TIM_PRIORITY_NORMAL：云端按默认优先级传输，适用于在群里发送非重要消息，比如观众发送的弹幕消息等。
     * </p>
     * @param callback           发送状态回调
     */
    public static void sendTextMsg(
            String msg,
            String groupId,
            int priority,
            V2TIMValueCallback<V2TIMMessage> callback
    ) {
        V2TIMManager.getInstance().sendGroupTextMessage(
                msg,
                groupId,
                priority, callback
        );
    }

    /**
     *  发送单聊普通文本消息
     */
    public static void sendC2CTextMsg(String msg, String userID, V2TIMValueCallback<V2TIMMessage> callback) {
        V2TIMManager.getInstance().sendC2CTextMessage(msg, userID, callback);
    }

    /**
     * 接受消息
     */
    public static void receiveMsg(V2TIMAdvancedMsgListener callback) {
        // 设置事件监听器
        V2TIMManager.getMessageManager().addAdvancedMsgListener(callback);
    }

    /**
     * 解散聊天室
     */
    public static void dismissGroup(String groupId) {
        if (TextUtils.isEmpty(groupId)) {
            V2TIMManager.getInstance().dismissGroup(groupId, null);
        }
    }

    /**
     * 用户退出 im 登录
     */
    public static void userLogout() {
        V2TIMManager.getInstance().logout(null);
    }

    /**
     * 登出账号并注销 im sdk
     */
    public static void unInitIMSDk() {
        userLogout();
        V2TIMManager.getInstance().unInitSDK();
    }

}