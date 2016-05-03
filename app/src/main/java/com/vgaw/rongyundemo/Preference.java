package com.vgaw.rongyundemo;

import android.content.Context;

/**
 * Created by caojin on 2016/4/29.
 */
public class Preference {
    private static final String PREFERENCE_NAME = "chat";
    private static final String KEY_PHONE = "key_name";
    private static final String KEY_PWD = "key_pwd";

    public static Preference instance = new Preference();
    private Context mContext;

    private Preference(){}

    public static Preference getInstance(){
        return instance;
    }

    public void init(Context mContext){
        this.mContext = mContext;
    }

    /**
     * 保存手机号
     * @return
     */
    public String getUsername(){
        return mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getString(KEY_PHONE, null);
    }

    public void setUsername(String username){
        mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit().putString(KEY_PHONE, username).commit();
    }

    /**
     * 保存密码
     * @param pwd
     */
    public void setPwd(String pwd){
        mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit().putString(KEY_PWD, pwd).commit();
    }

    public String getPwd(){
        return mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getString(KEY_PWD, null);
    }
}
