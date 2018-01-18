package com.demo.duke.videoplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.demo.duke.videoplayer.domain.MediaItem;
import com.demo.duke.videoplayer.util.PreferencesUtil;

import java.io.IOException;


public class PlayMusicService extends Service {

    MediaPlayer mediaPlayer = new MediaPlayer();
    public MediaItem playingMediaItem;
    OnPlayEventListener onPlayEventListener;
    private int currentPosition = -1;
    private static final int PROGRESS = 2;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROGRESS:
                    if (mediaPlayer != null) {
                        if (mediaPlayer.isPlaying()) {
                            //1.得到当前的播放进度

                            int currentPisition = mediaPlayer.getCurrentPosition();
                            //2. 设置seekbar setprogress
                            onPlayEventListener.updateProgress(currentPisition);
                            //3 每秒更新一次
                            removeMessages(PROGRESS);

                            handler.sendEmptyMessageDelayed(PROGRESS, 1 * 1000);

                        }
                        break;
                    }
            }
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
    }

    public MediaItem getPlayingMediaItem() {
        return playingMediaItem;
    }

    public void setOnPlayEventListener(OnPlayEventListener onPlayEventListener) {
        this.onPlayEventListener = onPlayEventListener;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void play(int position) {
        currentPosition = position;
        playingMediaItem = MusicPlayCenter.getMusicPlayCenter().getMediaItemList().get(position);
        play(playingMediaItem);
    }

    public void play(MediaItem mediaItem) {
        Uri uri = Uri.parse(mediaItem.getData());
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(this, uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MyPreparedListener());
        mediaPlayer.setOnBufferingUpdateListener(new MyBufferUpdateListener());
        mediaPlayer.setOnErrorListener(new MyErrorListener());
        mediaPlayer.setOnCompletionListener(new MyCompletionLiatener());
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void start() {

        mediaPlayer.start();
        //发消息
        Message message = handler.obtainMessage();
        message.obj = mediaPlayer.getCurrentPosition();
        message.what = PROGRESS;
        handler.sendMessage(message);
    }

    public void release() {
        mediaPlayer.release();
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public long getDuration() {
        return mediaPlayer.getDuration();
    }

    public void seekTo(int position) {
        mediaPlayer.seekTo(position);
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
            start();
            onPlayEventListener.setTotalTime(mediaPlayer.getDuration());
            onPlayEventListener.startView(true);

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
            Toast.makeText(getApplicationContext(), "播放错误", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    public void playNext() {
        PlayModeEnum mode = PlayModeEnum.valueOf(PreferencesUtil.getPlayMode());
        switch (mode) {
            case LOOP:
                if ((currentPosition + 1) < MusicPlayCenter.getMusicPlayCenter().getMediaItemList().size()) {
                    play(currentPosition + 1);
                } else {
                    Toast.makeText(getApplicationContext(), "没有下一首了", Toast.LENGTH_SHORT).show();
                }
                break;
            case RANDOM:
                int songNum = (int) (Math.random() * MusicPlayCenter.getMusicPlayCenter().getMediaItemList().size());
                play(songNum);
                break;
            case SINGLE:
                play(currentPosition);
                break;
        }
        onPlayEventListener.updatePlayView(playingMediaItem);


    }

    public void playPre() {


        PlayModeEnum mode = PlayModeEnum.valueOf(PreferencesUtil.getPlayMode());
        switch (mode) {
            case LOOP:
                if ((currentPosition - 1) >= 0) {
                    play(currentPosition - 1);
                    onPlayEventListener.updatePlayView(playingMediaItem);
                } else {
                    Toast.makeText(getApplicationContext(), "没有上一首了", Toast.LENGTH_SHORT).show();
                }
                break;
            case RANDOM:
                int songNum = (int) (Math.random() * MusicPlayCenter.getMusicPlayCenter().getMediaItemList().size());
                play(songNum);

                break;
            case SINGLE:
                play(currentPosition);
                break;
        }
        onPlayEventListener.updatePlayView(playingMediaItem);


    }

    private class MyCompletionLiatener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            playNext();
            Log.e("oncomple", "playNext");
        }
    }
}
