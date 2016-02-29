package com.vgaw.rongyundemo.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.vgaw.rongyundemo.App;

/**
 * Created by caojin on 15-10-22.
 */
public class BaseActivity extends FragmentActivity {
    protected App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (App) getApplication();
    }
}
