package com.demo.duke.videoplayer.pager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import com.demo.duke.videoplayer.adapter.NetAudioAdapter;
import com.demo.duke.videoplayer.adapter.VideoPagerListAdapter;
import com.demo.duke.videoplayer.domain.DownloadInfo;
import com.demo.duke.videoplayer.domain.MediaItem;
import com.demo.duke.videoplayer.domain.OnlineMusic;
import com.demo.duke.videoplayer.http.HttpUtils;
import com.demo.duke.videoplayer.http.OkhttpHeader;
import com.demo.duke.videoplayer.util.JsonUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;

/**
 * Created by zhiwei.wang on 2017/12/26.
 */

public class NetAudioPager extends BasePager {
    private static final String ERRORLOG = "TAGGGG";
    ListView listView;
    TextView textView;
    ProgressBar progressBar;
    private List<OnlineMusic> mediaItems = new ArrayList<>();
    private final static String BASEURL = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.billboard.billList&type=1&size=110&offset=0";
    private final static String GETONLINEMP3 = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.song.play";
    MediaItem playMediaItem = new MediaItem();
    public NetAudioPager(Context context) {
        super(context);
    }
    NetAudioAdapter  netAudioAdapter;
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
            OnlineMusic mediaItem = mediaItems.get(position);
            playMediaItem.setName(mediaItem.getTitle());
            playMediaItem.setArtist(mediaItem.getArtist_name());
            doDownloadOnlineMusicPath(mediaItem);
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
        doData();
    }

    private void doData() {
        DisposableObserver<List<OnlineMusic>> observer = new DisposableObserver<List<OnlineMusic>>() {
            @Override
            public void onNext(List<OnlineMusic> value) {
                mediaItems.addAll(value);
                netAudioAdapter = new NetAudioAdapter(mediaItems,context);
                listView.setAdapter(netAudioAdapter);
                textView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onComplete() {
                progressBar.setVisibility(View.GONE);
            }
        };
        Observable<List<OnlineMusic>> observable = Observable.create(
                new ObservableOnSubscribe<List<OnlineMusic>>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<List<OnlineMusic>> rx) throws Exception {
                        getdata(BASEURL, rx);
                    }
                });
        observable.onTerminateDetach()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);

    }

    public void getdata(String url, final ObservableEmitter<List<OnlineMusic>> rx) throws Exception {

        Response response = null;
        response = HttpUtils.okHttpRequest(url, null, null);
        if (response.isSuccessful()) {
            String responses = response.body().string();
            JSONObject jsonObject = new JSONObject(responses);
            String listString = jsonObject.getString("song_list");
            List<OnlineMusic> onlineMusicList = JsonUtil.jsonToList(listString,OnlineMusic.class);
            rx.onNext(onlineMusicList);
        } else {
            rx.onError(new NullPointerException("错误返回码：" + response.code()));
        }

    }
    private void doDownloadOnlineMusicPath(final OnlineMusic onlineMusic) {
        DisposableObserver<DownloadInfo> observer = new DisposableObserver<DownloadInfo>() {
            @Override
            public void onNext(DownloadInfo value) {
                playMediaItem.setData(value.getBitrate().getFile_link());
                playMediaItem.setDuration(value.getBitrate().getFile_duration() * 1000);
                // TODO: 2017/12/27 调用自己写的播放器 --显示意图  MediaPlayer 和 VideoView
                Intent intent = new Intent(context,SystemAudioPlayer.class);
                intent.setDataAndType(Uri.parse(playMediaItem.getData()),"video/*");
                context.startActivity(intent);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        Observable<DownloadInfo> observable = Observable.create(
                new ObservableOnSubscribe<DownloadInfo>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<DownloadInfo> rx) throws Exception {
                           downloadOnlineMusicPath(GETONLINEMP3,onlineMusic.getSong_id(),rx);
                    }
                });
        observable.onTerminateDetach()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);

    }

    public void downloadOnlineMusicPath(String url,String songid, final ObservableEmitter<DownloadInfo> rx) throws Exception {
        url = url +"&songid=" + songid;
        Response response = null;
        response = HttpUtils.okHttpRequest(url, null, null);
        if (response.isSuccessful()) {
            String responses = response.body().string();
            DownloadInfo downloadInfo = JsonUtil.GsonToBean(responses,DownloadInfo.class);
            rx.onNext(downloadInfo);
        } else {
            rx.onError(new NullPointerException("错误返回码：" + response.code()));
        }

    }

}
