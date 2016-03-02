package com.vgaw.rongyundemo.message;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.vgaw.rongyundemo.R;
import com.vgaw.rongyundemo.activity.MainActivity;
import com.vgaw.rongyundemo.util.DataFactory;
import com.vgaw.rongyundemo.view.MyToast;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;

/**
 * Created by caojin on 2016/2/29.
 */
public class SystemEngine {
    private Context mContext;
    private static SystemEngine instance = new SystemEngine();

    public static SystemEngine getInstance(){
        return instance;
    }

    private SystemEngine(){}

    public void initial(Context mContext){
        this.mContext = mContext;
        MessageDispatcher.getInstance().addOnReceiveMessageListener(listener);
    }

    private RongIMClient.OnReceiveMessageListener listener = new RongIMClient.OnReceiveMessageListener() {
        @Override
        public boolean onReceived(Message message, int i) {
            MessageContent msgContent = message.getContent();
            if (msgContent instanceof SystemMessage){
                /*Intent intent = new Intent(mContext, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.default_head)
                        .setContentTitle("新朋友")
                        .setContentText(((SystemMessage) msgContent).getName() + "请求加为好友")
                        .setContentIntent(pendingIntent);
                ((NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, mBuilder.build());*/
                /*Intent intent = new Intent("com.vgaw.rongyundemo.addfriend");
                intent.putExtra("data", msgContent);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);*/
                DataFactory.getInstance().addSysMsg((SystemMessage) msgContent);
            }
            return true;

        }
    };
}
