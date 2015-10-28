package com.vgaw.rongyundemo.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.vgaw.rongyundemo.R;
import com.vgaw.rongyundemo.app.App;
import com.vgaw.rongyundemo.fragment.WarnFragment;
import com.vgaw.rongyundemo.protopojo.FlyCatProto;
import com.vgaw.rongyundemo.util.WarnFragmentHelper;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

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

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {

                    Log.d("LoginActivity", "--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {

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
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();

        // shake between left and right to warn the user.
        Animation shakeAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.shake);

        // if user do not input anything.
        if ("".equals(username)) {
            new WarnFragmentHelper(manager, R.id.warn_fragment, getString(R.string.username_blank)).warn();
            et_username.startAnimation(shakeAnimation);
            return;
        }

        // if user do not input anything.
        if ("".equals(password)) {
            new WarnFragmentHelper(manager, R.id.warn_fragment, getString(R.string.password_blank)).warn();
            et_password.startAnimation(shakeAnimation);
            return;
        }

        switch (v.getId()) {
            case R.id.btn_login:
                new MyLoginTask(username, password).execute();
                break;
            case R.id.btn_register:
                new MyRegisterTask(username, password).execute();
                break;
        }
    }

    private class MyLoginTask extends AsyncTask<Void, Void, Integer>{
        private String username;
        private String password;

        public MyLoginTask(String username, String password){
            this.username = username;
            this.password = password;
        }

        @Override
        protected void onPostExecute(Integer aInteger) {
            Animation shakeAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.shake);

            if (aInteger != -1){
                new WarnFragmentHelper(manager, R.id.warn_fragment, getString(aInteger)).warn();
            }else{
                connect(app.getSp().getString(App.TOKEN, null));
                return;
            }
            if (aInteger == R.string.password_incorrect){
                et_username.startAnimation(shakeAnimation);
                et_password.setText("");
            }
            if (aInteger == R.string.username_dont_exist){
                et_username.startAnimation(shakeAnimation);
                et_username.setText("");
            }
        }

        @Override
        protected Integer doInBackground(Void... params) {
            if (username.equals(app.getSp().getString(App.USER_NAME, null))){
                if (password.equals(app.getSp().getString(App.PASSWORD, null))){
                    return -1;
                }else{
                    // password incorrect.
                    return R.string.password_incorrect;
                }
            }else{
                FlyCatProto.FlyCat response = request.requestForResult(FlyCatProto.FlyCat.newBuilder()
                        .setFlag(4)
                        .addStringV(username)
                        .build());
                if (!response.getBoolV(0)){
                    return R.string.username_dont_exist;
                }
                String token = response.getStringV(0);
                String passwordCorrect = response.getStringV(2);
                if (!password.equals(passwordCorrect)){
                    return R.string.password_incorrect;
                }

                app.getSp().edit().putString(App.TOKEN, token).commit();
                app.getSp().edit().putString(App.USER_NAME, username).commit();
                app.getSp().edit().putString(App.PASSWORD, password).commit();
                return -1;
            }
        }
    }

    private class MyRegisterTask extends AsyncTask<Void, Void, Boolean> {
        private WarnFragment warnFragment;
        private String username;
        private String password;

        public MyRegisterTask(String username, String password){
            warnFragment = new WarnFragment();
            this.username = username;
            this.password = password;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean){
                // register succeed.
                warnFragment.updateWarnInfo("注册成功");
            }else{
                // register failed.
                warnFragment.updateWarnInfo("用户名已存在");
                // shake between left and right to warn the user.
                Animation shakeAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.shake);
                et_username.startAnimation(shakeAnimation);
                et_username.setText("");
            }
            // remove the warn fragment.
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    FragmentTransaction fragmentTransaction1 = manager.beginTransaction();
                    fragmentTransaction1.remove(warnFragment);
                    fragmentTransaction1.commit();
                }
            };
            timer.schedule(timerTask, 1000);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            FlyCatProto.FlyCat response = request.requestForResult(FlyCatProto.FlyCat.newBuilder()
                    .setFlag(4)
                    .addStringV(username)
                    .build());
            // user is exist.
            if (response.getBoolV(0)){
                return false;
            }
            // #1 request rongyun server for token.
            // #2 send complete user info the app server for persist.
            String token = requestForToken(username);
            app.getSp().edit().putString(App.TOKEN, token).commit();
            app.getSp().edit().putString(App.USER_NAME, username).commit();
            app.getSp().edit().putString(App.PASSWORD, password).commit();
            request.request(FlyCatProto.FlyCat.newBuilder().setFlag(1).addStringV(token).addStringV(username).addStringV(password).build());
            return true;
        }

        @Override
        protected void onPreExecute() {
            warnFragment.setWarnInfo("请稍等。。。");
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.warn_fragment, warnFragment, WarnFragment.TAG);
            transaction.commit();
        }
    }
}
