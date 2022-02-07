package com.ski.box.bygame.utils.okhttp;


import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.appcompat.app.AlertDialog;

import com.ski.box.bygame.R;
import com.ski.box.bygame.app.MyApplication;
import com.ski.box.bygame.base.ActivityManage;
import com.ski.box.bygame.utils.WonderfulLogUtils;
import com.ski.box.bygame.utils.WonderfulToastUtils;

import java.io.IOException;
import java.net.UnknownHostException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/9/29.
 */

public abstract class StringCallback extends Callback<String> {
    @Override
    public String parseNetworkResponse(Response response) throws IOException {
        return response.body().string();
    }

    @Override
    public void onError(Request request, Exception e) {

        WonderfulLogUtils.logi("miao", e.toString().length() + "-666-");

        try {
            if (checkInternet()) {
                dialogT(MyApplication.app.getResources().getText(R.string.tv_no_network) + "");
                WonderfulToastUtils.showToast(MyApplication.app.getResources().getText(R.string.tv_examine_network) + "");
            }
        } catch (Resources.NotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private void dialogT(String title) {
//        if (MyApplication.getApp().typeBiaoshi == 0) {
//            MyApplication.getApp().typeBiaoshi = 1;
//            AlertDialog dialog = new AlertDialog.Builder(ActivityManage.activities.get(ActivityManage.activities.size() - 1), R.style.no_net_dialog)
//                    .setTitle(MyApplication.app.getResources().getText(R.string.Warm_prompt) + "")
//                    .setNegativeButton(MyApplication.app.getResources().getText(R.string.tv_quit) + "", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            MyApplication.getApp().typeBiaoshi = 0;
//                            dialog.cancel();
//                            System.exit(0);
//                        }
//                    }).create();
//            dialog.setMessage(title);
//            dialog.show();
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.setCancelable(false);
//        }
    }

    private boolean checkInternet() {
        ConnectivityManager cm = (ConnectivityManager) MyApplication.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        NetworkInfo.State mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if ((wifiState != null && wifiState == NetworkInfo.State.CONNECTED) || (mobileState != null && mobileState == NetworkInfo.State.CONNECTED)) {
            return true;
        }
        return false;
    }
}
