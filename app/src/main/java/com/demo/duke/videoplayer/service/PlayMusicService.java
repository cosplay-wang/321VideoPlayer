package com.demo.duke.videoplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.demo.duke.videoplayer.domain.MediaItem;

import java.io.IOException;

import io.vov.vitamio.utils.Log;

public class PlayMusicService extends Service {
    MediaPlayer mediaPlayer = new MediaPlayer();
    MediaItem playingMediaItem;
    private int currentPosition = -1;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    public void play(int position){
        currentPosition = position;
        MediaItem mediaItem = MusicPlayCenter.getMusicPlayCenter().getMediaItemList().get(position);
        play(mediaItem);
    }
    public void play(MediaItem mediaItem){
        Uri uri = Uri.parse(mediaItem.getData());
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(this,uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MyPreparedListener());
        mediaPlayer.setOnBufferingUpdateListener(new MyBufferUpdateListener());
        mediaPlayer.setOnErrorListener(new MyErrorListener());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new PlayBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
    public class PlayBinder extends Binder {
        public PlayMusicService getService() {
            return PlayMusicService.this;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private class MyPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mediaPlayer.start();
        }
    }

    private class MyBufferUpdateListener implements MediaPlayer.OnBufferingUpdateListener {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {

        }
    }

    private class MyErrorListener implements MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Toast.makeText(getApplicationContext(),"播放错误",Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}
