package com.demo.duke.videoplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.demo.duke.videoplayer.domain.MediaItem;
import com.demo.duke.videoplayer.service.MusicPlayCenter;
import com.demo.duke.videoplayer.service.OnPlayEventListener;
import com.demo.duke.videoplayer.service.PlayModeEnum;
import com.demo.duke.videoplayer.service.PlayMusicService;
import com.demo.duke.videoplayer.util.PreferencesUtil;
import com.demo.duke.videoplayer.util.TimeUtil;
import com.demo.duke.videoplayer.view.MusicPlayerView;

import java.util.prefs.Preferences;


public class SystemAudioPlayer extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, OnPlayEventListener {

    PlayMusicService playMusicService;
    private LinearLayout playTopLin;
    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvArtist;
    private MusicPlayerView musicplayView;
    private TextView tvCurrentTime;
    private SeekBar sbProgress;
    private TextView tvTotalTime;
    private ImageView ivMode;
    private ImageView ivPrev;
    private ImageView ivPlay;
    private ImageView ivNext;
    MediaItem mediaItem;
    private boolean isDraggingProgress;

    private void findViews() {
        setContentView(R.layout.activity_system_audio_player);
        playTopLin = (LinearLayout) findViewById(R.id.play_top_lin);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvArtist = (TextView) findViewById(R.id.tv_artist);
        musicplayView = (MusicPlayerView) findViewById(R.id.musicplay_view);
        tvCurrentTime = (TextView) findViewById(R.id.tv_current_time);
        sbProgress = (SeekBar) findViewById(R.id.sb_progress);
        tvTotalTime = (TextView) findViewById(R.id.tv_total_time);
        ivMode = (ImageView) findViewById(R.id.iv_mode);
        ivPrev = (ImageView) findViewById(R.id.iv_prev);
        ivPlay = (ImageView) findViewById(R.id.iv_play);
        ivNext = (ImageView) findViewById(R.id.iv_next);
        ivPlay.setOnClickListener(this);
        ivPrev.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        ivMode.setOnClickListener(this);
        sbProgress.setOnSeekBarChangeListener(this);

        playMusicService = MusicPlayCenter.getMusicPlayCenter().getPlayMusicService();
        playMusicService.setOnPlayEventListener(this);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-12-29 10:34:15 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_play) {
            if (playMusicService.isPlaying()) {
                ivPlay.setSelected(false);
                musicplayView.pauseMusic();
                pause();
            } else {
                ivPlay.setSelected(true);
                musicplayView.playMusic();
                playMusicService.start();
            }
        } else if (v.getId() == R.id.iv_next) {
            playMusicService.playNext();
        } else if (v.getId() == R.id.iv_prev) {
            playMusicService.playPre();
        }else if(v.getId() == R.id.iv_mode){
            switchPlayMode();
        }
    }
    private void switchPlayMode() {
        PlayModeEnum mode = PlayModeEnum.valueOf(PreferencesUtil.getPlayMode());
        switch (mode) {
            case LOOP:
                mode = PlayModeEnum.RANDOM;
                break;
            case RANDOM:
                mode = PlayModeEnum.SINGLE;
                break;
            case SINGLE:
                mode = PlayModeEnum.LOOP;
                break;
        }
        PreferencesUtil.savePlayMode(mode.value());
        initPlayMode();
    }
    private void initPlayMode() {
        int mode = PreferencesUtil.getPlayMode();
        ivMode.setImageLevel(mode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
        mediaItem = (MediaItem) getIntent().getSerializableExtra("mediaItem");
        updatePlayView(mediaItem);
        initPlayMode();
    }


    @Override
    public void updateProgress(int progress) {
        if (!isDraggingProgress) {
            sbProgress.setProgress(progress);
            //4 更新时间
            tvCurrentTime.setText(TimeUtil.getFormatedDateTime("mm:ss", progress));
        }

    }


    /**
     * 暂停
     */
    public void pause() {
        if (playMusicService != null) {
            playMusicService.pause();
        }
    }

    /**
     * 停止
     */
    public void stop() {
        if (playMusicService != null) {
            /**
             * MediaPlayer.release()方法可销毁MediaPlayer的实例。销毁是“停止”的一种具有攻击意味的说法，
             * 但我们有充足的理由使用销毁一词。
             * 除非调用MediaPlayer.release()方法，否则MediaPlayer将一直占用着音频解码硬件及其它系统资源
             * 。而这些资源是由所有应用共享的。
             * MediaPlayer有一个stop()方法。该方法可使MediaPlayer实例进入停止状态，等需要时再重新启动
             * 。不过，对于简单的音频播放应用，建议 使用release()方法销毁实例，并在需要时进行重见。基于以上原因，有一个简单可循的规则：
             * 只保留一个MediaPlayer实例，保留时长即音频文件 播放的时长。
             */
            playMusicService.stop();
            //   playMusicService.release();
            playMusicService = null;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        //   stop();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //4 更新时间
        tvCurrentTime.setText(TimeUtil.getFormatedDateTime("mm:ss", progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isDraggingProgress = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        if (playMusicService != null) {
            isDraggingProgress = false;
            playMusicService.seekTo(seekBar.getProgress());

        }


    }

    @Override
    public void setTotalTime(int totalTime) {
        sbProgress.setMax(totalTime);
        tvTotalTime.setText(TimeUtil.getFormatedDateTime("mm:ss", totalTime));
    }

    @Override
    public void startView(boolean isStart) {
        if (isStart) {
            ivPlay.setSelected(true);
            musicplayView.playMusic();
        } else {
            ivPlay.setSelected(false);
            musicplayView.pauseMusic();
        }
    }

    @Override
    public void updatePlayView(MediaItem mediaItem) {
        if (!mediaItem.getName().isEmpty()) {
            tvTitle.setText(mediaItem.getName());
        }
        if (!mediaItem.getArtist().isEmpty()) {
            tvArtist.setText(mediaItem.getArtist());
        }
        musicplayView.setCover(mediaItem.getCover());
    }
}
