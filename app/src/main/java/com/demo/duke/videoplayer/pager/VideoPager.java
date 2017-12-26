package com.demo.duke.videoplayer.pager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.demo.duke.videoplayer.R;
import com.demo.duke.videoplayer.domain.MediaItem;
import com.demo.duke.videoplayer.util.TimeUtil;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;


/**
 * Created by zhiwei.wang on 2017/12/26.
 */

public class VideoPager extends BasePager {
    public static final String TDD = "logbasepager";

    ListView listView;
    TextView textView;
    ProgressBar progressBar;
    private List<MediaItem> mediaItems = new ArrayList<>();
    VideoAdapter videoAdapter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mediaItems != null && mediaItems.size() > 0) {
                //有数据
                //设置文本，隐藏
                //设置适配器
                videoAdapter = new VideoAdapter();
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

    public VideoPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.listview, null);
        listView = view.findViewById(R.id.listview);
        textView = view.findViewById(R.id.tv_toast);
        progressBar = view.findViewById(R.id.pb_loading);
        return view;
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
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
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
                        mediaItems.add(mediaItem);

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

                    }
                    cursor.close();
                }
                //handler发消息
                handler.sendEmptyMessage(10);
            }
        }).start();
    }
    class VideoAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mediaItems==null?0:mediaItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mediaItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
             ViewHolder viewHolder;
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.item_media_pager,null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = convertView.findViewById(R.id.iv_icon);
                viewHolder.textViewName = convertView.findViewById(R.id.tv_name);
                viewHolder.textViewLong = convertView.findViewById(R.id.tv_durtion);
                viewHolder.textViewSize = convertView.findViewById(R.id.tv_size);
                convertView.setTag(viewHolder);

            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            MediaItem mediaItem = mediaItems.get(position);
            viewHolder.textViewName.setText(mediaItem.getName());
            viewHolder.textViewLong.setText(TimeUtil.getFormatedDateTime("HH:mm:ss",mediaItem.getDuration()));
            viewHolder.textViewSize.setText(android.text.format.Formatter.formatFileSize(context,mediaItem.getSize()));
            return convertView;
        }

    }
   static class ViewHolder{
        ImageView imageView;
        TextView textViewName;
        TextView textViewLong;
        TextView textViewSize;
    }
}
