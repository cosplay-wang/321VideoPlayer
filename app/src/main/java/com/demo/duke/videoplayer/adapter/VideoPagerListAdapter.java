package com.demo.duke.videoplayer.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.duke.videoplayer.R;
import com.demo.duke.videoplayer.domain.MediaItem;
import com.demo.duke.videoplayer.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhiwei.wang on 2017/12/27.
 * wechat 760560322
 * 作用：videopager 中listview的adapter
 *
 */

public class VideoPagerListAdapter extends BaseAdapter{
    List<MediaItem> mediaItems = new ArrayList<>();
    Context context;


    public VideoPagerListAdapter(List<MediaItem> mediaItems, Context context) {
        this.mediaItems = mediaItems;
        this.context = context;
    }

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
        if(mediaItem!=null) {
            viewHolder.textViewName.setText(mediaItem.getName());
            viewHolder.textViewLong.setText(TimeUtil.getFormatedDateTime("HH:mm:ss", mediaItem.getDuration()));
            viewHolder.textViewSize.setText(android.text.format.Formatter.formatFileSize(context, mediaItem.getSize()));
        }else{
            viewHolder.textViewName.setText("mediaItem.getName()");
            viewHolder.textViewLong.setText("ddddddddd");
            viewHolder.textViewSize.setText("daaaaaaaaa");
        }
        return convertView;
    }

}
 class ViewHolder{
    ImageView imageView;
    TextView textViewName;
    TextView textViewLong;
    TextView textViewSize;

}
