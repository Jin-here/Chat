package com.vgaw.rongyundemo.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ant.liao.GifView;
import com.vgaw.rongyundemo.R;

/**
 * Created by caojin on 2016/2/28.
 */
public class Loading {
    private static Dialog loading_dialog = null;

    public static Dialog getInstance(Context mContext) {
        if (loading_dialog == null) {
            loading_dialog = new Dialog(mContext, R.style.loading_dialog);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.loading, null);
            GifView gf_loading = ((GifView) view.findViewById(R.id.gf_loading));
            gf_loading.setGifImage(R.drawable.loading);
            gf_loading.setGifImageType(GifView.GifImageType.WAIT_FINISH);

            loading_dialog.setContentView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            loading_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    loading_dialog = null;
                }
            });
        }
        return loading_dialog;
    }

}
