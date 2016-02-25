package com.vgaw.rongyundemo.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.nearby.NearbyInfo;
import com.amap.api.services.nearby.NearbySearch;
import com.amap.api.services.nearby.NearbySearchFunctionType;
import com.amap.api.services.nearby.NearbySearchResult;
import com.amap.api.services.nearby.UploadInfo;
import com.vgaw.rongyundemo.DataFactory;
import com.vgaw.rongyundemo.R;
import com.vgaw.rongyundemo.activity.MainActivity;
import com.vgaw.rongyundemo.activity.ShowLocActivity;

import java.util.ArrayList;

import javax.security.auth.login.LoginException;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * Created by caojin on 2016/2/20.
 */
public class MapShowFragment extends Fragment {
    private final long TIME_OUT = 5000;
    private final int max_limit = 5;
    // 防止主动取消请求后，由于请求仍在执行而造成的匹配仍在继续
    private boolean isPaused = false;
    private ProgressDialog progDialog;
    private Handler handler = new Handler();
    private Runnable delayRun = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getActivity(), "你藏的太深啦，人家都找不到人了咧～", Toast.LENGTH_SHORT).show();
            progDialog.dismiss();
        }
    };
    private Runnable matchRun = new Runnable() {
        @Override
        public void run() {
            match();
        }
    };
    private LatLonPoint latLonPoint;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private EditText et;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mapshow_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ((TextView) view.findViewById(R.id.tv_title)).setText("首页");
        et = (EditText) view.findViewById(R.id.et);
        ((Button) view.findViewById(R.id.btn_match)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaused = false;
                progDialog.show();
                //启动定位
                mLocationClient.startLocation();
                handler.postDelayed(delayRun, TIME_OUT);
            }
        });

        initialProgressDialog();

        mLocationClient = ((MainActivity)getActivity()).mLocationClient;
        //initialAmap();
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //调用异步查询接口，该接口只add一次
        NearbySearch.getInstance(getActivity()).addNearbyListener(nearbyListener);
    }

    NearbySearch.NearbyListener nearbyListener = new NearbySearch.NearbyListener() {
        @Override
        public void onUserInfoCleared(int i) {

        }

        @Override
        public void onNearbyInfoSearched(NearbySearchResult nearbySearchResult, int resultCode) {
            //搜索周边附近用户回调处理
            if (resultCode == 0) {
                if (nearbySearchResult != null
                        && nearbySearchResult.getNearbyInfoList() != null
                        && nearbySearchResult.getNearbyInfoList().size() > 0) {
                    ArrayList<NearbyInfo> nearbyInfoList = (ArrayList<NearbyInfo>) nearbySearchResult.getNearbyInfoList();

                    if (nearbyInfoList.size() > 1) {
                        DataFactory.getInstance().setNearbyInfoList(nearbyInfoList);
                        Toast.makeText(getActivity(), nearbyInfoList.get(1).getUserID(), Toast.LENGTH_SHORT).show();
                        if (!isPaused) {
                            progDialog.dismiss();
                            RongIM.getInstance().startConversation(getActivity(), Conversation.ConversationType.CHATROOM, "9527", "聊天室");
                        }
                    } else {
                        if (!isPaused){
                            handler.postDelayed(matchRun, 500);
                        }
                    }
                } else {
                    //Toast.makeText(getActivity(), "周边搜索结果为空", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "周边搜索出现异常，异常码为：" + resultCode, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onNearbyInfoUploaded(int i) {
            match();
        }
    };

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    double off = Double.parseDouble(et.getText().toString());
                    latLonPoint = new LatLonPoint(aMapLocation.getLatitude() + off, aMapLocation.getLongitude() + off);
                    DataFactory.getInstance().setLat((int) aMapLocation.getLatitude());
                    DataFactory.getInstance().setLng((int) aMapLocation.getLongitude());
                    uploadLocInfo();
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Toast.makeText(getActivity(), "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    /**
     * 上传位置信息
     */
    private void uploadLocInfo() {
        //构造上传位置信息
        UploadInfo loadInfo = new UploadInfo();
        //设置上传位置的坐标系支持AMap坐标数据与GPS数据
        loadInfo.setCoordType(NearbySearch.AMAP);
        //设置上传数据位置,位置的获取推荐使用高德定位sdk进行获取
        loadInfo.setPoint(latLonPoint);
        //设置上传用户id
        loadInfo.setUserID(DataFactory.getInstance().getUsername());
        //调用异步上传接口
        NearbySearch.getInstance(getActivity())
                .uploadNearbyInfoAsyn(loadInfo);
    }

    private void match() {
        //设置搜索条件
        NearbySearch.NearbyQuery query = new NearbySearch.NearbyQuery();
        //设置搜索的中心点
        query.setCenterPoint(this.latLonPoint);
        //设置搜索的坐标体系
        query.setCoordType(NearbySearch.AMAP);
        //设置搜索半径
        //取值范围（1,10000]，单位：米，超出取值范围按照10公里返回
        query.setRadius(10000);
        //设置查询的时间
        //规则：[5,86400]，单位：秒 可检索5s~24小时之内上传过数据的用户信息 超过24小时未上传过数据的用户将作为过期数据，无法返回
        query.setTimeRange(5000);
        //设置查询的方式驾车还是距离
        query.setType(NearbySearchFunctionType.DRIVING_DISTANCE_SEARCH);
        NearbySearch.getInstance(getActivity()).searchNearbyInfoAsyn(query);
    }

    private void initialProgressDialog() {
        progDialog = new ProgressDialog(getActivity());
        progDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isPaused = true;
                handler.removeCallbacks(delayRun);
                handler.removeCallbacks(matchRun);
            }
        });
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 移除listener，防止添加多次listener，因为是单例
        NearbySearch.getInstance(getActivity()).removeNearbyListener(nearbyListener);
    }

        /*//用户信息清除后，将不会再被检索到，比如接单的美甲师下班后可以清除其位置信息。
        //获取附近实例，并设置要清楚用户的id
        NearbySearch.getInstance(getActivity()).setUserID("test_test_test_1");
        //调用异步清除用户接口
        NearbySearch.getInstance(getActivity())
                .clearUserInfoAsyn();*/

}
