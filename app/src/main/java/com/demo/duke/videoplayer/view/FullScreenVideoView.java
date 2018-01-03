package com.demo.duke.videoplayer.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;
import android.widget.VideoView;

/**
 * Created by zhiwei.wang on 2018/1/3.
 * wechat 760560322
 * 作用：
 */

public class FullScreenVideoView extends VideoView {
    Context context;
    boolean params;

    public FullScreenVideoView(Context context) {
        super(context);
        init(context);
    }

    public FullScreenVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FullScreenVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int width = getDefaultSize(0, widthMeasureSpec);//得到默认的大小（0，宽度测量规范）
//        int height = getDefaultSize(0, heightMeasureSpec);//得到默认的大小（0，高度度测量规范）
//        setMeasuredDimension(width, height); //设置测量尺寸,将高和宽放进去
    }

    /**
     * 设置videiview的全屏和窗口模式
     *
     * @param paramsType 标识 1为全屏模式 2为窗口模式
     */
    public void setVideoViewLayoutParams(int paramsType) {
        if (paramsType == 1) {
            params = true;
        } else {
            params = false;
        }
        ;
//全屏模式
        if (1 == paramsType) {
//设置充满整个父布局
            RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//设置相对于父布局四边对齐
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//为VideoView添加属性
            setLayoutParams(LayoutParams);
        } else {
//窗口模式
            //获取整个屏幕的宽高
            DisplayMetrics DisplayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(DisplayMetrics);
//设置窗口模式距离边框50
            int videoHeight = DisplayMetrics.heightPixels - 150;
            int videoWidth = DisplayMetrics.widthPixels - 150;
            RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(videoWidth, videoHeight);
//设置居中
            LayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//为VideoView添加属性
            setLayoutParams(LayoutParams);
        }
    }

    public boolean getParams() {
        return params;
    }


}
