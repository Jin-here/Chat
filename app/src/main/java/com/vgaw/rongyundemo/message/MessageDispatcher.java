package com.vgaw.rongyundemo.message;

import java.util.ArrayList;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

/**
 * Created by caojin on 2016/2/29.
 */
public class MessageDispatcher {
    private ArrayList<RongIMClient.OnReceiveMessageListener> listenerList = new ArrayList<>();
    private static MessageDispatcher instance = new MessageDispatcher();

    private MessageDispatcher(){
        RongIM.setOnReceiveMessageListener(listener);
    }

    RongIMClient.OnReceiveMessageListener listener = new RongIMClient.OnReceiveMessageListener() {
        @Override
        public boolean onReceived(Message message, int i) {
            if (!(message.getContent() instanceof BaseMessage)){
                return false;
            }

            for (RongIMClient.OnReceiveMessageListener listener : listenerList){
                listener.onReceived(message, i);
            }
            return true;
        }
    };

    public static MessageDispatcher getInstance() {
        return instance;
    }

    public void addOnReceiveMessageListener(RongIMClient.OnReceiveMessageListener listener){
        listenerList.add(listener);
    }
}
