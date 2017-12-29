package com.demo.duke.videoplayer.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.duke.videoplayer.pager.BasePager;

/**
 * Created by zhiwei.wang on 2017/12/26.
 */

public class BaseFragment extends Fragment {
    private BasePager basePager;
    private boolean hasLoadData;

    public BaseFragment() {
    }

    public BaseFragment(BasePager basePager) {
        this.basePager = basePager;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (basePager != null) {
            return basePager.initView();
        }
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (basePager != null && !hasLoadData) {
            basePager.initData();
        }
    }
}
