package com.vgaw.rongyundemo.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.vgaw.rongyundemo.R;
import com.vgaw.rongyundemo.protopojo.FlyCatProto;
import com.vgaw.rongyundemo.util.WarnFragmentHelper;

import io.rong.imkit.RongIM;

/**
 * Created by caojin on 15-10-21.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener{

    private EditText et_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_name = (EditText) findViewById(R.id.et_name);
        Button btn_private = (Button) findViewById(R.id.btn_private);
        Button btn_conversationList = (Button) findViewById(R.id.btn_conversationList);
        btn_private.setOnClickListener(this);
        btn_conversationList.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
        }
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
