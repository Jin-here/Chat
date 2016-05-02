package com.vgaw.rongyundemo.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;

import com.vgaw.rongyundemo.App;
import com.vgaw.rongyundemo.R;
import com.vgaw.rongyundemo.util.SystemBarTintManager;

/**
 * Created by caojin on 15-10-22.
 */
public class BaseActivity extends FragmentActivity {
    private SystemBarTintManager tintManager;

    protected App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (App) getApplication();
        // 给状态栏着色
        tintStatusBar();
    }

    protected void setStatusBarColor(int color){
        tintManager.setStatusBarTintColor(color);
    }

    private void tintStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            /*// 透明导航栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);*/
        }

        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(getResources().getColor(R.color.color_main));
    }
}
