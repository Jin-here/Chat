package com.vgaw.rongyundemo.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.vgaw.rongyundemo.app.App;
import com.vgaw.rongyundemo.http.HttpRequest;

/**
 * Created by caojin on 15-10-22.
 */
public class BaseActivity extends FragmentActivity {
    protected App app;
    protected FragmentManager manager;
    protected HttpRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (App) getApplication();
        manager = getSupportFragmentManager();

        request = new HttpRequest();
    }
}
