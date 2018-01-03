package com.demo.duke.videoplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.demo.duke.videoplayer.R;
import com.demo.duke.videoplayer.domain.MediaItem;
import com.demo.duke.videoplayer.domain.OnlineMusic;
import com.demo.duke.videoplayer.util.TimeUtil;

import java.util.List;

/**
 * Created by zhiwei.wang on 2018/1/2.
 * wechat 760560322
 * 作用：
 */

public class NetAudioAdapter<T> extends BaseAdapter {
    List<T> dataList;
    Context context;

    public NetAudioAdapter(List<T> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataList == null?0:dataList.size();
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

        OnlineMusic onlineMusic = (OnlineMusic) dataList.get(position);
        if(onlineMusic!=null) {
            viewHolder.textViewName.setText(onlineMusic.getTitle());
            Glide.with(context).load(onlineMusic.getPic_big()).into(viewHolder.imageView);
            viewHolder.textViewLong.setText(onlineMusic.getArtist_name()+" - "+ onlineMusic.getTitle());
            viewHolder.textViewSize.setVisibility(View.GONE);
        }else{
            viewHolder.textViewName.setText("mediaItem.getName()");
            viewHolder.textViewLong.setText("ddddddddd");
            viewHolder.textViewSize.setText("daaaaaaaaa");
        }
        return convertView;
    }


class ViewHolder{
    ImageView imageView;
    TextView textViewName;
    TextView textViewLong;
    TextView textViewSize;

}
}
