package com.vgaw.rongyundemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vgaw.rongyundemo.R;
import com.vgaw.rongyundemo.http.HttpCat;
import com.vgaw.rongyundemo.protopojo.FlyCatProto;
import com.vgaw.rongyundemo.util.DataFactory;
import com.vgaw.rongyundemo.view.MyToast;

import java.util.ArrayList;

import io.rong.imkit.RongIM;

/**
 * Created by caojin on 2016/2/29.
 */
public class FriendFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.friend_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView) view.findViewById(R.id.tv_title)).setText("好友");
        final ListView lv_friend = (ListView) view.findViewById(R.id.lv_friend);
        HttpCat.fly(FlyCatProto.FlyCat.newBuilder()
                .setFlag(7)
                .addStringV(DataFactory.getInstance().getUsername()).build(), new HttpCat.AbstractResponseListener() {
            @Override
            public void onSuccess(FlyCatProto.FlyCat flyCat) {
                if (flyCat.getFlag() == 1) {
                    final ArrayList<String> friendList = new ArrayList<String>();
                    for (int i = 0; i < flyCat.getStringVCount(); i++) {
                        friendList.add(flyCat.getStringV(i));
                    }
                    DataFactory.getInstance().setFriendList(friendList);
                    lv_friend.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.friend_item, R.id.tv_name, friendList));
                    lv_friend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            RongIM.getInstance().startPrivateChat(getActivity(), friendList.get(position), friendList.get(position));
                        }
                    });
                } else {
                    MyToast.makeText(getActivity(), "哎呀，您吓到我了，请慢点来");
                }
            }
        });
    }
}
