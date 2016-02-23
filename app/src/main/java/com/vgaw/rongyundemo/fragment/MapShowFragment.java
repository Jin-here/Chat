package com.vgaw.rongyundemo.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.vgaw.rongyundemo.activity.ShowLocActivity;

import java.util.ArrayList;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * Created by caojin on 2016/2/20.
 */
public class MapShowFragment extends Fragment {
    private final int max_limit = 5;
    private boolean isChated = false;
    private ProgressDialog progDialog;
    private Handler handler = new Handler();
    private Runnable delayRun = new Runnable() {
        @Override
        public void run() {
            if (!isChated) {
                Toast.makeText(getActivity(), "请稍后再试", Toast.LENGTH_SHORT).show();
                progDialog.dismiss();
            }
        }
    };
    private LatLonPoint latLonPoint;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mapshow_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ((TextView) view.findViewById(R.id.tv_title)).setText("首页");
        ((Button) view.findViewById(R.id.btn_match)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*progDialog.show();
                //启动定位
                mLocationClient.startLocation();
                handler.postDelayed(delayRun, 5000);*/
                //RongIM.getInstance().startConversation(getActivity(), Conversation.ConversationType.CHATROOM, "9527", "聊天室");
                startActivity(new Intent(getActivity(), ShowLocActivity.class));
            }
        });

        initialProgressDialog();
        //初始化定位
        mLocationClient = new AMapLocationClient(getActivity());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

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

                    if (nearbyInfoList.size() > 1){
                        DataFactory.getInstance().setNearbyInfoList(nearbyInfoList);
                        if (!isChated){
                            isChated = true;
                            progDialog.dismiss();
                            handler.removeCallbacks(delayRun);
                            startActivity(new Intent(getActivity(), ShowLocActivity.class));
                        }
                    }else {
                        match();
                    }
                } else {
                    Toast.makeText(getActivity(), "周边搜索结果为空", Toast.LENGTH_SHORT).show();
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
                    latLonPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    DataFactory.getInstance().setLat((int)aMapLocation.getLatitude());
                    DataFactory.getInstance().setLng((int) aMapLocation.getLongitude());
                    uploadLocInfo();
                    //aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    /*aMapLocation.getAccuracy();//获取精度信息
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(aMapLocation.getTime());
                    df.format(date);//定位时间
                    aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    aMapLocation.getCountry();//国家信息
                    aMapLocation.getProvince();//省信息
                    aMapLocation.getCity();//城市信息
                    aMapLocation.getDistrict();//城区信息
                    aMapLocation.getStreet();//街道信息
                    aMapLocation.getStreetNum();//街道门牌号信息
                    aMapLocation.getCityCode();//城市编码
                    aMapLocation.getAdCode();//地区编码*/
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Toast.makeText(getActivity(), "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private void uploadLocInfo(){
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

    private void initialProgressDialog(){
        progDialog = new ProgressDialog(getActivity());
        progDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                stopUpdateLoad();
                isChated = false;
                handler.removeCallbacks(delayRun);
            }
        });
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isChated){
            isChated = false;
        }
        stopUpdateLoad();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopAMap();
    }

    private void stopUpdateLoad(){
        //停止定位
        mLocationClient.stopLocation();
        //用户信息清除后，将不会再被检索到，比如接单的美甲师下班后可以清除其位置信息。
        //获取附近实例，并设置要清楚用户的id
        NearbySearch.getInstance(getActivity()).setUserID("test_test_test_1");
        //调用异步清除用户接口
        NearbySearch.getInstance(getActivity())
                .clearUserInfoAsyn();
    }

    public void stopAMap(){
        //停止定位
        mLocationClient.stopLocation();
        //销毁定位客户端：
        //销毁定位客户端之后，若要重新开启定位请重新New一个AMapLocationClient对象。
        mLocationClient.onDestroy();

        //用户信息清除后，将不会再被检索到，比如接单的美甲师下班后可以清除其位置信息。
        //获取附近实例，并设置要清楚用户的id
        NearbySearch.getInstance(getActivity()).setUserID("test_test_test_1");
        //调用异步清除用户接口
        NearbySearch.getInstance(getActivity())
                .clearUserInfoAsyn();
        //在停止使用附近派单功能时，需释放资源。
        //调用销毁功能，在应用的合适生命周期需要销毁附近功能
        NearbySearch.destroy();
    }
}
