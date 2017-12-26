package com.demo.duke.videoplayer.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.demo.duke.videoplayer.R;

/**
 * Created by zhiwei.wang on 2017/12/26.
 */

public class TitleBar extends LinearLayout implements View.OnClickListener {
    private View textView;
    private View relativeLayout;
    private View imageView;
    private  Context context;
    public TitleBar(Context context) {
        this(context, null);
        // TODO: 2017/12/26   直接new的时候 
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);// TODO: 2017/12/26 在xml中使用该类时调用的构造方法,反射的方式
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        // TODO: 2017/12/26  需要自定义样式的时候
        //得到孩子的实例


    }

    /**
     * 当布局文件加载完成，回调该方法
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        textView = getChildAt(1);
        relativeLayout = getChildAt(2);
        imageView = getChildAt(3);
        textView.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search:
                Toast.makeText(context,"s搜索",Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_game:
                Toast.makeText(context,"s游戏",Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_clear:
                Toast.makeText(context,"s历史",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
