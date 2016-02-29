package com.vgaw.rongyundemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.vgaw.rongyundemo.R;
import com.vgaw.rongyundemo.fragment.MeFragment;

/**
 * Created by caojin on 2016/2/29.
 */
public class MeActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_activity);

        Intent intent = getIntent();
        if(intent != null){
            getSupportFragmentManager().beginTransaction().add(R.id.container, MeFragment.newInstance(intent.getBundleExtra("data")), "mefragment").commit();
        }
    }
}
