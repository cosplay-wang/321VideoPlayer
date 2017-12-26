package com.demo.duke.videoplayer.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by zhiwei.wang on 2017/12/26.
 */

public class NetVideoPager extends BasePager {
    private TextView textView;

    public NetVideoPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
         textView = new TextView(context);
         textView.setTextColor(Color.RED);
         textView.setTextSize(25);
         textView.setGravity(Gravity.CENTER);

        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("网络视频页面");
    }
}
