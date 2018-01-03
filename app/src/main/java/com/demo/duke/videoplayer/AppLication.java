package com.demo.duke.videoplayer;

import android.app.Application;
import android.content.Context;

/**
 * Created by zhiwei.wang on 2018/1/2.
 * wechat 760560322
 * 作用：
 */

public class AppLication extends Application{
    public static Context context = null;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
