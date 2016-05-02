package com.vgaw.rongyundemo.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.vgaw.rongyundemo.message.MatchEngine;
import com.vgaw.rongyundemo.http.HttpCat;
import com.vgaw.rongyundemo.message.SystemEngine;
import com.vgaw.rongyundemo.util.DataFactory;
import com.vgaw.rongyundemo.R;
import com.vgaw.rongyundemo.App;
import com.vgaw.rongyundemo.protopojo.FlyCatProto;
import com.vgaw.rongyundemo.view.Loading;
import com.vgaw.rongyundemo.view.MyToast;

import org.json.JSONObject;

import io.rong.ApiHttpClient;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.models.FormatType;
import io.rong.models.SdkHttpResult;

/**
 * Created by caojin on 15-10-21.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_username;
    private EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setStatusBarColor(getResources().getColor(android.R.color.background_dark));

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        Button btn_login = (Button) findViewById(R.id.btn_login);
        Button btn_register = (Button) findViewById(R.id.btn_register);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);

    }

    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    private void connect(String token) {

        if (getApplicationInfo().packageName.equals(App.getCurProcessName(getApplicationContext()))) {
            MatchEngine.getInstance().initial(LoginActivity.this);
            SystemEngine.getInstance().initial(getApplicationContext());
            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {
                    Loading.getInstance(LoginActivity.this).dismiss();
                    Log.d("LoginActivity", "--onTokenIncorrect");
                    MyToast.makeText(LoginActivity.this, "哎呀，您吓到我了，请慢点来").show();
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {

                    Loading.getInstance(LoginActivity.this).dismiss();
                    Log.d("LoginActivity", "--onSuccess" + userid);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                    Log.d("LoginActivity", "--onError" + errorCode);
                    Loading.getInstance(LoginActivity.this).dismiss();
                    MyToast.makeText(LoginActivity.this, "哎呀，您吓到我了，请慢点来").show();
                }
            });
        }
    }

    // request the token from rongyun server.
    private String requestForToken(String username) {
        String token = null;
        try {
            SdkHttpResult result = ApiHttpClient.getToken(App.KEY, App.SECRET, username, null,
                    null, FormatType.json);
            JSONObject json = new JSONObject(result.getResult());
            token = json.getString(App.TOKEN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_net){
            final Dialog dialog = new Dialog(LoginActivity.this, R.style.loading_dialog);
            View view = getLayoutInflater().inflate(R.layout.net_set, null);
            final EditText et_net = (EditText) view.findViewById(R.id.et_net);
            view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HttpCat.setUri(et_net.getText().toString());
                    dialog.dismiss();
                }
            });
            dialog.setContentView(view);
            dialog.show();
            return;
        }
        final String username = et_username.getText().toString();
        final String password = et_password.getText().toString();

        // shake between left and right to warn the user.
        final Animation shakeAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.shake);

        // if user do not input anything.
        if ("".equals(username)) {
            MyToast.makeText(LoginActivity.this, "用户名不能为空").show();
            et_username.startAnimation(shakeAnimation);
            return;
        }

        // if user do not input anything.
        if ("".equals(password)) {
            MyToast.makeText(LoginActivity.this, "密码不能为空").show();
            et_password.startAnimation(shakeAnimation);
            return;
        }

        switch (v.getId()) {
            case R.id.btn_login:
                HttpCat.fly(FlyCatProto.FlyCat.newBuilder().setFlag(5).addStringV(username).build(), new HttpCat.AbstractResponseListener(){
                    @Override
                    public void onPreExecute() {
                        Loading.getInstance(LoginActivity.this).show();
                    }

                    @Override
                    public void onSuccess(FlyCatProto.FlyCat flyCat) {
                        if (flyCat.getFlag() == 1){
                            HttpCat.fly(FlyCatProto.FlyCat.newBuilder().setFlag(2).addStringV(username).addStringV(password).build(), new HttpCat.AbstractResponseListener(){
                                @Override
                                public void onSuccess(FlyCatProto.FlyCat flyCat) {
                                    if (flyCat.getFlag() == 1){
                                        connect(flyCat.getStringV(0));
                                        DataFactory.getInstance().setId(flyCat.getLongV(0));
                                        DataFactory.getInstance().setUsername(username);
                                        DataFactory.getInstance().setPwd(password);
                                    }else {
                                        // 密码错误
                                        et_password.startAnimation(shakeAnimation);
                                        et_password.setText("");
                                        MyToast.makeText(LoginActivity.this, "唉，不小心手抖了").show();
                                        Loading.getInstance(LoginActivity.this).dismiss();
                                    }
                                }
                            });
                        }else {
                            // 用户不存在
                            et_username.startAnimation(shakeAnimation);
                            MyToast.makeText(LoginActivity.this, "该用户不存在，请注册喔").show();
                            Loading.getInstance(LoginActivity.this).dismiss();
                        }
                    }
                });
                break;
            case R.id.btn_register:
                HttpCat.fly(FlyCatProto.FlyCat.newBuilder().setFlag(5).addStringV(username).build(), new HttpCat.AbstractResponseListener(){
                    @Override
                    public void onPreExecute() {
                        Loading.getInstance(LoginActivity.this).show();
                    }

                    @Override
                    public void onSuccess(FlyCatProto.FlyCat flyCat) {
                        if (flyCat.getFlag() == 0){
                            new AsyncTask<String, Void, String>(){
                                @Override
                                protected String doInBackground(String... params) {
                                    return requestForToken(params[0]);
                                }

                                @Override
                                protected void onPostExecute(String s) {
                                    if (s == null){
                                        MyToast.makeText(LoginActivity.this, "哎呀，注册失败了，请稍后再试一次").show();
                                    }else {
                                        HttpCat.fly(FlyCatProto.FlyCat.newBuilder().setFlag(1).addStringV(s).addStringV(username).addStringV(password).build(), new HttpCat.AbstractResponseListener(){
                                            @Override
                                            public void onSuccess(FlyCatProto.FlyCat flyCat) {
                                                if (flyCat.getFlag() == 1){
                                                    MyToast.makeText(LoginActivity.this, "注册成功，欢迎加入！").show();
                                                }else {
                                                    MyToast.makeText(LoginActivity.this, "哎呀，注册失败了，请稍后再试一次").show();
                                                }
                                            }
                                        });
                                    }
                                    Loading.getInstance(LoginActivity.this).dismiss();
                                }
                            }.execute(username);
                        }else {
                            // 用户存在
                            et_username.startAnimation(shakeAnimation);
                            et_username.setText("");
                            MyToast.makeText(LoginActivity.this, "该用户已存在").show();
                            Loading.getInstance(LoginActivity.this).dismiss();
                        }
                    }
                });
                break;
        }
    }
}
