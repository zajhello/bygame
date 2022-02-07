package com.ski.box.bygame.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.ski.box.bygame.R;


/**
 * author: wuzongjie
 * time  : 2018/4/27 0027 15:05
 * desc  :
 */

public class LoadDialog extends Dialog{

    public LoadDialog(@NonNull Context context) {
        super(context, R.style.custom_dialog);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog);
    }
}
