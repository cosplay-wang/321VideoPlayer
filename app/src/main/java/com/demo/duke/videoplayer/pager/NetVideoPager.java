package com.demo.duke.videoplayer.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.demo.duke.videoplayer.R;
import com.demo.duke.videoplayer.adapter.ListViewVideoAdapter;
import com.demo.duke.videoplayer.adapter.NetVideoAdapter;
import com.demo.duke.videoplayer.domain.MediaItem;
import com.demo.duke.videoplayer.domain.OnlineMusic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhiwei.wang on 2017/12/26.
 */

public class NetVideoPager extends BasePager {

    public NetVideoPager(Context context) {
        super(context);
    }
    ListView listView;
    TextView textView;
    ProgressBar progressBar;
    List<MediaItem> mediaItemList = new ArrayList<>();
    NetVideoAdapter netVideoAdapter;
    ListViewVideoAdapter listViewVideoAdapter;
    @Override
    public View initView() {

        View view = View.inflate(context, R.layout.listview, null);
        listView = view.findViewById(R.id.listview);
        textView = view.findViewById(R.id.tv_toast);
        progressBar = view.findViewById(R.id.pb_loading);
        listView.setOnItemClickListener(new MyOnItemClickListener());
        return view;

    }
    class MyOnItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            /**
             * Android 系统中提供开发者开发多媒体应用（音视频）
             * MediaPlayer 和 VideoView
             *
             * MediaPlayer
             *
             * 负责和底层打交道   解码的是jni的底层库  封装了start  pause  stop等方法  都是native的方法
             * 播放视频的类
             * 可以播放本地和网络的音视频
             * 播放网络要加网络权限
             * 1.执行流程
             * 2.视频支持的格式  mp4(pc的mp4  不一定能播   码率低码率可以播base   high不行)  3gp  .m3u8
             *
             * VideoView
             * 用来显示视频的，继承了surfaceview 实现了videoControl 接口，封装了MediaPlayer
             * start  stop   等方法  本质上是调用mediaplayer的方法
             *
             * surfaceview
             * 默认使用双缓冲技术  支持在子线程绘制图像  ，不会阻塞主线程，适合游戏和视频的开发
             *
             * 实现了videoControl 规范，便于控制面板调用videoview的方法
             */
            /**
             * 视频的原理  每秒24帧
             */
        }
    }
    @Override
    public void initData() {
        super.initData();
        getData();
        netVideoAdapter = new NetVideoAdapter(mediaItemList,context);
        listView.setAdapter(netVideoAdapter);
//        listViewVideoAdapter = new ListViewVideoAdapter(mediaItemList,context);
//        listView.setAdapter(listViewVideoAdapter);
        progressBar.setVisibility(View.GONE);
    }

    private void getData() {
        for(int i=0;i<20;i++){
            MediaItem mediaItem = new MediaItem();
            //"http://2449.vod.myqcloud.com/2449_43b6f696980311e59ed467f22794e792.f20.mp4";
            mediaItem.setData("http://ips.ifeng.com/video19.ifeng.com/video09/2017/05/24/4664192-102-008-1012.mp4");
            mediaItemList.add(mediaItem);
        }
    }

}
