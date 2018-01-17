package com.demo.duke.videoplayer.service;

import com.demo.duke.videoplayer.domain.MediaItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhiwei.wang on 2018/1/17.
 * wechat 760560322
 * 作用：
 */

public class MusicPlayCenter {
    private final List<MediaItem> mediaItemList = new ArrayList<>();
    PlayMusicService playMusicService;

    private MusicPlayCenter() {
    }

    private static class MusicPlayCenterSingle {
        private static final MusicPlayCenter instance = new MusicPlayCenter();
    }

    public static MusicPlayCenter getMusicPlayCenter() {
        return MusicPlayCenterSingle.instance;
    }

    public List<MediaItem> getMediaItemList() {
        return mediaItemList;
    }

    public PlayMusicService getPlayMusicService() {
        return playMusicService;
    }

    public void setPlayMusicService(PlayMusicService playMusicService) {
        this.playMusicService = playMusicService;
    }
}
