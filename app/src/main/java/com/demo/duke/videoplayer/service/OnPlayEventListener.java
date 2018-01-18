package com.demo.duke.videoplayer.service;

import com.demo.duke.videoplayer.domain.MediaItem;

/**
 * Created by zhiwei.wang on 2018/1/18.
 * wechat 760560322
 * 作用：
 */

public interface OnPlayEventListener {

    void updateProgress(int progress);
    void setTotalTime(int totalTime);
    void startView(boolean isStart);
    void updatePlayView(MediaItem mediaItem);
}
