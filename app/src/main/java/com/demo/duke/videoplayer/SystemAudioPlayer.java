package com.demo.duke.videoplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.duke.videoplayer.util.TimeUtil;

import java.io.File;
import java.io.IOException;

public class SystemAudioPlayer extends AppCompatActivity implements View.OnClickListener {
    private static final int PROGRESS = 2;
    private LinearLayout llBottom;
    private TextView tvCurrentTime;
    private SeekBar seekbarAudio;
    private TextView tvDurtion;
    private Button btnExit;
    private Button btnPre;
    private Button btnStartPause;
    private Button btnNext;
    private Button btnSwitchAudioScreen;
    private MediaPlayer mediaPlayer;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-12-29 10:34:15 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.activity_system_audio_player);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        tvCurrentTime = (TextView) findViewById(R.id.tv_current_time);
        seekbarAudio = (SeekBar) findViewById(R.id.seekbar_audio);
        tvDurtion = (TextView) findViewById(R.id.tv_durtion);
        btnExit = (Button) findViewById(R.id.btn_exit);
        btnPre = (Button) findViewById(R.id.btn_pre);
        btnStartPause = (Button) findViewById(R.id.btn_start_pause);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnSwitchAudioScreen = (Button) findViewById(R.id.btn_switch_audio_screen);

        btnExit.setOnClickListener(this);
        btnPre.setOnClickListener(this);
        btnStartPause.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnSwitchAudioScreen.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-12-29 10:34:15 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == btnExit) {
            // Handle clicks for btnExit
        } else if (v == btnPre) {
            // Handle clicks for btnPre
        } else if (v == btnStartPause) {

            if (mediaPlayer.isPlaying()) {
                //暂停--设置为暂停
                //按钮设置为播放
                pause();
                btnStartPause.setBackgroundResource(R.drawable.btn_start_pause_start_select);
            } else {
                //视频播放
                //按钮设置为暂停
                mediaPlayer.start();
                btnStartPause.setBackgroundResource(R.drawable.btn_start_pause_pause_select);
            }


            // Handle clicks for btnStartPause
        } else if (v == btnNext) {
            // Handle clicks for btnNext
        } else if (v == btnSwitchAudioScreen) {
            // Handle clicks for btnSwitchAudioScreen
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
        Uri uri = getIntent().getData();
        try {
            if (uri != null) {
                mediaPlayer = new MediaPlayer();

                mediaPlayer.setDataSource(this, uri);

                mediaPlayer.prepareAsync();
            } else {
                mediaPlayer = MediaPlayer.create(this, R.raw.ywcbs);
                mediaPlayer.prepare();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());
        mediaPlayer.setOnErrorListener(new MyOnErrorListener());
        mediaPlayer.setOnBufferingUpdateListener(new MyOnBufferingUpdateListener());//缓冲的进度  ，不是播放的进度
        mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROGRESS:
                    //1.得到当前的播放进度

                    int currentPisition = mediaPlayer.getCurrentPosition();
                    //2. 设置seekbar setprogress
                    seekbarAudio.setProgress(currentPisition);
                    //3 每秒更新一次
                    removeMessages(PROGRESS);
                    if(mediaPlayer.isPlaying()){


                    handler.sendEmptyMessageDelayed(PROGRESS, 1 * 1000);
                    //4 更新时间
                    tvCurrentTime.setText(TimeUtil.getFormatedDateTime("HH:mm:ss", currentPisition));
                    }
                    break;
            }
        }
    };

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {
        //当底层解码完毕
        @Override
        public void onPrepared(MediaPlayer mp) {
            mediaPlayer.start();
            int duration = mp.getDuration();//音频的总时长   mp.getDuration(); 关联总长度
            seekbarAudio.setMax(duration);
            tvDurtion.setText(TimeUtil.getFormatedDateTime("HH:mm:ss", duration));
            Log.e("TimeUtil", TimeUtil.getFormatedDateTime("HH:mm:ss", duration));

            //发消息
            Message message = handler.obtainMessage();
            message.obj = mp.getCurrentPosition();
            message.what = PROGRESS;
            handler.sendMessage(message);

        }
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Toast.makeText(SystemAudioPlayer.this, "出错了", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    /**
     * 停止
     */
    public void stop() {
        if (mediaPlayer != null) {
            /**
             * MediaPlayer.release()方法可销毁MediaPlayer的实例。销毁是“停止”的一种具有攻击意味的说法，
             * 但我们有充足的理由使用销毁一词。
             * 除非调用MediaPlayer.release()方法，否则MediaPlayer将一直占用着音频解码硬件及其它系统资源
             * 。而这些资源是由所有应用共享的。
             * MediaPlayer有一个stop()方法。该方法可使MediaPlayer实例进入停止状态，等需要时再重新启动
             * 。不过，对于简单的音频播放应用，建议 使用release()方法销毁实例，并在需要时进行重见。基于以上原因，有一个简单可循的规则：
             * 只保留一个MediaPlayer实例，保留时长即音频文件 播放的时长。
             */
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            stop();
            Toast.makeText(SystemAudioPlayer.this, "播放完成：", Toast.LENGTH_SHORT).show();
        }
    }

    class MyOnBufferingUpdateListener implements MediaPlayer.OnBufferingUpdateListener {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            seekbarAudio.setProgress(mp.getCurrentPosition());
            tvCurrentTime.setText(TimeUtil.getFormatedDateTime("HH:mm:ss", mp.getCurrentPosition()));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stop();
    }
}
