package com.demo.duke.videoplayer.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import static com.demo.duke.videoplayer.pager.VideoPager.TDD;

/**
 * Created by zhiwei.wang on 2017/12/26.
 */

public class AudioPager extends BasePager {
    private TextView textView;

    public AudioPager(Context context) {
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
        textView.setText("本地音频页面");
    }
}
