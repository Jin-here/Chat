package com.vgaw.rongyundemo.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vgaw.rongyundemo.R;
import com.vgaw.rongyundemo.activity.AboutActivity;
import com.vgaw.rongyundemo.activity.FeedbackActivity;
import com.vgaw.rongyundemo.activity.LoginActivity;
import com.vgaw.rongyundemo.activity.MainActivity;
import com.vgaw.rongyundemo.util.DataFactory;

import io.rong.imlib.RongIMClient;

/**
 * Created by caojin on 2016/2/20.
 */
public class MeFragment extends Fragment{
    private Bundle data = null;

    public MeFragment(){}

    public static MeFragment newInstance(Bundle data) {
        MeFragment fragment = new MeFragment();
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.me_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (data != null){
            boolean isMe = data.getBoolean("isMe");
            View rl_back = view.findViewById(R.id.rl_back);
            if (!isMe){
                rl_back.setOnClickListener(listener);
            }else {
                rl_back.setVisibility(View.GONE);
            }
            ((TextView)view.findViewById(R.id.tv_title)).setText(isMe ? "我" : "TA");
            ((TextView)view.findViewById(R.id.tv_name)).setText(data.getString("name"));
            view.findViewById(R.id.layout_me).setVisibility(data.getBoolean("isMe") ? View.VISIBLE : View.GONE);
        }

        view.findViewById(R.id.tv_feedback).setOnClickListener(listener);
        view.findViewById(R.id.tv_logout).setOnClickListener(listener);
        view.findViewById(R.id.tv_about).setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rl_back:
                    getActivity().finish();
                    break;
                case R.id.tv_set:
                    break;
                case R.id.tv_feedback:
                    startActivity(new Intent(getActivity(), FeedbackActivity.class));
                    break;
                case R.id.tv_about:
                    startActivity(new Intent(getActivity(), AboutActivity.class));
                    break;
                case R.id.tv_logout:
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("确认退出？");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 断开融云连接
                            RongIMClient.getInstance().disconnect();
                            // 清空缓存
                            DataFactory.getInstance().setId(-1);
                            DataFactory.getInstance().setUsername(null);
                            DataFactory.getInstance().setPwd(null);
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            getActivity().finish();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                    break;
            }
        }
    };
}
