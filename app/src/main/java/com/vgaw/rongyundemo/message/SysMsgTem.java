package com.vgaw.rongyundemo.message;

import android.content.Context;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vgaw.rongyundemo.R;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;

/**
 * Created by caojin on 2016/3/1.
 */
@ProviderTag(messageContent = SystemMessage.class)
public class SysMsgTem extends IContainerItemProvider.MessageProvider<SystemMessage> {
    class ViewHolder {
        TextView tv_content;
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.msg_template, null);
        ViewHolder holder = new ViewHolder();
        holder.tv_content = (TextView) view.findViewById(R.id.tv_content);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, int i, SystemMessage systemMessage, UIMessage uiMessage) {
        ViewHolder holder = (ViewHolder) view.getTag();

        holder.tv_content.setText(systemMessage.getName() + " 请求加为好友");
    }

    @Override
    public Spannable getContentSummary(SystemMessage systemMessage) {
        return null;
    }

    @Override
    public void onItemClick(View view, int i, SystemMessage systemMessage, UIMessage uiMessage) {

    }

    @Override
    public void onItemLongClick(View view, int i, SystemMessage systemMessage, UIMessage uiMessage) {

    }
}
