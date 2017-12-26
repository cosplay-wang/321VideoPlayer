package com.demo.duke.videoplayer;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.demo.duke.videoplayer.base.BaseFragment;
import com.demo.duke.videoplayer.pager.AudioPager;
import com.demo.duke.videoplayer.pager.BasePager;
import com.demo.duke.videoplayer.pager.NetAudioPager;
import com.demo.duke.videoplayer.pager.NetVideoPager;
import com.demo.duke.videoplayer.pager.VideoPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {


    RadioGroup radioGroup;
    FrameLayout frameLayout;
    private List<BasePager> basePagers;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
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

    private void setFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_content, new BaseFragment(getBasePager()),null);
        ft.commit();
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


}
