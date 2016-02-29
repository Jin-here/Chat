package com.vgaw.rongyundemo.message;

import android.os.Parcel;

import com.sea_monster.common.ParcelUtils;

import io.rong.imlib.model.MessageContent;

/**
 * Created by caojin on 2016/2/29.
 */
public class BaseMessage extends MessageContent {
    public BaseMessage(){}
    public BaseMessage(byte[] data){

    }

    public BaseMessage(Parcel in){
    }

    public static final Creator<BaseMessage> CREATOR = new Creator<BaseMessage>() {
        @Override
        public BaseMessage createFromParcel(Parcel source) {
            return new BaseMessage(source);
        }

        @Override
        public BaseMessage[] newArray(int size) {
            return new BaseMessage[size];
        }
    };

    @Override
    public byte[] encode() {
        return new byte[0];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
