package com.vgaw.rongyundemo.util;

import android.content.Context;
import android.view.WindowManager;

import com.amap.api.services.nearby.NearbyInfo;
import com.vgaw.rongyundemo.message.SystemEngine;
import com.vgaw.rongyundemo.message.SystemMessage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by caojin on 2016/2/17.
 */
public class DataFactory {
    private Context mContext;

    private long id;
    private String username;
    private String pwd;
    private ArrayList<NearbyInfo> nearbyInfoList;
    private int lat;
    private int lng;
    private ArrayList<SystemMessage> sysMsgList = new ArrayList<>();
    private ArrayList<String> friendList = new ArrayList<>();

    public void setFriendList(ArrayList<String> friendList){
        this.friendList = friendList;
    }

    public ArrayList<String> getFriendList(){
        return this.friendList;
    }

    public void addSysMsg(SystemMessage msg) {
        for (SystemMessage s : sysMsgList){
            if (s.getCode() == msg.getCode()
                    && s.getName().equals(msg.getName())){
                return;
            }
        }
        sysMsgList.add(msg);
        if (listener1 != null) {
            listener1.onFriendAdded(msg);
        }
    }

    public void removeSysMsg(SystemMessage msg){
        sysMsgList.remove(msg);
    }

    public ArrayList<SystemMessage> getSysMsg() {
        return this.sysMsgList;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public int getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public int getLng() {
        return lng;
    }

    public void setLng(int lng) {
        this.lng = lng;
    }

    public ArrayList<NearbyInfo> getNearbyInfoList() {
        return nearbyInfoList;
    }

    public void setNearbyInfoList(ArrayList<NearbyInfo> nearbyInfoList) {
        this.nearbyInfoList = nearbyInfoList;
        if (listener != null) {
            listener.onLocUpdated();
        }
    }

    private static DataFactory instance = new DataFactory();

    public DataFactory() {
    }

    public static DataFactory getInstance() {
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public interface OnLocUpdatedListener {
        void onLocUpdated();
    }

    public interface OnAddFriendListener {
        void onFriendAdded(SystemMessage msg);
    }

    private OnLocUpdatedListener listener;
    private OnAddFriendListener listener1;

    public void setOnLocUpdatedListener(OnLocUpdatedListener listener) {
        this.listener = listener;
    }

    public void setOnAddFriendListener(OnAddFriendListener listener1) {
        this.listener1 = listener1;
    }

    public void initial(Context mContext) {
        this.mContext = mContext;
    }
}
