package com.vgaw.rongyundemo.view;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vgaw.rongyundemo.R;

/**
 * Created by caojin on 2016/2/26.
 */
public class MyToast extends Toast {
    private long DELAY = 1000;
    private Context context;
    private long currentTime = 0;

    private static MyToast instance = null;
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public MyToast(Context context) {
        super(context);
        this.context = context;
    }

    private MyToast initialView(String text){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.toast, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ((TextView)view.findViewById(R.id.tv_context)).setText(text);
        setView(view);
        return this;
    }

    public synchronized static MyToast makeText(Context context, String text){
        if (instance == null){
            instance = new MyToast(context);
        }
        return instance.initialView(text);
    }

    @Override
    public void show() {
        long tempTime = System.currentTimeMillis();
        if (tempTime - currentTime > DELAY){
            super.show();
            currentTime = tempTime;
        }else {
            return;
        }
    }
}
