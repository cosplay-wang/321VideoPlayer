package com.demo.duke.videoplayer;

import android.app.Activity;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import static com.demo.duke.videoplayer.MainActivity.TAGG;

public class LifeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life);
        Log.e(TAGG,"onCreateB");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAGG,"onStartB");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAGG,"onPauseB");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAGG,"onResumeB");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAGG,"onStopB");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAGG,"onDestroyB");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAGG,"onRestartB");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
