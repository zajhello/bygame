package com.ski.box.bygame.base;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gyf.barlibrary.ImmersionBar;
import com.ski.box.bygame.R;
import com.ski.box.bygame.app.MyApplication;
import com.ski.box.bygame.utils.SharedPreferenceInstance;
import com.ski.box.bygame.utils.WonderfulKeyboardUtils;
import com.ski.box.bygame.utils.WonderfulLogUtils;
import com.ski.box.bygame.utils.WonderfulStringUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseActivity extends AppCompatActivity {
    private PopupWindow loadingPopup;
    protected View permissionView;
    protected View notLoginView;
    private Unbinder unbinder;
    protected ImmersionBar immersionBar;
    protected boolean isSetTitle = false;
    private TextView tvNote;
    private ViewGroup emptyView;
    boolean lockOpen = false;
    protected boolean isNeedChecke = true;// 解锁界面 是不需要判断的 用此变量控制
    protected boolean isNeedhide = true;

    protected boolean isSupportEventBus = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFlag();
        setContentView(getActivityLayoutId());
        unbinder = ButterKnife.bind(this);
        ActivityManage.addActivity(this);
        emptyView = getEmptyView();
        initLoadingPopup();
        if (isImmersionBarEnabled()) initImmersionBar();
        initViews(savedInstanceState);
        obtainData();
        fillWidget();
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        });

        //初始化PreferenceUtil
        PreferenceUtil.init(this);
        //根据上次的语言设置，重新设置语言
        switchLanguage(PreferenceUtil.getString("language", "zh"));

        if (isSupportEventBus && !EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    protected void switchLanguage(String language) {
        //设置应用语言类型
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (language.equals("en")) {
            config.locale = Locale.ENGLISH;
        } else if (language.equals("ch")) {
            config.locale = Locale.SIMPLIFIED_CHINESE;
        } else if (language.equals("de")) {
            config.locale = Locale.GERMAN;
        } else if (language.equals("fa")) {
            config.locale = Locale.FRENCH;
        } else if (language.equals("ja")) {
            config.locale = Locale.JAPANESE;
        } else if (language.equals("ko")) {
            config.locale = Locale.KOREAN;
        } else if (language.equals("tr")) {
            config.locale = new Locale("tr", "");
        } else if (language.equals("th")) {
            config.locale = new Locale("th", "");
        }
        resources.updateConfiguration(config, dm);

        //保存设置语言的类型
        PreferenceUtil.commitString("language", language);
    }

    public static class PreferenceUtil {
        protected static SharedPreferences mSharedPreferences = null;
        private static SharedPreferences.Editor mEditor = null;

        public static void init(Context context) {
            if (null == mSharedPreferences) {
                mSharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
            }
        }

        public static String getString(String key, String faillValue) {
            return mSharedPreferences.getString(key, faillValue);
        }

        public static void commitString(String key, String value) {
            mEditor = mSharedPreferences.edit();
            mEditor.putString(key, value);
            mEditor.commit();
        }
    }

    protected void setFlag() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        WonderfulLogUtils.loge("当前Activity", this.getLocalClassName());
        if (isNeedShowLockActivity()) {

        }


    }

    protected boolean isNeedShowLockActivity() {
        boolean isLogin = MyApplication.getApp().isLogin();//登录否？
        lockOpen = !WonderfulStringUtils.isEmpty(SharedPreferenceInstance.getInstance().getLockPwd());//开启否？
        boolean b = SharedPreferenceInstance.getInstance().getIsNeedShowLock();//是否处于后台过？
        return isLogin && isNeedChecke && lockOpen && b;
    }


    /**
     * 子类重写实现扩展设置
     */
    protected void initImmersionBar() {
        immersionBar = ImmersionBar.with(this);
        immersionBar.keyboardEnable(false, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN).statusBarDarkFont(false, 0.2f).flymeOSStatusBarFontColor(R.color.bgBlue).init();
    }

    /**
     * 获取布局ID
     */
    protected abstract int getActivityLayoutId();

    /**
     * 获取空布局的父布局
     */
    protected ViewGroup getEmptyView() {
        return null;
    }

    /**
     * 是否启用沉浸式
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 初始化工作
     *
     * @param savedInstanceState
     */
    protected abstract void initViews(Bundle savedInstanceState);

    /**
     * 获取本地或传递的数据
     */
    protected abstract void obtainData();

    /**
     * 控件填充
     */
    protected abstract void fillWidget();

    /**
     * 初始数据加载
     */
    protected abstract void loadData();

    @Override
    protected void onStop() {
        super.onStop();
        lockOpen = !WonderfulStringUtils.isEmpty(SharedPreferenceInstance.getInstance().getLockPwd());
        if (lockOpen) {
            if (!isAppOnForeground())
                SharedPreferenceInstance.getInstance().saveIsNeedShowLock(true);
            else
                SharedPreferenceInstance.getInstance().saveIsNeedShowLock(false);
        }


    }

    /**
     * 程序是否在前台运行
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        ActivityManage.removeActivity(this);
        hideLoadingPopup();
        if (immersionBar != null) immersionBar.destroy();

        if (isSupportEventBus && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 初始化加载dialog
     */
    private void initLoadingPopup() {
        View loadingView = getLayoutInflater().inflate(R.layout.pop_loading, null);
        loadingPopup = new PopupWindow(loadingView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        loadingPopup.setFocusable(true);
        loadingPopup.setClippingEnabled(false);
        loadingPopup.setBackgroundDrawable(new ColorDrawable());
    }

    /**
     * 显示加载框
     */
    public void displayLoadingPopup() {
        loadingPopup.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    /**
     * 隐藏加载框
     */
    public void hideLoadingPopup() {
        if (loadingPopup != null) {
            loadingPopup.dismiss();
        }
    }


    /**
     * 获取用户token
     */
    public String getToken() {
        return "";
    }

    /**
     * 处理软件盘智能弹出和隐藏
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                View view = getCurrentFocus();
                if (isNeedhide) {
                    WonderfulKeyboardUtils.editKeyboard(ev, view, this);
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }


}
