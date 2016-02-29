package com.vgaw.rongyundemo.message;

import android.os.Parcel;

import com.sea_monster.common.ParcelUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * Created by caojin on 2016/2/25.
 */
@MessageTag(value = "app:match", flag = MessageTag.NONE)
public class MatchMessage extends BaseMessage {
    // 请求或返回标识
    private int code = -1;
    // 聊天室id
    private String chatRoomId = "fuck";


    public MatchMessage(int code){
        super();
        this.code = code;
    }

    public int getCode(){
        return this.code;
    }

    public String getChatRoomId(){
        return this.chatRoomId;
    }

    public MatchMessage setChatRoomId(String chatRoomId){
        this.chatRoomId = chatRoomId;
        return this;
    }

    public MatchMessage(byte[] data){
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("code")){
                this.code = jsonObj.optInt("code");
            }
            if (jsonObj.has("chatRoomId")){
                this.chatRoomId = jsonObj.optString("chatRoomId");
            }

        } catch (JSONException e) {
        }
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", this.code);
            jsonObject.put("chatRoomId", this.chatRoomId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            return jsonObject.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public MatchMessage(Parcel in){
        this.code = ParcelUtils.readIntFromParcel(in);
        this.chatRoomId = ParcelUtils.readFromParcel(in);
    }

    public static final Creator<MatchMessage> CREATOR = new Creator<MatchMessage>() {
        @Override
        public MatchMessage createFromParcel(Parcel source) {
            return new MatchMessage(source);
        }

        @Override
        public MatchMessage[] newArray(int size) {
            return new MatchMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.code);
        ParcelUtils.writeToParcel(dest, this.chatRoomId);
    }
}
