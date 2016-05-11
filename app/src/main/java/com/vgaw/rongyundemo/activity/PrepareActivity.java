package com.vgaw.rongyundemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.vgaw.rongyundemo.App;
import com.vgaw.rongyundemo.Preference;
import com.vgaw.rongyundemo.R;
import com.vgaw.rongyundemo.http.HttpCat;
import com.vgaw.rongyundemo.message.MatchEngine;
import com.vgaw.rongyundemo.message.SystemEngine;
import com.vgaw.rongyundemo.protopojo.FlyCatProto;
import com.vgaw.rongyundemo.util.DataFactory;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by caojin on 2016/4/29.
 */
public class PrepareActivity extends Activity {
    private static long MIN_TIME = 2000;

    private boolean isLogined = false;
    private long currentTime;

    private String pwd = null;
    private String username = null;
    private long id = -1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*TextView tv = new TextView(PrepareActivity.this);
        tv.setText("CHAT");
        tv.setTextSize(50);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundColor(getResources().getColor(R.color.color_main));*/
        setContentView(R.layout.activity_prepare);
        // 沉浸式
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        currentTime = System.currentTimeMillis();

        loginBackground();
    }

    /**
     * 后台自动登录
     */
    private void loginBackground() {
        username = Preference.getInstance().getUsername();
        pwd = Preference.getInstance().getPwd();
        if (username != null && pwd != null){
            HttpCat.fly(FlyCatProto.FlyCat.newBuilder().setFlag(5).addStringV(username).build(), new HttpCat.AbstractResponseListener(){
                @Override
                public void onSuccess(FlyCatProto.FlyCat flyCat) {
                    if (flyCat.getFlag() == 1){
                        HttpCat.fly(FlyCatProto.FlyCat.newBuilder().setFlag(2).addStringV(username).addStringV(pwd).build(), new HttpCat.AbstractResponseListener(){
                            @Override
                            public void onSuccess(FlyCatProto.FlyCat flyCat) {
                                if (flyCat.getFlag() == 1){
                                    id = flyCat.getLongV(0);
                                    connect(flyCat.getStringV(0));
                                }
                            }
                        });
                    }else {
                        // 用户不存在
                        goToNext();
                    }
                }

                @Override
                public void onException(FlyCatProto.FlyCat flyCat) {
                    super.onException(flyCat);
                    Toast.makeText(PrepareActivity.this, "请求出现故障，请稍后再试", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }else {
            goToNext();
        }
    }

    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    private void connect(String token) {

        if (getApplicationInfo().packageName.equals(App.getCurProcessName(getApplicationContext()))) {
            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {
                    Toast.makeText(PrepareActivity.this, "请求出现故障，请稍后再试", Toast.LENGTH_SHORT).show();
                    finish();
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    DataFactory.getInstance().setId(id);
                    DataFactory.getInstance().setUsername(username);
                    DataFactory.getInstance().setPwd(pwd);
                    isLogined = true;
                    goToNext();
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Toast.makeText(PrepareActivity.this, "请求出现故障，请稍后再试", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    private void goToNext(){
        long delay = System.currentTimeMillis() - currentTime;
        if (delay > MIN_TIME){
            direct();
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            direct();
                        }
                    });
                }
            }, MIN_TIME - delay);
        }
    }

    private void direct(){
        if (isLogined){
            startActivity(new Intent(PrepareActivity.this, MainActivity.class));
        }else {
            startActivity(new Intent(PrepareActivity.this, LoginActivity.class));
        }
        finish();
    }

}
