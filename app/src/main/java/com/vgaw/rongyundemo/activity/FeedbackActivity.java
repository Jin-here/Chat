package com.vgaw.rongyundemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.vgaw.rongyundemo.R;

/**
 * Created by caojin on 2016/5/2.
 */
public class FeedbackActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        ((TextView)findViewById(R.id.tv_title)).setText("帮助与反馈");
        findViewById(R.id.rl_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
