package com.demo.duke.videoplayer;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.demo.duke.videoplayer.base.BaseFragment;
import com.demo.duke.videoplayer.pager.AudioPager;
import com.demo.duke.videoplayer.pager.BasePager;
import com.demo.duke.videoplayer.pager.NetAudioPager;
import com.demo.duke.videoplayer.pager.NetVideoPager;
import com.demo.duke.videoplayer.pager.VideoPager;
import com.demo.duke.videoplayer.service.MusicPlayCenter;
import com.demo.duke.videoplayer.service.PlayMusicService;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends FragmentActivity implements EasyPermissions.PermissionCallbacks {

    PlayMusicServiceBinder playMusicServiceBinder;
    public static final String TAGG = "lifeactvity";
    RadioGroup radioGroup;
    FrameLayout frameLayout;
    private List<BasePager> basePagers;
    private int position;
    final public int permissionRequestCode = 9;
    String permissionss[] = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE
    };
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPermission();
        //  bindView();
        Log.e(TAGG, "oncreateA");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAGG, "onStartA");
        bindService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAGG, "onPauseA");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAGG, "onResumeA");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAGG, "onRestartA");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAGG, "onStopA");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAGG, "onDestroyA");
        if (playMusicServiceBinder != null) {
            unbindService(playMusicServiceBinder);
        }
    }

    private void bindView() {
        radioGroup = findViewById(R.id.rg_button_tag);
        frameLayout = findViewById(R.id.fl_content);

        basePagers = new ArrayList<>();
        basePagers.add(new VideoPager(this));//本地视频页面 position 0；
        basePagers.add(new AudioPager(this));//本地音频页面 position 1；
        basePagers.add(new NetVideoPager(this));//网络视频页面 position 2；
        basePagers.add(new NetAudioPager(this));//w网络音频页面 position 3；
        radioGroup.setOnCheckedChangeListener(new MyOnCheckChangeListener());
        radioGroup.check(R.id.rb_video);// TODO: 2017/12/25 默认选中第一个
    }

    class MyOnCheckChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                default:
                    position = 0;
                    break;
                case R.id.rb_video:
                    position = 0;
                    break;
                case R.id.rb_audio:
                    position = 1;
                    break;
                case R.id.rb_net_video:
                    position = 2;
                    break;
                case R.id.rb_net_audio:
                    position = 3;
                    break;
            }
            setFragment();

        }
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setClass(AppLication.context, PlayMusicService.class);
        playMusicServiceBinder = new PlayMusicServiceBinder();
        bindService(intent, playMusicServiceBinder, Context.BIND_AUTO_CREATE);
    }

    class PlayMusicServiceBinder implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicPlayCenter.getMusicPlayCenter().setPlayMusicService(((PlayMusicService.PlayBinder) service).getService());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    private void setFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_content, new BaseFragment(getBasePager()), null);
        ft.commitAllowingStateLoss();
    }

    /**
     * 得到对应的view
     *
     * @return
     */
    private BasePager getBasePager() {
        BasePager basePager = basePagers.get(position);
        return basePager;
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {

        if (EasyPermissions.hasPermissions(this, permissionss)) {
            Log.e(TAGG, "hasPermissionsbindviews");
            bindView();
        } else {
            Log.e(TAGG, "requestPermissions");
            EasyPermissions.requestPermissions(this, "", permissionRequestCode, permissionss);
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.e(TAGG, "onPermissionsGranted" + perms.size());
        for (int i = 0; i < perms.size(); i++) {    //通过循环输出列表中的内容
            if (perms.get(i).equals(permissionss[4])) {
                Log.e(TAGG, "onPermissionsGrantedbindviews");
                bindView();
            }
        }


    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.e(TAGG, "onPermissionsDenied");
        finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onStop();
        onDestroy();
    }
}
