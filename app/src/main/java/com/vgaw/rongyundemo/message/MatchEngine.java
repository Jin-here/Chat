package com.vgaw.rongyundemo.message;

import android.content.Context;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.vgaw.rongyundemo.util.DataFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;

/**
 * Created by caojin on 2016/2/25.
 */

/**
 * 注意：只能多加一，不能多加多
 * 一次实际上只处理一个请求
 */
public class MatchEngine {
    public static final int INVITE = 7;
    private final int AGREE = 8;
    private final int AGREE_AGREE = 9;
    private final int AGREE_REFUSE = 10;
    private final int LEAVE = 11;

    // 总开关
    private boolean canBeMatched = false;

    // 是否可以接受别人邀请
    private boolean can_agree = true;
    // 是否可以同意别人加入
    private boolean can_accept = true;

    private int now_num = 1;
    private int max_num = 2;
    private String another = null;

    private static MatchEngine instance = new MatchEngine();

    private Context mContext;

    private MatchEngine() {
    }

    public static MatchEngine getInstance() {
        return instance;
    }

    /**
     * 需在connect之前调用
     */
    public void initial(Context mContext) {
        this.mContext = mContext;
        //RongIM.setOnReceiveMessageListener(listener);
        MessageDispatcher.getInstance().addOnReceiveMessageListener(listener);
    }

    //  邀请，同意邀请，同意邀请被拒绝，同意邀请被同意，离开聊天室
    RongIMClient.OnReceiveMessageListener listener = new RongIMClient.OnReceiveMessageListener() {
        @Override
        public boolean onReceived(Message message, int i) {
            if (i != 0){
                return true;
            }
            MessageContent msgContent = message.getContent();
            if (msgContent instanceof MatchMessage) {
                synchronized (this) {
                    int code = ((MatchMessage) msgContent).getCode();
                    final String targetId = message.getSenderUserId();
                    if (!canBeMatched && code != LEAVE){
                        return true;
                    }
                    switch (code) {
                        /***********************作为被邀请方(一次回复只能收到一次对应回复)，一旦回复就会被锁住，所以收到的对应回复和回复是对应的**********************************/
                        case INVITE:
                            if (can_agree && now_num == 1) {
                                RongIM.getInstance().getRongIMClient().sendMessage(Conversation.ConversationType.PRIVATE, targetId, new MatchMessage(AGREE), "", "", new RongIMClient.SendMessageCallback() {
                                    @Override
                                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {

                                    }

                                    @Override
                                    public void onSuccess(Integer integer) {
                                        // 互斥锁
                                        can_accept = false;
                                        can_agree = false;
                                    }
                                });
                                Log.e("fuck", "接受邀请->");
                            } else {
                                // 拒绝邀请
                                Log.e("fuck", "拒绝邀请->");
                            }
                            break;
                        case AGREE_REFUSE:
                            can_accept = true;
                            can_agree = true;
                            Log.e("fuck", "接受邀请被拒绝->");
                            break;
                        case AGREE_AGREE:
                            Log.e("fuck", "接受邀请被同意->");
                            another = targetId;
                            can_accept = false;
                            can_agree = false;
                            now_num++;
                            String chatRoomId = ((MatchMessage) msgContent).getChatRoomId();
                            //RongIM.getInstance().startConversation(mContext, Conversation.ConversationType.CHATROOM, chatRoomId, targetId);
                            if (listener2 != null){
                                listener2.onMatched(chatRoomId, targetId);
                            }
                            break;

                        /***********************作为邀请方**********************************/
                        case AGREE:
                            if (can_accept && now_num < max_num) {
                                Log.e("fuck", "同意此接受->");
                                another = targetId;
                                final String chatRoomId1 = genChatRoomId(DataFactory.getInstance().getUsername(), targetId);
                                RongIM.getInstance().getRongIMClient().sendMessage(Conversation.ConversationType.PRIVATE, targetId, new MatchMessage(AGREE_AGREE).setChatRoomId(chatRoomId1), "", "", new RongIMClient.SendMessageCallback() {
                                    @Override
                                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {

                                    }

                                    @Override
                                    public void onSuccess(Integer integer) {
                                        // 互斥锁
                                        can_accept = false;
                                        can_agree = false;
                                        //RongIM.getInstance().startConversation(mContext, Conversation.ConversationType.CHATROOM, chatRoomId1, targetId);
                                        if (listener2 != null){
                                            listener2.onMatched(chatRoomId1, targetId);
                                        }
                                    }
                                });
                            } else {
                                sendResponse(targetId, AGREE_REFUSE);
                                Log.e("fuck", "拒绝此接受->");
                            }
                            break;

                        case LEAVE:
                            if (listener1 != null){
                                listener1.onUserLeaved();
                            }
                            initialStatus();
                            break;
                    }
                    return true;
                }
            }
            return false;
        }
    };

    public void initialStatus(){
        can_agree = true;
        can_accept = true;
        now_num = 1;
    }

    public void sendLeave(){
        initialStatus();
        sendResponse(another, LEAVE);
    }

    public void sendResponse(Context mContext, String targetId, int code){
        this.mContext = mContext;
        sendResponse(targetId, code);
    }

    public void sendResponse(String targetId, int code) {
        RongIM.getInstance().getRongIMClient().sendMessage(Conversation.ConversationType.PRIVATE, targetId, new MatchMessage(code), "", "", null);
    }

    public void sendResponse(String targetId, int code, String chatRoomId) {
        RongIM.getInstance().getRongIMClient().sendMessage(Conversation.ConversationType.PRIVATE, targetId, new MatchMessage(code).setChatRoomId(chatRoomId), "", "", null);
    }

    private String genChatRoomId(String meId, String targetId){
        if (meId.compareTo(targetId) > 0){
            return hashKeyForDisk(targetId + "&" + meId);
        }
        return hashKeyForDisk(meId + "&" + targetId);
    }
    private String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public interface OnUserLeavedListener {
        void onUserLeaved();
    }

    public interface OnMatchedListener{
        void onMatched(String chatRoomId, String targetId);
    }

    private OnUserLeavedListener listener1 = null;
    private OnMatchedListener listener2 = null;

    public void setOnUserLeavedListener(OnUserLeavedListener listener) {
        this.listener1 = listener;
    }

    public void setOnMatchedListener(OnMatchedListener listener){
        this.listener2 = listener;
    }

    public void setCanBeMatched(boolean canBeMatched){
        this.canBeMatched = canBeMatched;
    }
}
