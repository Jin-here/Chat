package com.vgaw.rongyundemo.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.vgaw.rongyundemo.R;
import com.vgaw.rongyundemo.fragment.ConversationListDynamicFragment;
import com.vgaw.rongyundemo.fragment.MapShowFragment;
import com.vgaw.rongyundemo.fragment.MeFragment;
import com.vgaw.rongyundemo.protopojo.FlyCatProto;
import com.vgaw.rongyundemo.util.WarnFragmentHelper;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * Created by caojin on 15-10-21.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener{

    private String[] iconContextList = new String[]{"首页", "会话", "我"};
    private int[] iconOrangeList = new int[]{R.drawable.home_orange, R.drawable.talklist_orange, R.drawable.me_orange};
    private int[] iconGrayList = new int[]{R.drawable.home_gray, R.drawable.talklist_gray, R.drawable.me_gray};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FragmentTabHost mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        Bundle bundle = new Bundle();
        bundle.putString("context", "fuck");
        mTabHost.addTab(mTabHost.newTabSpec("simple").setIndicator(getTabItemView(0)),
                MapShowFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("contacts").setIndicator(getTabItemView(1)),
                ConversationListDynamicFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("custom").setIndicator(getTabItemView(2)),
                MeFragment.class, null);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                View view = null;
                for (int i = 0;i < mTabHost.getTabWidget().getChildCount();i++){
                    view = mTabHost.getTabWidget().getChildTabViewAt(i);
                    ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                    TextView tv_icon = (TextView) view.findViewById(R.id.tv_icon);
                    if (mTabHost.getCurrentTab() == i){
                        iv_icon.setImageResource(iconOrangeList[i]);
                        tv_icon.setTextColor(getResources().getColor(R.color.color_main));
                    }else {
                        iv_icon.setImageResource(iconGrayList[i]);
                        tv_icon.setTextColor(getResources().getColor(R.color.gray));
                    }
                }
            }
        });
    }

    /**
     * 获取tabhost item布局
     * @param position
     * @retur
     */
    private View getTabItemView(int position){
        View view = getLayoutInflater().inflate(R.layout.tabhost_item, null);
        ImageView iv = (ImageView)view.findViewById(R.id.iv_icon);
        TextView tv = (TextView)view.findViewById(R.id.tv_icon);
        if (position != 0){
            iv.setImageResource(iconGrayList[position]);
            tv.setText(iconContextList[position]);
            tv.setTextColor(getResources().getColor(R.color.gray));
        }else {
            iv.setImageResource(iconOrangeList[position]);
            tv.setText(iconContextList[position]);
            tv.setTextColor(getResources().getColor(R.color.color_main));
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        /*switch (v.getId()){
            case R.id.btn_private:
                // query whether the user exist
                //      exist:start private talk
                //      not:warn the user
                new MyTask(et_name.getText().toString()).execute();
                break;
            case R.id.btn_conversationList:
                //启动会话列表界面
                if (RongIM.getInstance() != null)
                    RongIM.getInstance().startConversationList(this);
                break;
            case R.id.btn_chatroom:
                *//**
                 * 启动聊天室聊天界面。
                 *
                 * @param context          应用上下文。
                 * @param conversationType 开启会话类型。
                 * @param targetId         聊天室 Id。
                 * @param title            聊天的标题，如果传入空值，则默认显示会话的名称。
                 *//*
                RongIM.getInstance().startConversation(MainActivity.this, Conversation.ConversationType.CHATROOM, "9527", "标题");
                break;
        }*/
    }

    /*
    * 以会话页面的启动 Uri 为例说明：
    * rong://{packagename:应用包名}/conversation/[private|discussion|group]?targetId={目标Id}&[title={开启会话名称}]
    * 上面的例子，如果你的包名为 io.rong.imkit.demo，目标 Id 为 12345 的私聊会话，拼接后的 Uri 就是
    * rong://io.rong.imkit.demo/conversation/private?targetId=12345
    *
    *
    * */

    private class MyTask extends AsyncTask<Void, Void, Boolean>{
        private String name;
        private FlyCatProto.FlyCat response;

        public MyTask(String name){
            this.name = name;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean){
                /**
                 * 启动单聊
                 * context - 应用上下文。
                 * targetUserId - 要与之聊天的用户 Id。
                 * title - 聊天的标题，如果传入空值，则默认显示与之聊天的用户名称。
                 */
                //启动会话界面
                if (RongIM.getInstance() != null)
                    RongIM.getInstance().startPrivateChat(MainActivity.this, response.getStringV(1), response.getStringV(1));
            }else{
                new WarnFragmentHelper(manager, R.id.warn_fragment, getResources().getString(R.string.username_dont_exist)).warn();
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            response = request.requestForResult(FlyCatProto.FlyCat.newBuilder().setFlag(4).addStringV(name).build());
            return response.getBoolV(0);
        }
    }


}
