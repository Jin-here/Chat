package com.vgaw.rongyundemo.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.radar.RadarNearbyResult;
import com.baidu.mapapi.radar.RadarNearbySearchOption;
import com.baidu.mapapi.radar.RadarSearchError;
import com.baidu.mapapi.radar.RadarSearchListener;
import com.baidu.mapapi.radar.RadarSearchManager;
import com.baidu.mapapi.radar.RadarUploadInfo;
import com.baidu.mapapi.radar.RadarUploadInfoCallback;
import com.vgaw.rongyundemo.R;
import com.vgaw.rongyundemo.app.App;
import com.vgaw.rongyundemo.protopojo.FlyCatProto;
import com.vgaw.rongyundemo.util.WarnFragmentHelper;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * Created by caojin on 15-10-21.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, RadarSearchListener {

    private EditText et_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_name = (EditText) findViewById(R.id.et_name);
        Button btn_private = (Button) findViewById(R.id.btn_private);
        Button btn_conversationList = (Button) findViewById(R.id.btn_conversationList);
        btn_private.setOnClickListener(this);
        btn_conversationList.setOnClickListener(this);

        Button btn_chatroom = (Button) findViewById(R.id.btn_chatroom);
        btn_chatroom.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_private:
                // query whether the user exist
                //      exist:start private talk
                //      not:warn the user
                new MyTask(et_name.getText().toString()).execute();
                break;
            case R.id.btn_conversationList:
                //启动会话列表界面
                if (RongIM.getInstance() != null)
                    RongIM.getInstance().startConversationList(this);
                break;
            case R.id.btn_chatroom:
                /**
                 * 启动聊天室聊天界面。
                 *
                 * @param context          应用上下文。
                 * @param conversationType 开启会话类型。
                 * @param targetId         聊天室 Id。
                 * @param title            聊天的标题，如果传入空值，则默认显示会话的名称。
                 */
                RongIM.getInstance().startConversation(MainActivity.this, Conversation.ConversationType.CHATROOM, "9527", "标题");
                break;
        }
    }


    /*
    * 以会话页面的启动 Uri 为例说明：
    * rong://{packagename:应用包名}/conversation/[private|discussion|group]?targetId={目标Id}&[title={开启会话名称}]
    * 上面的例子，如果你的包名为 io.rong.imkit.demo，目标 Id 为 12345 的私聊会话，拼接后的 Uri 就是
    * rong://io.rong.imkit.demo/conversation/private?targetId=12345
    *
    *
    * */

    private class MyTask extends AsyncTask<Void, Void, Boolean> {
        private String name;
        private FlyCatProto.FlyCat response;

        public MyTask(String name) {
            this.name = name;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                /**
                 * 启动单聊
                 * context - 应用上下文。
                 * targetUserId - 要与之聊天的用户 Id。
                 * title - 聊天的标题，如果传入空值，则默认显示与之聊天的用户名称。
                 */
                //启动会话界面
                if (RongIM.getInstance() != null)
                    RongIM.getInstance().startPrivateChat(MainActivity.this, response.getStringV(1), response.getStringV(1));
            } else {
                new WarnFragmentHelper(manager, R.id.warn_fragment, getResources().getString(R.string.username_dont_exist)).warn();
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            response = request.requestForResult(FlyCatProto.FlyCat.newBuilder().setFlag(4).addStringV(name).build());
            return response.getBoolV(0);
        }
    }


    private void radarMatch() {
        // 初始化周边雷达功能模块
        final RadarSearchManager mManager = RadarSearchManager.getInstance();

        // 周边雷达设置监听
        mManager.addNearbyInfoListener(this);
        // 周边雷达设置用户身份标识，id为空默认是设备标识
        mManager.setUserID(app.getSp().getString(App.USER_NAME, null));

        // 定位初始化
        LocationClient mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation == null) {
                    return;
                }
                final LatLng pt = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());

                if (pt == null) {
                    Toast.makeText(getBaseContext(), "未获取到位置", Toast.LENGTH_SHORT).show();
                    return;
                }
                // upload info per 3 seconds
                final RadarUploadInfo info = new RadarUploadInfo();
                // add chatroom info like id, members...
                info.comments = "用户备注信息";
                info.pt = pt;
                RadarSearchManager.getInstance().startUploadAuto(new RadarUploadInfoCallback() {
                    @Override
                    public RadarUploadInfo onUploadInfoCallback() {
                        return info;
                    }
                }, 3000);

                //构造请求参数，其中centerPt是自己的位置坐标
                RadarNearbySearchOption option = new RadarNearbySearchOption().centerPt(pt).pageNum(0).radius(2000);
                //发起查询请求
                mManager.nearbyInfoRequest(option);

            }
        });
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(false); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        // 开始定位
        mLocClient.start();

    }

    @Override
    public void onGetNearbyInfoList(RadarNearbyResult radarNearbyResult, RadarSearchError radarSearchError) {
        if (radarSearchError == RadarSearchError.RADAR_NO_ERROR) {
            Toast.makeText(getBaseContext(), "查询周边成功", Toast.LENGTH_LONG)
                    .show();
            // 获取成功，处理数据
        } else {
            // 获取失败
            Toast.makeText(getBaseContext(), "查询周边失败", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onGetUploadState(RadarSearchError radarSearchError) {

    }

    @Override
    public void onGetClearInfoState(RadarSearchError radarSearchError) {

    }

}
