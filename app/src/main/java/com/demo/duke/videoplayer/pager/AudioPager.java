package com.demo.duke.videoplayer.pager;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.duke.videoplayer.R;
import com.demo.duke.videoplayer.SystemAudioPlayer;
import com.demo.duke.videoplayer.adapter.VideoPagerListAdapter;
import com.demo.duke.videoplayer.domain.MediaItem;
import com.demo.duke.videoplayer.service.MusicPlayCenter;
import com.demo.duke.videoplayer.service.PlayMusicService;

import java.util.ArrayList;
import java.util.List;

import static com.demo.duke.videoplayer.pager.VideoPager.TDD;

/**
 * Created by zhiwei.wang on 2017/12/26.
 */

public class AudioPager extends BasePager {
    public static final String TDD = "logbasepager";

    ListView listView;
    TextView textView;
    ProgressBar progressBar;
    private List<MediaItem> mediaItems = new ArrayList<>();
    VideoPagerListAdapter videoAdapter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mediaItems != null && mediaItems.size() > 0) {
                //有数据
                //设置文本，隐藏
                //设置适配器
                Log.e("ddddddddddffff", mediaItems.size() + "----------");
                Log.e("ddddddddddffff", mediaItems.size() + "----------" + mediaItems.get(0).toString());
                MusicPlayCenter.getMusicPlayCenter().getMediaItemList().clear();
                MusicPlayCenter.getMusicPlayCenter().getMediaItemList().addAll(mediaItems);
                videoAdapter = new VideoPagerListAdapter(mediaItems, context);
                listView.setAdapter(videoAdapter);
                textView.setVisibility(View.GONE);
            } else {
                //没有数据
                //文本显示
                textView.setVisibility(View.VISIBLE);
                textView.setText("暂无数据");
            }
            progressBar.setVisibility(View.GONE);
        }
    };

    public AudioPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.listview, null);
        listView = view.findViewById(R.id.listview);
        textView = view.findViewById(R.id.tv_toast);
        progressBar = view.findViewById(R.id.pb_loading);
        listView.setOnItemClickListener(new AudioPager.MyOnItemClickListener());
        return view;
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MediaItem mediaItem = mediaItems.get(position);
            if (MusicPlayCenter.getMusicPlayCenter().getPlayMusicService() != null) {
                MusicPlayCenter.getMusicPlayCenter().getPlayMusicService().play(position);
            }


            // TODO: 2017/12/27  调启 系统所有的播放器--隐式意图
//            Intent intent = new Intent();
//            intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
//            context.startActivity(intent);
            // TODO: 2017/12/27 调用自己写的播放器 --显示意图  MediaPlayer 和 VideoView
//            Intent intent = new Intent(context,SystemAudioPlayer.class);
//            intent.putExtra("mediaItem",mediaItem);
//            context.startActivity(intent);
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
        //加载本地视频数据
        getDataFromLocal();

    }

    /**
     * 从本地得到数据
     * 1.遍历sdCard,后缀名（慢）
     * 2.从内容提供者获取数据
     * 3.如果是6.0 动态权限
     */
    private void getDataFromLocal() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentResolver resolver = context.getContentResolver();
                // Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Video.Media.DISPLAY_NAME,//名称
                        MediaStore.Video.Media.DURATION,//时长
                        MediaStore.Video.Media.SIZE,//大小
                        MediaStore.Video.Media.DATA,//绝对地址
                        MediaStore.Video.Media.ARTIST,//歌曲演唱者
                };
                Cursor cursor = resolver.query(uri, objs, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        MediaItem mediaItem = new MediaItem();


                        String name = cursor.getString(0);
                        mediaItem.setName(name);

                        long duration = cursor.getLong(1);
                        mediaItem.setDuration(duration);

                        long size = cursor.getLong(2);
                        mediaItem.setSize(size);

                        String data = cursor.getString(3);
                        mediaItem.setData(data);

                        String artist = cursor.getString(4);
                        mediaItem.setArtist(artist);
                        Log.e("opop", mediaItem.toString());
                        mediaItems.add(mediaItem);
                        Log.e("opop", mediaItem.toString() + "---------");
                    }
                    cursor.close();
                    //handler发消息
                    handler.sendEmptyMessage(10);
                }

            }
        }).start();
    }

    @Override
    public void clearData() {
        super.clearData();
        mediaItems.clear();
    }
}
