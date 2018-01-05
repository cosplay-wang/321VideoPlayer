package com.demo.duke.videoplayer;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.demo.duke.videoplayer.util.TimeUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SurfaceViewMediaPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    private SurfaceView surfaceview;
    private ProgressBar progressBar;
    private Button buttonPlay;
    private Button buttonReplay;
    private Button buttonScreenShot;
    private Button buttonChangeVedioSize;
    private TextView textViewShowTime;
    private SeekBar seekbar;
    SurfaceHolder surfaceHolder;
    MediaPlayer mediaPlayer;
    public final static int PROGRESS = 0;
    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-01-04 09:27:55 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.activity_surface_view_media_player);
        surfaceview = (SurfaceView) findViewById(R.id.surfaceview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        buttonPlay = (Button) findViewById(R.id.button_play);
        buttonReplay = (Button) findViewById(R.id.button_replay);
        buttonScreenShot = (Button) findViewById(R.id.button_screenShot);
        buttonChangeVedioSize = (Button) findViewById(R.id.button_changeVedioSize);
        textViewShowTime = (TextView) findViewById(R.id.textView_showTime);
        seekbar = (SeekBar) findViewById(R.id.seekbar);

        buttonPlay.setOnClickListener(this);
        buttonReplay.setOnClickListener(this);
        buttonScreenShot.setOnClickListener(this);
        buttonChangeVedioSize.setOnClickListener(this);
        seekbar.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2018-01-04 09:27:55 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == buttonPlay) {
            // Handle clicks for buttonPlay
        } else if (v == buttonReplay) {
            // Handle clicks for buttonReplay
        } else if (v == buttonScreenShot) {
            // Handle clicks for buttonScreenShot
        } else if (v == buttonChangeVedioSize) {
            // Handle clicks for buttonChangeVedioSize
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        findViews();
        initSurFaceView();
    }

    void initSurFaceView() {
        surfaceHolder = surfaceview.getHolder();
       // 设置Holder类型,该类型表示surfaceView自己不管理缓存区,虽然提示过时，但最好还是要设置
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
      // 设置surface回调
        surfaceHolder.addCallback(new SurfaceCallback());
    }
    // SurfaceView的callBack  
    private class SurfaceCallback implements SurfaceHolder.Callback {
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // SurfaceView的大小改变  
        }

        public void surfaceCreated(SurfaceHolder holder) {
            // surfaceView被创建  
            // 设置播放资源  
            playVideo();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // surfaceView销毁  
            // 如果MediaPlayer没被销毁，则销毁mediaPlayer  
            if (null != mediaPlayer) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
    }

    /**
     * 播放视频
     */
    public void playVideo() {
        // 初始化MediaPlayer
        mediaPlayer = new MediaPlayer();
        // 重置mediaPaly,建议在初始滑mediaplay立即调用。
        mediaPlayer.reset();
        // 设置声音效果
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        // 设置播放完成监听
      //  mediaPlayer.setOnCompletionListener(this);
        // 设置媒体加载完成以后回调函数。
        mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
        // 错误监听回调函数
       // mediaPlayer.setOnErrorListener(this);
        // 设置缓存变化监听
      //  mediaPlayer.setOnBufferingUpdateListener(this);
        Uri uri = Uri
                .parse("http://ips.ifeng.com/video19.ifeng.com/video09/2017/05/24/4664192-102-008-1012.mp4");
        try {
            mediaPlayer.setDataSource(this, uri);
            // 设置异步加载视频，包括两种方式 prepare()同步，prepareAsync()异步
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {
        //当底层解码完毕
        @Override
        public void onPrepared(MediaPlayer mp) {
            // 当视频加载完毕以后，隐藏加载进度条
            progressBar.setVisibility(View.GONE);
            // 判断是否有保存的播放位置,防止屏幕旋转时，界面被重新构建，播放位置丢失。
//            if (Constants.playPosition >= 0) {
//                mediaPlayer.seekTo(Constants.playPosition);
//                Constants.playPosition = -1;
//                // surfaceHolder.unlockCanvasAndPost(Constants.getCanvas());
//            }
            // 播放视频
            mediaPlayer.start();
            // 设置显示到屏幕
            mediaPlayer.setDisplay(surfaceHolder);//绑定视频显示区域
            // 设置surfaceView保持在屏幕上
          //  mediaPlayer.setScreenOnWhilePlaying(true);
           // surfaceHolder.setKeepScreenOn(true);
            // 设置控制条,放在加载完成以后设置，防止获取getDuration()错误
            seekbar.setProgress(0);
            seekbar.setMax(mediaPlayer.getDuration());
            // 设置播放时间
//            videoTimeString = getShowTime(mediaPlayer.getDuration());
//            textViewShowTime.setText("00:00:00/" + videoTimeString);

            //发消息
            handler.sendEmptyMessage(PROGRESS);

        }
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
                    seekbar.setProgress(currentPisition);
                    //3 每秒更新一次
                    removeMessages(PROGRESS);
                    handler.sendEmptyMessageDelayed(PROGRESS, 1 * 1000);
                    //4 更新时间
                  //  textViewShowTime.setText(TimeUtil.getFormatedDateTime("HH:mm:ss", currentPisition));
                    break;
            }
        }
    };

    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (progress >= 0) {
                // 如果是用户手动拖动控件，则设置视频跳转。
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
//                // 设置当前播放时间
//                vedioTiemTextView.setText(getShowTime(progress) + "/" + videoTimeString);
            }
            handler.sendEmptyMessage(PROGRESS);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
    /**
     * 转换播放时间
     *
     * @param milliseconds 传入毫秒值
     * @return 返回 hh:mm:ss或mm:ss格式的数据
     */
    @SuppressLint("SimpleDateFormat")
    public String getShowTime(long milliseconds) {
        // 获取日历函数
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        SimpleDateFormat dateFormat = null;
        // 判断是否大于60分钟，如果大于就显示小时。设置日期格式
        if (milliseconds / 60000 > 60) {
            dateFormat = new SimpleDateFormat("hh:mm:ss");
        } else {
            dateFormat = new SimpleDateFormat("mm:ss");
        }
        return dateFormat.format(calendar.getTime());
    }
}
