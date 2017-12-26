package com.demo.duke.videoplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "spalshThreadName";
    private Handler handler = new Handler();
    private boolean isStartMain = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO: 2017/12/25  三秒跳主页
                startMainActivity();
                Log.e(TAG,"当前线程名字："+ Thread.currentThread().getName());
            }
        },3*1000);
    }

    /**
     * 跳转mainactivity  ，并且关闭当前activity
     */
    private void startMainActivity() {

        if(!isStartMain) {
            isStartMain = true;
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        startMainActivity();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
