package com.demo.duke.videoplayer.pager;

import android.content.Context;
import android.view.View;

/**
 * Created by zhiwei.wang on 2017/12/26.
 * 基类
 */

public abstract class BasePager {
    public final Context context;
    private View rootView;
    public boolean isInitData;

    public BasePager(Context context) {
        this.context = context;
        rootView = initView();
    }

    // TODO: 2017/12/26

    /**
     * 强制子类实现，初始化页面
     * @return
     */

    public abstract View initView();

    // TODO: 2017/12/26

    /**
     * 初始化数据
     */
    public void initData(){};
}
