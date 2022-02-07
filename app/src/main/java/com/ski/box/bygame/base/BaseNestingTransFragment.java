package com.ski.box.bygame.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Created by Administrator on 2018/3/30.
 */

public abstract class BaseNestingTransFragment extends BaseTransFragment {
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) addFragments();
        super.onViewCreated(view, savedInstanceState);
    }

    protected abstract void addFragments();

    protected abstract void recoveryFragments();

}
