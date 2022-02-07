package com.ski.box.bygame.data;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface DataSource {

    interface DataCallback {

        void onDataLoaded(Object obj);

        void onDataNotAvailable(Integer code, String toastMessage);
    }
}
