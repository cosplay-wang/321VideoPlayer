package com.demo.duke.videoplayer.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;


/**
 * Created by zhiwei.wang on 2017/12/26.
 */

public class VideoPager extends BasePager {
    public static final String TDD = "logbasepager";
    private TextView textView;

    public VideoPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        textView = new TextView(context);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);
        Log.e(TDD,"VideoPagerinintVIEW:" + isInitData);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("本地视频页面");
        Log.e(TDD,"VideoPagerinitData:"+ isInitData);

    }
}
