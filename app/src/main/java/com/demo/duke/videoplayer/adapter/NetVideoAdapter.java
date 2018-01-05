package com.demo.duke.videoplayer.adapter;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.demo.duke.videoplayer.R;
import com.demo.duke.videoplayer.SurfaceViewMediaPlayerActivity;
import com.demo.duke.videoplayer.SystemVideoPlayer;
import com.demo.duke.videoplayer.domain.MediaItem;
import com.demo.duke.videoplayer.domain.OnlineMusic;

import java.io.IOException;
import java.util.List;

/**
 * Created by zhiwei.wang on 2018/1/2.
 * wechat 760560322
 * 作用：
 */

public class NetVideoAdapter<T> extends BaseAdapter {
    List<T> dataList;
    Context context;
    MediaPlayer mediaPlayer;
    int clickPosition = -1;

    public NetVideoAdapter(List<T> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
        // 初始化MediaPlayer
        mediaPlayer = new MediaPlayer();
        // 设置声音效果
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        // 设置媒体加载完成以后回调函数。
        mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
        // 错误监听回调函数
        mediaPlayer.setOnErrorListener(new MyOnErrorListener());

    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_media_video_pager, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }



        final MediaItem mediaItem = (MediaItem) dataList.get(position);
        if (clickPosition == position) {
            viewHolder.surfaceView.setVisibility(View.VISIBLE);
            viewHolder.videoCover.setVisibility(View.INVISIBLE);

            mediaPlayer.reset();

            try {
                mediaPlayer.setDisplay(viewHolder.surfaceView.getHolder());
                mediaPlayer.setDataSource(mediaItem.getData());
                mediaPlayer.prepareAsync();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {

            viewHolder.videoCover.setVisibility(View.VISIBLE);
            viewHolder.surfaceView.setVisibility(View.INVISIBLE);
        }
        viewHolder.videoplayImage.setTag(position);
        viewHolder.videoplayImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.videoCover.setVisibility(View.GONE);
                viewHolder.videoplayImage.setVisibility(View.INVISIBLE);
                // 设置显示到屏幕
                clickPosition = (Integer) v.getTag();
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();

                }
                notifyDataSetChanged();
            }
        });
        Object tag = viewHolder.videoplayImage.getTag();
        Log.e("adadadada",tag+"==="+clickPosition +"===="+ position);
        //判断正在播放的视频的item是否滑出屏幕
        if (tag != null) {
            Integer tag1 = (Integer) tag;
            if (tag1 == clickPosition && tag1 != position) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                clickPosition = -1;
                Log.e("adadadada",tag+"====================="+ position);
            }
        }

        return convertView;
    }


    class ViewHolder {
        ImageView imageViewPlayPause, videoCover, videoplayImage;
        SurfaceView surfaceView;

        public ViewHolder(View convertView) {
            imageViewPlayPause = convertView.findViewById(R.id.video_play_image);
            videoCover = convertView.findViewById(R.id.vidoe_cover);
            surfaceView = convertView.findViewById(R.id.surfaceview);
            videoplayImage = convertView.findViewById(R.id.video_play_image);
        }


    }


    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {
        //当底层解码完毕
        @Override
        public void onPrepared(MediaPlayer mp) {

            // 播放视频
            mp.start();


        }
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            switch (what) {
                case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                    Log.e("text", "发生未知错误");

                    break;
                case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                    Log.e("text", "媒体服务器死机");
                    break;
                default:
                    Log.e("text", "onError+" + what);
                    break;
            }
            switch (extra) {
                case MediaPlayer.MEDIA_ERROR_IO:
                    //io读写错误
                    Log.e("text", "文件或网络相关的IO操作错误");
                    break;
                case MediaPlayer.MEDIA_ERROR_MALFORMED:
                    //文件格式不支持
                    Log.e("text", "比特流编码标准或文件不符合相关规范");
                    break;
                case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                    //一些操作需要太长时间来完成,通常超过3 - 5秒。
                    Log.e("text", "操作超时");
                    break;
                case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                    //比特流编码标准或文件符合相关规范,但媒体框架不支持该功能
                    Log.e("text", "比特流编码标准或文件符合相关规范,但媒体框架不支持该功能");
                    break;
                default:
                    Log.e("text", "onError+" + extra);
                    break;
            }
            Toast.makeText(context, "出错了", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


}
