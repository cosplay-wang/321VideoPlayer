package com.demo.duke.videoplayer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.demo.duke.videoplayer.util.TimeUtil;
import com.demo.duke.videoplayer.view.FullScreenVideoView;

public class SystemVideoPlayer extends Activity implements View.OnClickListener {
    private static final int PROGRESS = 1;
    FullScreenVideoView videoView;
    private Uri uri;

    private LinearLayout llTop;
    private TextView tvName;
    private ImageView ivBattery;
    private TextView tvTime;
    private Button bgnVoice;
    private SeekBar seekbarVideoVoice;
    private Button btnSwitchPlayer;
    private LinearLayout llBottom;
    private TextView tvCurrentTime;
    private SeekBar seekbarVideo;
    private TextView tvDurtion;
    private Button btnExit;
    private Button btnPre;
    private Button btnStartPause;
    private Button btnNext;
    private Button btnSwitchVideoScreen;
    MediaController mediaController;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-12-28 15:21:50 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.activity_system_video_player);
        llTop = (LinearLayout) findViewById(R.id.ll_top);
        tvName = (TextView) findViewById(R.id.tv_name);
        ivBattery = (ImageView) findViewById(R.id.iv_battery);
        tvTime = (TextView) findViewById(R.id.tv_time);
        bgnVoice = (Button) findViewById(R.id.bgn_voice);
        seekbarVideoVoice = (SeekBar) findViewById(R.id.seekbar_video_voice);
        btnSwitchPlayer = (Button) findViewById(R.id.btn_switch_player);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        tvCurrentTime = (TextView) findViewById(R.id.tv_current_time);
        seekbarVideo = (SeekBar) findViewById(R.id.seekbar_video);
        tvDurtion = (TextView) findViewById(R.id.tv_durtion);
        btnExit = (Button) findViewById(R.id.btn_exit);
        btnPre = (Button) findViewById(R.id.btn_pre);
        btnStartPause = (Button) findViewById(R.id.btn_start_pause);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnSwitchVideoScreen = (Button) findViewById(R.id.btn_switch_video_screen);

        videoView = findViewById(R.id.video_view);

        bgnVoice.setOnClickListener(this);
        btnSwitchPlayer.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnPre.setOnClickListener(this);
        btnStartPause.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnSwitchVideoScreen.setOnClickListener(this);
        seekbarVideo.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
        videoView.setVideoViewLayoutParams(1);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-12-28 15:21:50 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == bgnVoice) {
            // Handle clicks for bgnVoice
        } else if (v == btnSwitchPlayer) {
            // Handle clicks for btnSwitchPlayer
        } else if (v == btnExit) {
            // Handle clicks for btnExit
            if (videoView != null) {
                videoView.stopPlayback();

            }
        } else if (v == btnPre) {
            // Handle clicks for btnPre
            if (videoView.getCurrentPosition() - 1000 >= 0) {
                videoView.seekTo(videoView.getCurrentPosition() - 1000);
            } else {
                videoView.seekTo(0);
            }
            handler.sendEmptyMessage(PROGRESS);


        } else if (v == btnStartPause) {
            if (videoView.isPlaying()) {
                //暂停--设置为暂停
                //按钮设置为播放
                videoView.pause();
                btnStartPause.setBackgroundResource(R.drawable.btn_start_pause_start_select);
            } else {
                //视频播放
                //按钮设置为暂停
                videoView.start();
                btnStartPause.setBackgroundResource(R.drawable.btn_start_pause_pause_select);
            }

            // Handle clicks for btnStartPause
        } else if (v == btnNext) {
            // Handle clicks for btnNext
            if (videoView.getCurrentPosition() + 1000 <= videoView.getDuration()) {
                videoView.seekTo(videoView.getCurrentPosition() + 1000);
            } else {
                videoView.seekTo(videoView.getDuration());
            }
            handler.sendEmptyMessage(PROGRESS);

        } else if (v == btnSwitchVideoScreen) {
            // Handle clicks for btnSwitchVideoScreen
           if(videoView.getParams()){
               videoView.setVideoViewLayoutParams(2);
           }else{
               videoView.setVideoViewLayoutParams(1);
           }

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findViews();


        //准备好的监听
        videoView.setOnPreparedListener(new MyOnPreparedListener());


        //播放出错了的监听
        videoView.setOnErrorListener(new MyOnErrorListener());


        // 播放完了的监听
        videoView.setOnCompletionListener(new MyOnCompletionListener());

        // TODO: 2017/12/27  得到播放地址
        uri = getIntent().getData();
        if (uri != null) {
            videoView.setVideoURI(uri);//有可能是网络地址，所以有回调
            // videoView.setVideoPath("http://m.youku.com/video/id_XMTQ5NjkwMzQ0.html?source=");
        }
        mediaController = new MediaController(this);
        //设置控制面板
        videoView.setMediaController(mediaController);//本质上加上了一个帧布局，通过接口加上监听，
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {
        //当底层解码完毕
        @Override
        public void onPrepared(MediaPlayer mp) {
            videoView.start();//准备好了，开始播放
            int duration = videoView.getDuration();//视频的总时长   mp.getDuration(); 关联总长度
            seekbarVideo.setMax(duration);
            tvDurtion.setText(TimeUtil.getFormatedDateTime("HH:mm:ss", duration));
            //发消息
            handler.sendEmptyMessage(PROGRESS);

        }
    }
    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            videoView.seekTo(progress);
            handler.sendEmptyMessage(PROGRESS);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROGRESS:
                    //1.得到当前的播放进度
                    int currentPisition = videoView.getCurrentPosition();
                    //2. 设置seekbar setprogress
                    seekbarVideo.setProgress(currentPisition);
                    //3 每秒更新一次
                    removeMessages(PROGRESS);
                    handler.sendEmptyMessageDelayed(PROGRESS, 1 * 1000);
                    //4 更新时间
                    tvCurrentTime.setText(TimeUtil.getFormatedDateTime("HH:mm:ss", currentPisition));
                    break;
            }
        }
    };

    class MyOnErrorListener implements MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            switch (what){
                case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                    Log.e("text","发生未知错误");

                    break;
                case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                    Log.e("text","媒体服务器死机");
                    break;
                default:
                    Log.e("text","onError+"+what);
                    break;
            }
            switch (extra){
                case MediaPlayer.MEDIA_ERROR_IO:
                    //io读写错误
                    Log.e("text","文件或网络相关的IO操作错误");
                    break;
                case MediaPlayer.MEDIA_ERROR_MALFORMED:
                    //文件格式不支持
                    Log.e("text","比特流编码标准或文件不符合相关规范");
                    break;
                case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                    //一些操作需要太长时间来完成,通常超过3 - 5秒。
                    Log.e("text","操作超时");
                    break;
                case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                    //比特流编码标准或文件符合相关规范,但媒体框架不支持该功能
                    Log.e("text","比特流编码标准或文件符合相关规范,但媒体框架不支持该功能");
                    break;
                default:
                    Log.e("text","onError+"+extra);
                    break;
            }
            Toast.makeText(SystemVideoPlayer.this, "出错了", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(SystemVideoPlayer.this, "播放完成：" + uri, Toast.LENGTH_SHORT).show();
        }
    }
    // TODO: 2017/12/28

    /**
     * 视频seekbar的更新
     * 1.视频的总时长  seekbar的setmax
     * 2 实例化handler 每秒得到当前的播放进度，设置setprogress
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (videoView != null) {
            videoView.stopPlayback();

        }
    }
}
