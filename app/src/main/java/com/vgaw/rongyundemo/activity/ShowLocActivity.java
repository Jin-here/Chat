package com.vgaw.rongyundemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Marker;
import com.vgaw.rongyundemo.util.DataFactory;
import com.vgaw.rongyundemo.R;

/**
 * Created by caojin on 2016/2/13.
 */
public class ShowLocActivity extends Activity {
    private MapView mMapView;
    private AMap mAMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showloc);

        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        if (mAMap == null) {
            mAMap = mMapView.getMap();
            /*mAMap.getUiSettings().setMyLocationButtonEnabled(true);
            mAMap.getUiSettings().setScaleControlsEnabled(true);
            mAMap.getUiSettings().setCompassEnabled(true);*/

            updateMarkers();
            mAMap.setOnMarkerClickListener(onMarkerClickListener);

            DataFactory.getInstance().setOnLocUpdatedListener(new DataFactory.OnLocUpdatedListener() {
                @Override
                public void onLocUpdated() {
                    updateMarkers();
                }
            });
        }
    }

    private void updateMarkers(){
        /*ArrayList<NearbyInfo> nearbyInfoList = DataFactory.getInstance().getNearbyInfoList();
        if (nearbyInfoList == null){
            return;
        }
        ArrayList<MarkerOptions> markerOptionsList = new ArrayList<>(nearbyInfoList.size());
        for (NearbyInfo nearbyInfo : nearbyInfoList){
            if (DataFactory.getInstance().getUsername().equals(nearbyInfo.getUserID())){
                markerOptionsList.add(new MarkerOptions()
                        .title(nearbyInfo.getUserID())
                        .position(new LatLng(nearbyInfo.getPoint().getLatitude(), nearbyInfo.getPoint().getLongitude()))
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                continue;
            }
            markerOptionsList.add(new MarkerOptions()
                    .title(nearbyInfo.getUserID())
                    .position(new LatLng(nearbyInfo.getPoint().getLatitude(), nearbyInfo.getPoint().getLongitude()))
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }

        mAMap.addMarkers(markerOptionsList, true);*/
    }

    AMap.OnMarkerClickListener onMarkerClickListener = new AMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(final Marker marker) {
            marker.showInfoWindow();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    marker.hideInfoWindow();
                }
            }, 700);
            return true;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
