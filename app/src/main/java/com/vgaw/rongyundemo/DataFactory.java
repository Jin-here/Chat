package com.vgaw.rongyundemo;

import com.amap.api.services.nearby.NearbyInfo;

import java.util.ArrayList;

/**
 * Created by caojin on 2016/2/17.
 */
public class DataFactory {
    private String username;
    private String pwd;
    private ArrayList<NearbyInfo> nearbyInfoList;
    private int lat;
    private int lng;

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
        if (listener != null){
            listener.onLocUpdated();
        }
    }

    private static DataFactory instance = new DataFactory();

    public DataFactory(){}

    public static DataFactory getInstance(){
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

    public interface OnLocUpdatedListener{
        void onLocUpdated();
    }

    private OnLocUpdatedListener listener;

    public void setOnLocUpdatedListener(OnLocUpdatedListener listener){
        this.listener = listener;
    }

}
