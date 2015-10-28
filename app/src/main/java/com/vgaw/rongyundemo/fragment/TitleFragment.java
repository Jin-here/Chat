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
 * Created by caojin on 15-10-23.
 */
public class TitleFragment extends Fragment {
    public static final String TAG = "TITLEFRAGMENT";

    private String title;

    public TitleFragment(){}

    public static TitleFragment newInstance(String title) {

        Bundle args = new Bundle();
        args.putString("title", title);
        TitleFragment fragment = new TitleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title = getArguments() == null ? null : getArguments().getString("title");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.title_fragment, container, false);
        TextView tv_title = (TextView) layout.findViewById(R.id.tv_title);
        tv_title.setText(this.title);
        return layout;
    }
}
