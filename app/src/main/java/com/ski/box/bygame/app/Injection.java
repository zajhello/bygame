package com.ski.box.bygame.app;

import android.content.Context;

import com.ski.box.bygame.data.DataRepository;
import com.ski.box.bygame.data.LocalDataSource;
import com.ski.box.bygame.data.RemoteDataSource;


/**
 * Created by Administrator on 2017/9/25.
 */

public class Injection {
    public static DataRepository provideTasksRepository(Context context) {
        return DataRepository.getInstance(RemoteDataSource.getInstance(), LocalDataSource.getInstance(context));
    }
}
