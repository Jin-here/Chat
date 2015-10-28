package com.vgaw.rongyundemo.util;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.vgaw.rongyundemo.fragment.WarnFragment;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by caojin on 15-10-22.
 */
public class WarnFragmentHelper {
    private int layoutId;
    private String warnInfo;
    private FragmentManager manager;

    public WarnFragmentHelper(FragmentManager manager, int layoutId, String warnInfo){
        this.manager = manager;
        this.layoutId = layoutId;
        this.warnInfo = warnInfo;
    }

    /**
     * show the warn animation.
     */
    public void warn(){
        final WarnFragment warnFragment = new WarnFragment();
        warnFragment.setWarnInfo(warnInfo);

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(layoutId, warnFragment);
        transaction.commit();

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                FragmentTransaction fragmentTransaction1 = manager.beginTransaction();
                fragmentTransaction1.remove(warnFragment);
                fragmentTransaction1.commit();
            }
        };
        timer.schedule(timerTask, 1000);
    }
}
