package com.vgaw.rongyundemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vgaw.rongyundemo.R;

/**
 * Created by caojin on 15-10-22.
 */
public class WarnFragment extends Fragment {
    public static final String TAG = "WARNFRAGMENT";

    private TextView tv_warn;
    private String warnInfo;

    public WarnFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.warn_fragment, container, false);
        tv_warn = (TextView) layout.findViewById(R.id.tv_warn);
        tv_warn.setText(warnInfo);
        return layout;
    }

    /**
     * set the warn info that shown to the user.
     */
    public void setWarnInfo(String warnInfo){
        this.warnInfo = warnInfo;
    }

    /**
     * update the warn info.
     * @param warnInfo
     */
    public void updateWarnInfo(String warnInfo){
        tv_warn.setText(warnInfo);
    }
}
