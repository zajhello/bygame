package com.ski.box.bygame.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.view.View;
import android.widget.TextView;


import androidx.multidex.MultiDex;

import com.ski.box.bygame.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


/**
 * Created by pc on 2017/3/8.
 */
public class MyApplication extends Application {
    /**
     * 是否发布了
     */
    private boolean isReleased = false;

    /**
     * 当前是否有网络
     */
    private boolean isConnect = false;

    /**
     * 当前用户信息是否发生改变
     */
    private boolean isLoginStatusChange = false;


    public static MyApplication app;

    /**
     * WonderfulToastView
     */
    private TextView tvToast;
    /**
     * 当前手机屏幕的宽高
     */
    private int mWidth;
    private int mHeight;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initView();

        getDisplayMetric();
        checkInternet();
    }


    private void initView() {
        tvToast = (TextView) View.inflate(app, R.layout.my_toast, null);
    }

    public boolean isLogin() {
        return  false;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    /**
     * 检查是否有网络
     */
    private void checkInternet() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        NetworkInfo.State mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if ((wifiState != null && wifiState == NetworkInfo.State.CONNECTED) || (mobileState != null && mobileState == NetworkInfo.State.CONNECTED)) {
            isConnect = true;
        }
    }

    /**
     * 获取屏幕的宽高
     */
    private void getDisplayMetric() {
        mWidth = getResources().getDisplayMetrics().widthPixels;
        mHeight = getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取程序的Application对象
     */
    public static MyApplication getApp() {
        return app;
    }





    public boolean isReleased() {
        return isReleased;
    }

    public int getmWidth() {
        return mWidth;
    }

    public int getmHeight() {
        return mHeight;
    }

    public TextView getTvToast() {
        return tvToast;
    }

    public boolean isLoginStatusChange() {
        return isLoginStatusChange;
    }

    public void setLoginStatusChange(boolean loginStatusChange) {
        isLoginStatusChange = loginStatusChange;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }






}
