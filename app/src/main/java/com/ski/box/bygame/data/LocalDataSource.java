package com.ski.box.bygame.data;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.io.File;
import java.math.BigDecimal;


/**
 * 当需要使用缓存等时 需要使用Local该类加载数据
 */

public class LocalDataSource implements DataSource {
    private static LocalDataSource INSTANCE;
    private Handler handler = new Handler(Looper.getMainLooper());

    public LocalDataSource(Context context) {
    }

    public static LocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocalDataSource(context);
        }
        return INSTANCE;
    }

}
